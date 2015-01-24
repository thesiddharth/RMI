/**
 * 
 */
package manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.HostInfo;
import data.RMIException;
import data.RMIInvocationMessage;
import data.RMIInvocationReply;
import data.RMIMessage;
import data.RMIMessage.RMIMessageType;
import data.RMIRegisterServiceMessage;
import data.RMIRegistryLookupReply;
import data.RMIRegistryLookupRequest;
import data.RemoteObjectReference;

/**
 * Util class that provides communication capability between 2 endpoints.
 * @author surajd
 *
 */
public class CommunicationManager {
	
	public static RMIMessage sendMessage(HostInfo hostInfo , RMIMessage message) throws RMIException
	{
		try {
			Socket socket = getSocketFromHostInfo(hostInfo);

            writeRMIMessage(message, socket);

			RMIMessage reply = readRMIMessage(socket);
			
			socket.close();
			
			return reply;
		} 
		catch (Exception e) {
			throw new RMIException(e);
		}
	}
	
	/**
	 * Registers a service with the registry.
	 * @param registryHost
	 * @param interfaceName
	 * @param implClassName
	 * @return
	 * @throws Exception
	 */
	public static void registerService(HostInfo registryHost , HostInfo dispatcherHostInfo , String interfaceName , String implClassName) throws RMIException
	{
		RMIRegisterServiceMessage message = new RMIRegisterServiceMessage();
		
		message.setHostInfo(dispatcherHostInfo);
		message.setImplClassName(implClassName);
		message.setInterfaceName(interfaceName);
		message.setType(RMIMessageType.GLOBAL_REGISTER);
		
		RMIMessage reply = sendMessage(registryHost, message);
        validateReply(reply);
	}
	
	/**
	 * Method to be used by a client to get a remote reference from the registry, localise it and return
     * the stub object.
	 * @param serverHostInfo
	 * @param serviceName
	 * @throws IOException 
	 * @throws Exception 
	 */
	public static Object lookupAndLocalise(HostInfo serverHostInfo, String serviceName) throws RMIException
	{
		try 
		{
			RemoteObjectReference remoteRef = lookup(serverHostInfo, serviceName);
			return remoteRef.localise();
		} 
		catch (Exception e) 
		{
			throw new RMIException(e);
		}
	}

    /**
     * Method to be used by a client to get a remote reference from the registry.
     * @param globalRegistryHostInfo
     * @throws IOException
     * @throws Exception
     */
    public static String listReferencesInRegistry(HostInfo globalRegistryHostInfo) throws RMIException
    {
        try
        {
            Socket socket = getSocketFromHostInfo(globalRegistryHostInfo);

            RMIMessage lookupMessage = new RMIMessage();
            lookupMessage.setType(RMIMessageType.LIST_REGISTERED_SERVICES);
            writeRMIMessage(lookupMessage, socket);

            RMIMessage lookupReply = (RMIMessage) readRMIMessage(socket);
            validateReply(lookupReply);
            return lookupReply.getMessage();
        }
        catch (Exception e)
        {
            throw new RMIException(e);
        }
    }

    /**
     * Method to be used by a client to get a list of remote references from the registry.
     * @param globalRegistryHostInfo
     * @param serviceName
     * @throws IOException
     * @throws Exception
     */
    public static RemoteObjectReference lookup(HostInfo globalRegistryHostInfo, String serviceName) throws RMIException
    {
        try
        {
            Socket socket = getSocketFromHostInfo(globalRegistryHostInfo);

            RMIRegistryLookupRequest lookupMessage = new RMIRegistryLookupRequest(serviceName);
            lookupMessage.setType(RMIMessageType.LOOKUP);
            writeRMIMessage(lookupMessage, socket);

            RMIRegistryLookupReply lookupReply = (RMIRegistryLookupReply) readRMIMessage(socket);
            validateReply(lookupReply);
            return lookupReply.getReference();
        }
        catch (Exception e)
        {
            throw new RMIException(e);
        }
    }

    public static Object lookupObjectId(HostInfo serverHostInfo , String serviceName) throws RMIException
	{
		try 
		{
			Socket socket = getSocketFromHostInfo(serverHostInfo);	
			
			RMIRegistryLookupRequest lookupMessage = new RMIRegistryLookupRequest(serviceName);
			lookupMessage.setType(RMIMessageType.LOOKUP);
			writeRMIMessage(lookupMessage, socket);
			
			RMIRegistryLookupReply lookupReply = (RMIRegistryLookupReply) readRMIMessage(socket);
			validateReply(lookupReply);
			RemoteObjectReference remoteRef = lookupReply.getReference();
			
			return remoteRef.localise();
		} 
		catch (Exception e) 
		{
			throw new RMIException(e);
		}
	}
	
	/**
	 * Invokes a method remotely and returns the result of the computation to the client.
	 * @param hostInfo
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static RMIInvocationReply invokeRemoteMethod(HostInfo hostInfo , RMIInvocationMessage message) throws RMIException
	{
		try 
		{
			return (RMIInvocationReply) sendMessage(hostInfo, message);
		}
		catch (Exception e) {
			throw new RMIException(e);
		}
	}
	
	private static Socket getSocketFromHostInfo(HostInfo info) throws Exception
	{
		return new Socket(info.getIpAddress() , info.getPort());
	}

	private static void writeRMIMessage(RMIMessage message , Socket socket) throws Exception
	{
		ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
		outputStream.writeObject(message);
	}

	private static RMIMessage readRMIMessage(Socket socket) throws Exception
	{
		ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
		return (RMIMessage)inputStream.readObject();
	}

    private static void validateReply(RMIMessage lookupReply) throws RMIException {
        RMIException returnedException = lookupReply.getException();
        if (returnedException != null)
            throw returnedException;
    }
}
