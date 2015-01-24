package data;

/**
 * Contains the reply from the registry containing the ROR.
 * @author surajd
 *
 */
public class RMIRegistryLookupReply extends RMIMessage {

	private static final long serialVersionUID = -7383690661884013024L;
	
	private RemoteObjectReference reference;
	
	// param less constructor for serialization.
	public RMIRegistryLookupReply()
	{
		
	}
	// constructor from fields.
	public RMIRegistryLookupReply(RemoteObjectReference reference) {
		this.reference = reference;
	}

	/**
	 * @return the reference
	 */
	public RemoteObjectReference getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(RemoteObjectReference reference) {
		this.reference = reference;
	}
	
	
	
}
