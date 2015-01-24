package data;

/**
 * Represents a message that a service sends to the registry containing the RemoteObjectReference.
 * @author surajd
 *
 */
public class RMIRegisterServiceMessage extends RMIMessage{

	private static final long serialVersionUID = -3780714283769668016L;
	
	// host where the service is located.
	private HostInfo hostInfo;
	//name of the interface that it implements
	private String interfaceName;
	//private Object instance;
	private String implClassName;

	
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
	 * @return the implClassName
	 */
	public String getImplClassName() {
		return implClassName;
	}
	/**
	 * @param implClassName the implClassName to set
	 */
	public void setImplClassName(String implClassName) {
		this.implClassName = implClassName;
	}

}
