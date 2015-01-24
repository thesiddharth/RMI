/**
 * 
 */
package data;

import interfaces.StubClass;

import java.io.Serializable;
import java.lang.reflect.Constructor;

/**
 * Represents the reference to a java object that is located "remotely" , on another JVM.
 * @author surajd
 *
 */
public class RemoteObjectReference implements Serializable {
	
	private static final long serialVersionUID = -4763170444048231704L;
	// the host on which the remote object is located.
	private HostInfo hostInfo;
	//name of the interface.
	private String interfaceName;
	//name of the class implementing this interface.
	private String implementingClassName;
	//object key id, to reference a particular instance.
	private String objectKeyId;
	
	//param less constructor for serialization
	public RemoteObjectReference()
	{
		
	}
	
	// constructor from fields.
	public RemoteObjectReference(HostInfo hostInfo, String interfaceName,
			String implementingClassName) {
		this.hostInfo = hostInfo;
		this.interfaceName = interfaceName;
		this.implementingClassName = implementingClassName;
	}
	
	/**
	 * Returns a local implementation of the service for the client to work with. It registers this
	 * instance with the remote server, and returns the local impl.
	 * @return
	 * @throws RMIException
	 */
    public Object localise() throws RMIException
	{
		StubClass stubObject = null;
		try 
		{
			String stubClassName = "services." + implementingClassName + "_stub";
			Class<?> stubClass = Class.forName(stubClassName);
			
			Constructor<?> stubConstructor = stubClass.getConstructor();
			 	
			stubObject = (StubClass)stubConstructor.newInstance();
			stubObject.setRemoteObjectReference(this);
			
		} catch (Exception e) {
			throw new RMIException(e);
		} 
		
		return stubObject;
	}



	/**
	 * @return the hostInfo
	 */
	public HostInfo getHostInfo() {
		return hostInfo;
	}

	/**
	 * @param hostInfo the hostInfo to set
	 */
	public void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}

	/**
	 * @return the interfaceImplName
	 */
	public String getImplementingClassName() {
		return implementingClassName;
	}

	/**
	 * @param interfaceImplName the interfaceImplName to set
	 */
	public void setImplementingClassName(String interfaceImplName) {
		this.implementingClassName = interfaceImplName;
	}

	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}

	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * @return the objectKeyId
	 */
	public String getObjectKeyId() {
		return objectKeyId;
	}

	/**
	 * @param objectKeyId the objectKeyId to set
	 */
	public void setObjectKeyId(String objectKeyId) {
		this.objectKeyId = objectKeyId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RemoteObjectReference [hostInfo=" + hostInfo
				+ ", interfaceName=" + interfaceName
				+ ", implementingClassName=" + implementingClassName
				+ ", objectKeyId=" + objectKeyId + "]";
	}
	
	


}
