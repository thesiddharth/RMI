package services;

import interfaces.EchoService;
import interfaces.StubClass;

import java.util.List;

import manager.CommunicationManager;
import data.HostInfo;
import data.RMIException;
import data.RMIInvocationMessage;
import data.RMIInvocationReply;
import data.RMIMessage.RMIMessageType;
import data.RemoteObjectReference;

public class EchoServiceImpl_stub implements EchoService, StubClass{
	
	private static final long serialVersionUID = 7556433911244031981L;
	private RemoteObjectReference remoteRef;
	
	public EchoServiceImpl_stub() throws RMIException
	{
		
	}
	

	@Override
	public String echo(String message) throws RMIException 
	{
		RMIInvocationReply reply = invokeRemoteMethod(new Object[]{message}, "find");
		return (String)reply.getResult();
	}

	@Override
	public String remoteEcho(EchoService echoService , String echo1 , String echo2) throws RMIException
	{
		RMIInvocationReply reply = invokeRemoteMethod(new Object[]{echoService, echo1, echo2}, "remoteEcho",
                new Class[]{EchoService.class, String.class, String.class});
		return (String)reply.getResult();
			
	}

	@Override
	public void setRemoteObjectReference(RemoteObjectReference remoteRef) {
		this.remoteRef = remoteRef;
		
	}

	@Override
	public RemoteObjectReference getRemoteObjectReference() {
		return remoteRef;
	}

	@Override
	public void setPrefix(String prefix) throws RMIException {
		invokeRemoteMethod(new Object[]{prefix}, "setPrefix");
		
	}

	@Override
	public EchoService createNewEchoService(String newServicePrefix) throws RMIException
	{
		RMIInvocationReply reply = invokeRemoteMethod(new Object[]{newServicePrefix}, "createNewEchoService");
		return (EchoService)reply.getResult();
	}
	
	@Override
	public String getPrefix() throws RMIException 
	{
		RMIInvocationReply reply = invokeRemoteMethod(new Object[]{}, "getPrefix");
		return (String)reply.getResult();
	}
	

	@Override
	public List<String> getAllPrefixes(EchoService[] services) throws RMIException
	{
		RMIInvocationReply reply = invokeRemoteMethod(new Object[]{services}, "getAllPrefixes",
                new Class[]{EchoService[].class});
		return (List<String>)reply.getResult();
	}

    @Override
    public RemoteObjectReference createNewEchoServiceReference() throws RMIException {
    	RMIInvocationReply reply = invokeRemoteMethod(new Object[]{}, "createNewEchoServiceReference");
		return (RemoteObjectReference)reply.getResult();
    }

    @Override
    public RemoteObjectReference createNewZipCodeServiceReference() throws RMIException {
    	RMIInvocationReply reply = invokeRemoteMethod(new Object[]{}, "createNewZipCodeServiceReference");
		return (RemoteObjectReference)reply.getResult();
    }

    @Override
    public RemoteObjectReference createNewEchoServiceReference(String interfaceName, String hostName) throws RMIException {
        RMIInvocationReply reply = invokeRemoteMethod(new Object[]{interfaceName, hostName}, "createNewEchoServiceReference");
        return (RemoteObjectReference)reply.getResult();
    }

    public EchoService createNewEchoService(String newServicePrefix, String interfaceName, String hostName) throws RMIException {
        RMIInvocationReply reply = invokeRemoteMethod(new Object[]{newServicePrefix, interfaceName, hostName}, "createNewEchoService");
        return (EchoService)reply.getResult();
    }

    private RMIInvocationMessage createInvokeMessage(Object[] arguments,String methodName) {
		// construct parameter types.
		Class[] argumentTypes = new Class[arguments.length];

		for (int i = 0; i < arguments.length; i++) {
			argumentTypes[i] = arguments[i].getClass();
		}

		return createInvokeMessage(arguments, methodName, argumentTypes);
	}

	private RMIInvocationMessage createInvokeMessage(Object[] arguments,
			String methodName, Class[] argumentTypes) {
		RMIInvocationMessage invocationMessage = new RMIInvocationMessage();

		invocationMessage.setType(RMIMessageType.INVOKE);
		invocationMessage.setArgumentTypes(argumentTypes);
		invocationMessage.setArguments(arguments);
		invocationMessage.setInterfaceName(remoteRef.getInterfaceName());
		invocationMessage.setMethodName(methodName);
		invocationMessage
				.setImplClassName(remoteRef.getImplementingClassName());
		invocationMessage.setObjectKeyId(remoteRef.getObjectKeyId());

		return invocationMessage;

	}
	
	private RMIInvocationReply invokeRemoteMethod(Object[] params , String methodName ) throws RMIException
	{
		return invokeRemoteMethod(params, methodName , null);
	}
	
	private RMIInvocationReply invokeRemoteMethod(Object[] params , String methodName , Class[] argumentTypes) throws RMIException
	{
		RMIInvocationReply result;
		try {
			HostInfo hostInfo = remoteRef.getHostInfo();

			RMIInvocationMessage invocationMessage = null;
			if(argumentTypes != null)
			{
				invocationMessage = createInvokeMessage( params , methodName , argumentTypes);						
			}
			else
			{
				 invocationMessage = createInvokeMessage(params, methodName);
			}
			 result = CommunicationManager
					.invokeRemoteMethod(hostInfo, invocationMessage);
			
			if(result.getException() != null)
				throw new RMIException(result.getMessage() , result.getException());
		} catch (Exception e) {
			throw new RMIException(e);
		}
		
		return result;
	}


		

}
