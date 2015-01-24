package data;

import java.io.Serializable;

/**
 * Represents a packet of communciation between the client(s) and the remote server.
 * @author surajd
 *
 */
public class RMIMessage implements Serializable {

	private static final long serialVersionUID = -51371207180888027L;
	
	public static enum RMIMessageType
	{
		LOOKUP , // for a registry lookupAndLocalise
		INVOKE, // for a remote invocation.
		INVOKE_SUCCEEDED, // reply upon a message invocation.
		GLOBAL_REGISTER,
		LOOKUP_REPLY_SUCCEEDED, 
		GLOBAL_REGISTER_SUCCEEDED, 
		LOOKUP_FAILED, GLOBAL_REGISTER_FAILED, 
		INVOKE_FAILED, LOCAL_REGISTER,
        LOCAL_REGISTER_SUCCEEDED, LOCAL_REGISTER_FAILED,
		LIST_REGISTERED_SERVICES, LIST_REGISTERED_SERVICES_REPLY, LIST_REGISTERED_SERVICES_FAILED, BAD_REQUEST;
	}

	// type of the message.
	private RMIMessageType type;
	// string message.
	private String message;
	//any exception that might have occured.
	private RMIException exception;

	/**
	 * @return the type
	 */
	public RMIMessageType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RMIMessageType type) {
		this.type = type;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the exception
	 */
	public RMIException getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(RMIException exception) {
		this.exception = exception;
	}
}
