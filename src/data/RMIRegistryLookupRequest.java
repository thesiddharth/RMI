/**
 * 
 */
package data;

/**
 * Contains the information needed to lookupAndLocalise a service.
 * @author surajd
 *
 */
public class RMIRegistryLookupRequest extends RMIMessage {

	private static final long serialVersionUID = 8152523332506534957L;
	
	// remote interface we are trying to lookup.
	private String interfaceName;
	
	
	// for serialization.
	public RMIRegistryLookupRequest()
	{
		
	}
	// constructor from fields.
	public RMIRegistryLookupRequest(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}
	/**
	 * @return the serviceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}
	/**
	 * @param interfaceName the serviceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	
	

}
