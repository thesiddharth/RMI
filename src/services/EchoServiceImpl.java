/**
 * 
 */
package services;

import interfaces.EchoService;

import java.util.ArrayList;
import java.util.List;

import manager.CommunicationManager;
import server.RMIServer;
import data.HostInfo;
import data.RMIException;
import data.RMIMessage;
import data.RMIRegisterInstanceReplyMessage;
import data.RMIRegisterObjectInstanceMessage;
import data.RemoteObjectReference;

/**
 * Implementation for the stateful echo service interface.
 * @author surajd
 *
 */
public class EchoServiceImpl implements EchoService {

	private static final long serialVersionUID = 629638054119531071L;
	
	private String prefix;
	
	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
	

	@Override
	public String echo(String toEcho) 
	{
		String ret = prefix + ": " + toEcho;
		return ret;
	}

	@Override
	public String remoteEcho(EchoService other, String thisEcho,String thatEcho) throws RMIException
	{
		String ret = echo(thisEcho) + "\t\t" +  other.echo(thatEcho) ;
		return ret;
	}

    @Override
    public EchoService createNewEchoService(String newServicePrefix) throws RMIException
    {
        return createNewEchoService(newServicePrefix, "SuperEchoService", "localhost");
    }

	@Override
	public EchoService createNewEchoService(String newServicePrefix, String interfaceName, String hostName) throws RMIException
	{
        EchoService returnValue = null;
        try
        {
            RemoteObjectReference reference = createNewEchoServiceReference(interfaceName, hostName);
            returnValue = (EchoService) reference.localise();
            returnValue.setPrefix(newServicePrefix);
        }
        catch (Exception e)
        {
            throw new RMIException(e);
        }
        return returnValue;
	}

    @Override
    public RemoteObjectReference createNewEchoServiceReference() throws RMIException
    {
        return createNewEchoServiceReference("SuperEchoService","localhost");
    }

    @Override
    public RemoteObjectReference createNewEchoServiceReference(String interfaceName, String hostName) throws RMIException
    {
    	
        RemoteObjectReference returnValue = null;
        try
        {
        	// we are trying to return a ROR to an EchoService.
            RMIRegisterObjectInstanceMessage registerMessage = new RMIRegisterObjectInstanceMessage();
            registerMessage.setImplClassName(this.getClass().getSimpleName());
            // set the key to something else.
            registerMessage.setInterfaceName(interfaceName);
            registerMessage.setType(RMIMessage.RMIMessageType.LOCAL_REGISTER);
            
            // wait for registry reply.
            RMIRegisterInstanceReplyMessage reply = (RMIRegisterInstanceReplyMessage) CommunicationManager.sendMessage(new HostInfo(hostName , RMIServer.RMI_LOCAL_REGISTRY_PORT), registerMessage);
           
            // check for exception that may have occured.
            if(reply.getException()!=null)
            {
                throw reply.getException();
            }
            System.out.println(String.format("Registered a new service with key %s" , interfaceName));
            returnValue =  reply.getReturnValue();
        }
        catch (Exception e)
        {
        	throw new RMIException(e);
        }
        return returnValue;
    }


    @Override
	public String getPrefix() throws RMIException {
		return prefix;
	}
	
	@Override
	public List<String> getAllPrefixes(EchoService[] services) throws RMIException {
		ArrayList<String> allPrefixes = new ArrayList(services.length);
		for(EchoService service : services)
		{
			allPrefixes.add(service.getPrefix());
		}
		return allPrefixes;
	}

	
	@Override
    public RemoteObjectReference createNewZipCodeServiceReference() throws RMIException
    {
    	
        RemoteObjectReference returnValue = null;
        try
        {
        	// we are trying to return a ROR to an EchoService.
            RMIRegisterObjectInstanceMessage registerMessage = new RMIRegisterObjectInstanceMessage();
            registerMessage.setImplClassName(ZipCodeServerImpl.class.getSimpleName());
            // set the key to something else.
            registerMessage.setInterfaceName("ZipCodeServer");
            registerMessage.setType(RMIMessage.RMIMessageType.LOCAL_REGISTER);
            
            // wait for the local registry to reply.
            RMIRegisterInstanceReplyMessage reply = (RMIRegisterInstanceReplyMessage) 
            		CommunicationManager.sendMessage(new HostInfo("localhost" , RMIServer.RMI_LOCAL_REGISTRY_PORT), registerMessage);
           
            // check for exception that may have occurred.
            if(reply.getException()!=null)
            {
                throw reply.getException();
            }
            System.out.println(String.format("Registered a new service with key %s" , registerMessage.getInterfaceName()));
            returnValue =  reply.getReturnValue();
        }
        catch (Exception e)
        {
        	throw new RMIException(e);
        }
        return returnValue;
    }
}
