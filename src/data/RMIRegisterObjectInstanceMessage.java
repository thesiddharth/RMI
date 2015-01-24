/**
 * 
 */
package data;

/**
 * 
 * @author surajd
 *
 */
public class RMIRegisterObjectInstanceMessage extends RMIMessage {

	private static final long serialVersionUID = 7318025275105311331L;
	private String interfaceName;
	private String implClassName;
	
	public RMIRegisterObjectInstanceMessage()
	{
		
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
