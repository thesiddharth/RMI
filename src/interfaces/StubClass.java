/**
 * 
 */
package interfaces;

import data.RemoteObjectReference;

/**
 * Represents the required functionality of a stub implementation. Each stub implementation must be able to
 * remotely call the server on which the service is hosted.
 * @author surajd
 *
 */
public interface StubClass {
	
	/**
	 * Attach a {@link RemoteObjectReference} to this stub class.
	 * @param remoteRef
	 */
	public void setRemoteObjectReference(RemoteObjectReference remoteRef);
	
	/**
	 * Get the {@link RemoteObjectReference} in this stub class.
	 */
	public RemoteObjectReference getRemoteObjectReference();


	
	

}
