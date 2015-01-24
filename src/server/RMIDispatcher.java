package server;

import interfaces.Remote640;
import interfaces.StubClass;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.RMIException;
import data.RMIInvocationMessage;
import data.RMIInvocationReply;
import data.RMIMessage;
import data.RMIMessage.RMIMessageType;


/**
 * RMI Dispatcher class. Handles request for remote invocation of methods. It does this by starting a thread for 
 * each incoming request. 
 * @author surajd
 *
 */
class RMIDispatcher implements Runnable
{
	private static final int MAX_NUM_PARALLEL_REQUESTS = 50;
	
	private ServerSocket serverSocket;
	
	private ConcurrentHashMap<String,Object> serviceToObjectMap;
	private ExecutorService executorService;
	
	public RMIDispatcher(int port, ConcurrentHashMap serviceToObjectMap) throws IOException {
		this.serviceToObjectMap = serviceToObjectMap;
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(MAX_NUM_PARALLEL_REQUESTS);
	}
	

	@Override
	public void run() {
		
		while(true)
		{
			try 
			{			
				final Socket socket = serverSocket.accept();
				
				executorService.submit(new Runnable() {
					
					@Override
					public void run() {	
						try
						{
							handleRequest(socket);
						} 
						catch (Exception e) 
						{
							System.err.println("Error while invoking method.");
							e.printStackTrace();
						}
					}
				});
				
			}	
			catch (Exception e)
			{
				e.printStackTrace();				
			}
			
		}
		
	}
	
	/**
	 * Helper method to handle incoming requests on a socket.
	 * @param socket
	 * @throws Exception
	 */
	private void handleRequest(Socket socket) throws Exception
	{
		System.out.println(String.format("Handling a method invocation request from client %s." , socket.getInetAddress().getHostAddress()));
		ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		
		RMIMessage message = (RMIMessage)objectInputStream.readObject();
		
		if(message.getType().equals(RMIMessageType.INVOKE))
		{
			RMIInvocationMessage invokeMessage = (RMIInvocationMessage) message;
			handleInvokeRequest( objectOutputStream , invokeMessage);
		}
        else
        {
            RMIMessage reply = new RMIMessage();
            reply.setType(RMIMessageType.BAD_REQUEST);
            reply.setMessage("Dispatcher can only handle inovation requests.");
            reply.setException(new RMIException(new Exception("Invalid Message Type: " + message.getType())));

            objectOutputStream.writeObject(reply);
        }

		objectInputStream.close();
		objectOutputStream.close();
		
	}
	
	/**
	 * Method which handles remote method invocations.
	 * @param objectOutputStream
	 * @param invokeMessage
	 * @throws IOException
	 */
	private void handleInvokeRequest(ObjectOutputStream objectOutputStream , RMIInvocationMessage invokeMessage) throws IOException
	{
		RMIInvocationReply reply = new RMIInvocationReply();
		
		// name of the remote interface to implement.
		try 
		{
			String methodName = invokeMessage.getMethodName();
			Object[] methodArgs = invokeMessage.getArguments();
			Class<?>[] parameterTypes = invokeMessage.getArgumentTypes();
			String implClassName = invokeMessage.getImplClassName();
			String objectKeyId = invokeMessage.getObjectKeyId();
			implClassName = "services." + implClassName;
			Class<?> classToInstantiate = Class.forName(implClassName);
			Object localInstance = null;
			
			// replace the remote parameters, with the a local reference, if we can find one.
			for(int i = 0 ; i < methodArgs.length ; i++)
			{
				if(methodArgs[i] instanceof Remote640)
				{
					// get the remote Object ref associated with the stub.
					StubClass stubObject = (StubClass)methodArgs[i];
                    String remoteObjectKeyId = stubObject.getRemoteObjectReference().getObjectKeyId();
                    // Is the remote object located on this server. If yes - replace it here to avoid unnecessary
                    // and potentially bad socket communication. If it's located on another server, let's go ahead
                    // and use the stub itself.
                    // Now,
                    // We could have used InetAddress.getLocalHost() to check if this is the right server, but
                    // with multiple network interfaces, this may not return the correct IP. Let's just check
                    // if we have the object in our map, since objectkeyid is a global id.
                    if(serviceToObjectMap.containsKey(remoteObjectKeyId))
					    methodArgs[i] = serviceToObjectMap.get(remoteObjectKeyId);
				}
			}

			if(serviceToObjectMap.containsKey(objectKeyId))
			{
				localInstance = serviceToObjectMap.get(objectKeyId);
			}
			else
			{
				//object not registered.
				// should not happen.
				reply.setType(RMIMessageType.INVOKE_FAILED);
				reply.setMessage("Invocation of method failed.");
				reply.setException(new RMIException(new IllegalArgumentException("The requested object is not present in the "
						+ " registered services on the server")));
				
				objectOutputStream.writeObject(reply);
			}
			
			System.out.println(String.format("Invoking method %s on class %s" , methodName , implClassName));
			Method method = classToInstantiate.getMethod(methodName, parameterTypes);
			Object returnVal = method.invoke(localInstance, methodArgs);
			
			
			reply.setType(RMIMessageType.INVOKE_SUCCEEDED);
			reply.setResult(returnVal);
			
			objectOutputStream.writeObject(reply);
		} 
		catch (Exception e) 
		{
			reply.setType(RMIMessageType.INVOKE_FAILED);
			reply.setMessage("Invocation of method failed.");
			reply.setException(new RMIException(e));
			
			objectOutputStream.writeObject(reply);
		}
	}
	
}