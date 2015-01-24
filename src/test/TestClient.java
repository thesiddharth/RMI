/**
 * 
 */
package test;

import interfaces.EchoService;
import interfaces.ZipCodeServer;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import manager.CommunicationManager;
import server.RMIGlobalRegistry;
import services.ZipCodeList;
import data.HostInfo;
import data.RMIException;
import data.RemoteObjectReference;

/**
 * @author surajd
 *
 */
public class TestClient {

    private final String help = "1 - You can start a new RMIServer on a different machine, and give us the IP and the interface name (this " +
            "is a dummy name used as a key). We'll start a new remote Echo service on that host and echo something back" +
            "\n" +
            "2 - Run a ZipCodeServer example here. Don't worry about having to bind it, we'll do " +
            "that using a ZipCodeServer remote object spawning method in our remote object. We'll use" +
            " the initial server itself."+
            "\n"+
            "3 - Query the global registry for all registered RORs" +
            "\n" +
            "4 - Quit";
	
	public void testRMIRemoteCalls(final RegistryClient registryClient)
	{
        try
        {
            EchoService ref1 = (EchoService)registryClient.lookupAndLocalise("EchoService");
            // set a prefix for this guy.
            ref1.setPrefix("FirstReference");
            System.out.println("------------------------------------------------------");
            System.out.println("First service looked up and localized, with value: " + ref1.getPrefix());
            System.out.println("------------------------------------------------------");
            pressAKeyToContinue();

            EchoService ref2 = (EchoService)registryClient.lookupAndLocalise("EchoService");
            // set a prefix for this guy.
            ref2.setPrefix("SecondReference");
            // this will print the prefix set just above.
            System.out.println("------------------------------------------------------");
            System.out.println("Second service looked up with same key as the first. Should point " +
                    "to the same remote object. Value set to: " + ref2.getPrefix());
            System.out.println("First service now points to: " + ref1.getPrefix());
            System.out.println("------------------------------------------------------");
            pressAKeyToContinue();

            // remote call to create a new echo service, i.e. returning an instance of remote service.
            EchoService ref3 = (EchoService)ref2.createNewEchoServiceReference().localise();
            // this is a new intance altogether.
            ref3.setPrefix("ThirdReference");
            System.out.println("------------------------------------------------------");
            System.out.println("Using the remote object running on the server to spawn a new " +
                    "remote object and get back the reference. Value set to: " + ref3.getPrefix());
            System.out.println("Initial object value: " + ref1.getPrefix()); // will print "I am the second service"
            System.out.println("------------------------------------------------------");
            pressAKeyToContinue();

            // remote echo call - passing in a remote service as a paramter.
            System.out.println("------------------------------------------------------");
            System.out.println("Passing in the localized SecondReference to a method on the " +
                    "localized ThirdReference to append some values and print. RMI takes care of cases " +
                    "like this as well" );
            System.out.println(ref3.remoteEcho(ref2, "TextFor3", "TextFor1or2"));
            System.out.println("------------------------------------------------------");
            pressAKeyToContinue();

            System.out.println("------------------------------------------------------");
            EchoService ref4 = ref3.createNewEchoService("FourthService", "SuperEchoService2", "localhost");

            // passing multiple remote services as parameters.
            List<String> ret = ref4.getAllPrefixes(new EchoService[]{ref1 , ref2 ,ref3});
            System.out.println("Spawned another new remote object using the ThirdReference and a new key. " +
                    "This time, we localized it on the server itself, and just got back the instantiated " +
                    "stub. " +
                    "Value set to: " + ref4.getPrefix());
            System.out.println("Now, we're passing in all the previous localized references into a " +
                    "method on the new one, which returns their combined values in a list: \n");
            for(String r :ret)
                System.out.print(r + "-->");

            System.out.println("|");
            System.out.println("------------------------------------------------------");
            pressAKeyToContinue();

            System.out.println("Now you can do the following: ");
            System.out.println(help);

            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext())
            {
                try
                {
                    int userInput = scanner.nextInt();
                    switch (userInput)
                    {
                        case 1:
                            System.out.println("Enter new Server IP: ");
                            String serverIP = scanner.next();
                            System.out.println("Enter new dummy interface name/key: ");
                            String key = scanner.next();
                            System.out.println("Enter text to echo: ");
                            String echo = scanner.next();
                            EchoService refNew = (EchoService)ref1.createNewEchoServiceReference(key, serverIP).localise();
                            refNew.setPrefix(echo);
                            System.out.println("Value: " + refNew.getPrefix());
                            break;
                        case 2:
                            // find a remote reference to the ZipCodeServer.
                            System.out.println("Remotely Creating a reference of a ZipCode server using EchoService");
                            RemoteObjectReference zipCodeServerRef = ref4.createNewZipCodeServiceReference();
                            ZipCodeServer server = (ZipCodeServer) zipCodeServerRef.localise();
                            pressAKeyToContinue();
                            // build a list.
                            System.out.println("Adding Pittsburgh (15213) ,  Beverly Hills ( 90210) and Timbuktu (99999) to a zip code list and initialising the server");
                            ZipCodeList zipCodeList = new ZipCodeList();
                            zipCodeList.add("Pittsburgh", "15213");
                            server.initialise(zipCodeList);
                            zipCodeList.add("Beverly Hills", "90210");
                            server.initialise(zipCodeList);
                            zipCodeList.add("Timbuktu", "99999");
                            server.initialise(zipCodeList);

                            System.out.println("Trying to find Pittsburgh on the remote ZipCode Server");
                            pressAKeyToContinue();
                            System.out.println(server.find("Pittsburgh")); // 15213

                            pressAKeyToContinue();
                            System.out.println("Querying the server for all cities and their zipcodes");
                            System.out.println(server.findAll()); // all the cities

                            pressAKeyToContinue();
                            System.out.println("Printing all the zipcodes on the server");

                            server.printAll(); // prints all the cities on the server.
                            break;
                        case 3:
                            System.out.println(CommunicationManager.listReferencesInRegistry(registryClient.getHostInfo()));
                            break;
                        case 4:
                            System.out.println("Quitting...");
                            return;
                        default:
                            System.err.println("Illegal input. Try again:\n" + help);
                    }
                    System.out.println("Enter option:");
                }
                catch (InputMismatchException e)
                {
                    System.err.println("Illegal input. Try again:\n" + help);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (RMIException e) {
            System.err.println(e);
        }
	}

    private void pressAKeyToContinue()
    {
        System.out.println("Hit enter key to continue");
        try {
            System.in.read();
        } catch (IOException e) {
        }
    }
	
	public static void main(String[] args) throws Exception
	{
		String registryIpAddress = args[0];
		
		final HostInfo registryInfo = new HostInfo( registryIpAddress , RMIGlobalRegistry.RMI_GLOBAL_REGISTRY_PORT);
        final RegistryClient registryClient = new RegistryClient(registryInfo);
		
		TestClient client = new TestClient();
		
		client.testRMIRemoteCalls(registryClient);
	}
}
