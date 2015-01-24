/**
 * 
 */
package server;

import interfaces.Remote640;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import data.HostInfo;
import data.RMIException;
import data.RMIMessage;
import data.RMIMessage.RMIMessageType;
import data.RMIRegisterServiceMessage;
import data.RMIRegistryLookupReply;
import data.RMIRegistryLookupRequest;
import data.RemoteObjectReference;
import data.ServicesList;

/**
 * Class that represents the Registry, which clients call to lookupAndLocalise/add remote references. This is implemented as a thread,
 * and it listens on a socket for registrayion/lookupAndLocalise requests from clients.
 * @author surajd
 *
 */
public class RMIGlobalRegistry implements Runnable {

	public static final int RMI_GLOBAL_REGISTRY_PORT = 6666;
	// map from interface name to remote object reference for the registry.
	private Map<String,RemoteObjectReference> registeredServicesLookup;
	private ServerSocket serverSocket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	
	
	public RMIGlobalRegistry(int port) throws IOException
	{
		this.serverSocket = new ServerSocket(port);
		this.registeredServicesLookup = new HashMap<String, RemoteObjectReference>();
	}


	@Override
	public void run() {
		
		while(true)
		{
				Socket socket;
				RMIMessage message;
				try
				{
					socket = serverSocket.accept();
					outputStream = new ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
					message = (RMIMessage) inputStream.readObject();
				} 
				catch (Exception e) 
				{
					System.err.println("Unable to open socket for registry server");
					return;
				}
				
				
				if(message.getType().equals(RMIMessageType.LOOKUP))
				{
					// lookup request.
					RMIRegistryLookupRequest request = (RMIRegistryLookupRequest) message;
					RMIRegistryLookupReply reply = new RMIRegistryLookupReply();
					
					try 
					{						
						if(registeredServicesLookup.containsKey(request.getInterfaceName()))
						{
							// we have found a registered service.
							reply.setType(RMIMessageType.LOOKUP_REPLY_SUCCEEDED);
							RemoteObjectReference remoteRef = registeredServicesLookup.get(request.getInterfaceName());
							reply.setReference(remoteRef);
							outputStream.writeObject(reply);
						}
						else
						{
							//the requested service is not registered.
							reply.setType(RMIMessageType.LOOKUP_FAILED);
                            reply.setException(new RMIException(new IllegalArgumentException("The requested reference is not present in the "
                                    + " registry")));
							reply.setMessage(String.format("The requested service %s is not registered with the registry" , request.getInterfaceName() ));
							outputStream.writeObject(reply);
						}
					}
					catch (Exception e)
					{
						System.err.println("Failed to lookup service on the registry server");
						reply.setMessage("Lookup in registry failed.");
						reply.setType(RMIMessageType.LOOKUP_FAILED);
						try
						{
							outputStream.writeObject(reply);
						} 
						catch (IOException e1) {
							// cant really do anything about it.
							e1.printStackTrace();
						}
					}
				}
				else if(message.getType().equals(RMIMessageType.LIST_REGISTERED_SERVICES))
				{
					ServicesList servicesList = new ServicesList();
					
					for(Map.Entry<String, RemoteObjectReference> entry : registeredServicesLookup.entrySet())
					{
						String serviceName = entry.getKey();
                        String remoteObjectInfo = entry.getValue().toString();
                        servicesList.addService( serviceName, remoteObjectInfo);
					}
                    try {
                        RMIMessage reply = new RMIMessage();
                        reply.setMessage(servicesList.printServices());
                        reply.setType(RMIMessageType.LIST_REGISTERED_SERVICES_REPLY);

                        outputStream.writeObject(reply);
                    } catch (IOException e) {
                        RMIMessage reply = new RMIMessage();
                        System.err.println("Failed to lookup list of services on the registry server");
                        reply.setMessage("List Lookup in registry failed.");
                        reply.setType(RMIMessageType.LIST_REGISTERED_SERVICES_FAILED);
                        try
                        {
                            outputStream.writeObject(reply);
                        }
                        catch (IOException e1) {
                            // cant really do anything about it.
                            e1.printStackTrace();
                        }
                    }
				}
				else if(message.getType().equals(RMIMessageType.GLOBAL_REGISTER))
				{
					// somebody wants to register a service.
					RMIMessage reply = new RMIMessage();
					try 
					{
						RMIRegisterServiceMessage registerMessage = (RMIRegisterServiceMessage) message;
						
						register(registerMessage);
						
						reply.setType(RMIMessageType.GLOBAL_REGISTER_SUCCEEDED);
						outputStream.writeObject(reply);
					} 
					catch (Exception e) 
					{
						System.err.println("Failed to register method on the server");
						reply.setMessage("Call to register service failed");
						reply.setType(RMIMessageType.GLOBAL_REGISTER_FAILED);
						reply.setException(new RMIException(e));
						try
						{	
							outputStream.writeObject(reply);
						} 
						catch (IOException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			} 
		
		}
	
	/**
	 * Register the input object reference with the registry.
	 * @param message
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private void register(RMIRegisterServiceMessage message) throws ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		String interfaceName = message.getInterfaceName();
		HostInfo hostInfo = message.getHostInfo();
		// object which we are trying to register.
		// the name of the implementing interface.
		String className = message.getImplClassName();
		
		//enforce the constraint that Remote440 must be implemented.
		Class<?> implClass = Class.forName("services." + className);
		
		// check whether this instance of Remote440.
		Object implClassInstance = implClass.newInstance();
		
		if(! (implClassInstance instanceof Remote640) )
			throw new IllegalStateException("This object does not implement the remote640 interface. Cann"
					+ "ot be registered as a remote service.");
		
		// create a new remote object reference.
		RemoteObjectReference remoteRef = new RemoteObjectReference(hostInfo, interfaceName , className);
        remoteRef.setObjectKeyId(interfaceName);
		
		// add this service to the store.
		registeredServicesLookup.put(interfaceName , remoteRef);
		
		System.out.println(String.format("Registered service with name %s running on host %s" , interfaceName , hostInfo));
	}

    public static void main(String[] args) throws IOException
    {

        String registryAddress = InetAddress.getLocalHost().getHostAddress();
        //starting registry thread to add/lookupAndLocalise remote references.
        new Thread(new RMIGlobalRegistry(RMIGlobalRegistry.RMI_GLOBAL_REGISTRY_PORT)).start();

        System.out.println("Global Registry started on host " + registryAddress);

    }
	
	
}
