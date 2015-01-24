/**
 * 
 */
package data;

/**
 * Encapsulates the contents of a message for a remote method invocation.
 * @author surajd
 *
 */
public class RMIInvocationMessage extends RMIMessage {

	private static final long serialVersionUID = 5605903096357934337L;
	
	// the name of the interface for which we are doing a remote invoke.
	private String interfaceName;
	// name of the invoking method
	private String methodName;
	// args to the method
	private Object[] arguments;
	//argument types
	private Class<?>[]  argumentTypes;
	//name of the implementing class
	private String implClassName;
	// object key id , returned by lookupAndLocalise
	private String objectKeyId;
	
	/**
	 * Parameterless constructor for serialization.
	 */
	public RMIInvocationMessage()
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
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the arguments
	 */
	public Object[] getArguments() {
		return arguments;
	}

	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return the argumentTypes
	 */
	public Class<?>[] getArgumentTypes() {
		return argumentTypes;
	}

	/**
	 * @param argumentTypes the argumentTypes to set
	 */
	public void setArgumentTypes(Class<?>[] argumentTypes) {
		this.argumentTypes = argumentTypes;
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

	

}
