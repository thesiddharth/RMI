/**
 * 
 */
package interfaces;

import java.util.List;

import data.RMIException;
import data.RemoteObjectReference;

/**
 * Sample service to demonstrate functionality of 640RMI facility.
 * @author surajd
 *
 */
public interface EchoService extends Remote640 {
	
	/**
	 * Sets a "prefix" string used for identifying service instances.
	 * @param prefix
	 * @throws RMIException
	 */
	void setPrefix(String prefix) throws RMIException;
	
	/**
	 * Sets a "prefix" string used for identifying service instances.
	 * @return
	 * @throws RMIException
	 */
	String getPrefix() throws RMIException;
	
	/**
	 * Call which takes a String input to the echo service.
	 * @param toEcho
	 * @return
	 * @throws RMIException
	 */
	String echo(String toEcho)throws RMIException;
	
	/**
	 * Call which takes a remote service as a parameter.
	 * @param other
	 * @param thisEcho
	 * @param thatEcho
	 * @return
	 * @throws RMIException
	 */
	String remoteEcho(EchoService other , String thisEcho , String thatEcho) throws RMIException;
	/**
	 * Returns an instantiated (on the server itself) stub of a SuperEcho service, which is a remote object.
	 * @param newServicePrefix
	 * @return
	 * @throws RMIException
	 */
	EchoService createNewEchoService(String newServicePrefix) throws RMIException;
	/**
	 * Returns a list of all prefixes for the input array of echo services.
	 * @param services
	 * @return
	 * @throws RMIException
	 */
	List<String> getAllPrefixes(EchoService[] services) throws RMIException;
    /**
     * Returns an ROR of a SuperEcho service
     * @return
     * @throws RMIException
     */
    public RemoteObjectReference createNewEchoServiceReference() throws RMIException;
   
    /**
     * Returns an ROR of a {@link ZipCodeServer} service.
     * @return
     * @throws RMIException
     */
    public RemoteObjectReference createNewZipCodeServiceReference() throws RMIException;

    /**
     * Returns an ROR of a {@link EchoService} service with interfaceName and hostName specified.
     * @return
     * @throws RMIException
     */
    public RemoteObjectReference createNewEchoServiceReference(String interfaceName, String hostName) throws RMIException;

    /**
     * Returns an instantiated (on the server itself) stub of a remote {@link EchoService} service with interfaceName and hostName specified.
     * @return
     * @throws RMIException
     */
    public EchoService createNewEchoService(String newServicePrefix, String interfaceName, String hostName) throws RMIException;

}
