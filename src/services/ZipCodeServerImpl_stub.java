/**
 * 
 */
package services;

import interfaces.StubClass;
import interfaces.ZipCodeServer;
import manager.CommunicationManager;
import data.HostInfo;
import data.RMIException;
import data.RMIInvocationMessage;
import data.RMIInvocationReply;
import data.RMIMessage.RMIMessageType;
import data.RemoteObjectReference;

/**
 * @author surajd
 *
 */
public class ZipCodeServerImpl_stub implements ZipCodeServer, StubClass {

	private RemoteObjectReference remoteRef;

	@Override
	public void initialise(ZipCodeList newlist) throws RMIException {
		invokeRemoteMethod(new Object[]{newlist}, "initialise");
	}

	@Override
	public String find(String city) throws RMIException {
		RMIInvocationReply reply = invokeRemoteMethod(new Object[]{city}, "find");
		return (String)reply.getResult();
	}

	@Override
	public ZipCodeList findAll() throws RMIException {
		RMIInvocationReply reply = invokeRemoteMethod(new Object[]{}, "findAll");
		return (ZipCodeList)reply.getResult();
	}

	@Override
	public void printAll() throws RMIException{
		invokeRemoteMethod(new Object[]{}, "printAll");
	}

	@Override
	public void setRemoteObjectReference(RemoteObjectReference remoteRef) {
		this.remoteRef = remoteRef;
	}

	@Override
	public RemoteObjectReference getRemoteObjectReference() {
		return remoteRef;
	}

	private RMIInvocationMessage createInvokeMessage(Object[] arguments,
			String methodName) {
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
				invocationMessage = createInvokeMessage(new Object[] { params }, methodName , argumentTypes);						
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
