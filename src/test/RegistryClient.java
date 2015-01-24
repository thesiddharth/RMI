package test;

import data.HostInfo;
import data.RMIException;
import data.RemoteObjectReference;
import manager.CommunicationManager;

/**
 * Created with IntelliJ IDEA.
 * User: Sid
 * Date: 10/8/14
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegistryClient {

    private HostInfo hostInfo;

    public RegistryClient(String serverIpAddress , int portNumber)
    {
        hostInfo =  new HostInfo();
        hostInfo.setIpAddress(serverIpAddress);
        hostInfo.setPort(portNumber);
    }

    public RegistryClient(HostInfo info)
    {
        hostInfo =  new HostInfo();
        hostInfo.setIpAddress(info.getIpAddress());
        hostInfo.setPort(info.getPort());
    }

    public Object lookupAndLocalise(String referenceName) throws RMIException
    {
        return CommunicationManager.lookupAndLocalise(hostInfo, referenceName);
    }

    public RemoteObjectReference lookup(String referenceName) throws RMIException
    {
        return CommunicationManager.lookup(hostInfo, referenceName);
    }

    public HostInfo getHostInfo() {
        return hostInfo;
    }
}
