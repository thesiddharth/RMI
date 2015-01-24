package server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server class.
 * @author surajd
 *
 */
public class RMIServer {
	
	public static final int RMI_DISPATCHER_PORT = 4444;
    public static final int RMI_LOCAL_REGISTRY_PORT = 7777;
    private static final ConcurrentHashMap<String, Object> serviceToObjectMap = new ConcurrentHashMap<>();
	
	private static String globalRegistryAddress;
	
	public void RmiServer()
	{
		
	}
	
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("Server starting.");
		
		globalRegistryAddress = args[0];
		
        // starting localRegistryThread
        new Thread(new RMILocalRegistry(globalRegistryAddress, RMIGlobalRegistry.RMI_GLOBAL_REGISTRY_PORT, serviceToObjectMap)).start();
        System.out.println("Local registry service launched");

		// starting dispatcher thread to wait for invocations.
		new Thread(new RMIDispatcher(RMI_DISPATCHER_PORT, serviceToObjectMap)).start();
		System.out.println("Register dispatcher service launched");
	}
}