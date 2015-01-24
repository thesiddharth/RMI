/**
 * 
 */
package data;

import java.io.Serializable;

/**
 * Container class that holds information describing a host.
 * @author surajd
 *
 */
public class HostInfo implements Serializable {
	
	private static final long serialVersionUID = -7478303017142747055L;
	
	private String ipAddress;
	private int port;
	
	
	public HostInfo()
	{
		
	}
	
	public HostInfo(String ipAddress, int port)
	{
		this.ipAddress = ipAddress;
		this.port = port;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HostInfo [ipAddress=" + ipAddress + ", port=" + port + "]";
	}

}
