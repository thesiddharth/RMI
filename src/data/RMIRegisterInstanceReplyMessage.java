package data;

/**
 * Contains the reply from the registry containing the ROR.
 * @author surajd
 *
 */
public class RMIRegisterInstanceReplyMessage extends RMIMessage {

	private static final long serialVersionUID = -7383690661884013024L;

	private RemoteObjectReference returnValue;

	// param less constructor for serialization.
	public RMIRegisterInstanceReplyMessage()
	{

	}
	// constructor from fields.
	public RMIRegisterInstanceReplyMessage(RemoteObjectReference returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * @return the reference
	 */
	public RemoteObjectReference getReturnValue() {
		return returnValue;
	}

	/**
	 * @param returnValue the reference to set
	 */
	public void setReturnValue(RemoteObjectReference returnValue) {
		this.returnValue = returnValue;
	}
	
	
	
}
