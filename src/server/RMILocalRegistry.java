package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import manager.CommunicationManager;
import data.HostInfo;
import data.RMIException;
import data.RMIMessage;
import data.RMIRegisterInstanceReplyMessage;
import data.RMIRegisterObjectInstanceMessage;

/**
 * Class that represents a local registry. This serves as a lookup into the global registry. This maintains a map
 * of service to object instance. When we try to register a service with the global registry, we add an instance of that
 * service in the local registry's map, and remote reference in the global registry.
 * Created with IntelliJ IDEA.
 * User: Sid
 * Date: 10/9/14
 * Time: 12:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class RMILocalRegistry implements Runnable
{
    private ConcurrentHashMap<String,Object> serviceToObjectMap;
    private HostInfo globalRegistryInfo;
    private HostInfo dispatcherHostInfo;
    private final String helpMessage = "-------------------------------------------------------------------------\n" +
            "Register New Instances as follows: register <Remote Interface Name> <Implementing Class Name>\n" +
            "Print list of services running on this server with: listServices\n" +
            "-------------------------------------------------------------------------";

    public RMILocalRegistry(String globalRegistryIP, int globalRegistryPort, ConcurrentHashMap serviceToObjectMap ) throws IOException {
        this.serviceToObjectMap = serviceToObjectMap;
        this.globalRegistryInfo = new HostInfo();
        this.globalRegistryInfo.setIpAddress(globalRegistryIP);
        this.globalRegistryInfo.setPort(globalRegistryPort);
        this.dispatcherHostInfo = new HostInfo(InetAddress.getLocalHost().getHostAddress(), RMIServer.RMI_DISPATCHER_PORT);
    }

    public RMILocalRegistry(HostInfo globalRegistryInfo, ConcurrentHashMap serviceToObjectMap) throws IOException {
        this.serviceToObjectMap = serviceToObjectMap;
        this.globalRegistryInfo = new HostInfo();
        this.globalRegistryInfo.setIpAddress(globalRegistryInfo.getIpAddress());
        this.globalRegistryInfo.setPort(globalRegistryInfo.getPort());
        this.dispatcherHostInfo = new HostInfo(InetAddress.getLocalHost().getHostAddress(), RMIServer.RMI_DISPATCHER_PORT);
    }

    @Override
    public void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pollCommandLineForNewRegistrations();
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                listenAtSocketForNewRegistrations();
            }
        }).start();
    }

    private void pollCommandLineForNewRegistrations()
    {
        System.out.println(helpMessage);
        final Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext())
        {
            try
            {
                String userInput = scanner.nextLine();
                String[] args= userInput.split("\\s");
                if(args.length==0)
                {
                    System.err.println("Invalid command");
                    System.err.println(helpMessage);
                }
                else if(args[0].equalsIgnoreCase("help"))
                {
                    System.out.println(helpMessage);
                }
                else if (args[0].equalsIgnoreCase("listServices"))
                {
                    for (Map.Entry entry: serviceToObjectMap.entrySet())
                    {
                        System.out.println(entry.getKey());
                    }
                }
                else if(args[0].equals("register"))
                {
                    if(args.length!=3)
                    {
                        System.err.println("Invalid number of parameters for register");
                        System.err.println(helpMessage);
                        continue;
                    }
                    setUpRemoteObject(args[1], args[2]);
                }
                else
                {
                    System.err.println("Invalid command");
                    System.err.println(helpMessage);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    private void listenAtSocketForNewRegistrations()
    {
        try
        {
            final ServerSocket serverSocket = new ServerSocket(RMIServer.RMI_LOCAL_REGISTRY_PORT);

            while(true)
            {
                Socket socket = serverSocket.accept();
                try
                {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                    RMIMessage message = (RMIMessage)objectInputStream.readObject();

                    if(message.getType().equals(RMIMessage.RMIMessageType.LOCAL_REGISTER))
                    {
                        RMIRegisterObjectInstanceMessage registerMessage = (RMIRegisterObjectInstanceMessage) message;
                        RMIRegisterInstanceReplyMessage reply = new RMIRegisterInstanceReplyMessage();
                        try
                        {
                            setUpRemoteObjectSuppressCatches(registerMessage.getInterfaceName(), registerMessage.getImplClassName());
                            reply.setType(RMIMessage.RMIMessageType.LOCAL_REGISTER_SUCCEEDED);
                            reply.setReturnValue(CommunicationManager.lookup(globalRegistryInfo,registerMessage.getInterfaceName()));
                        }
                        catch(ClassNotFoundException e)
                        {
                            reply.setType(RMIMessage.RMIMessageType.LOCAL_REGISTER_FAILED);
                            reply.setException(new RMIException(new Exception("Class Not Found: " + registerMessage.getImplClassName())));
                        }
                        catch(IllegalAccessException | InstantiationException e)
                        {
                            reply.setType(RMIMessage.RMIMessageType.LOCAL_REGISTER_FAILED);
                            reply.setException(new RMIException(new Exception("Can't instantiate: " + registerMessage.getImplClassName())));
                        }
                        catch (RMIException e)
                        {

                            reply.setType(RMIMessage.RMIMessageType.LOCAL_REGISTER_FAILED);
                            reply.setException(new RMIException(e));
                        }
                        finally
                        {
                            objectOutputStream.writeObject(reply);
                        }
                    }
                    else
                    {
                        RMIRegisterInstanceReplyMessage reply = new RMIRegisterInstanceReplyMessage();
                        reply.setType(RMIMessage.RMIMessageType.LOCAL_REGISTER_FAILED);
                        reply.setMessage("Invalid Message.");
                        reply.setException(new RMIException(new Exception("Invalid Message Type: " + message.getType())));
                        objectOutputStream.writeObject(reply);
                    }

                    objectInputStream.close();
                    objectOutputStream.close();
                }
                catch (Exception e)
                {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    RMIRegisterInstanceReplyMessage reply = new RMIRegisterInstanceReplyMessage();
                    reply.setType(RMIMessage.RMIMessageType.LOCAL_REGISTER_FAILED);
                    reply.setMessage("Invalid Message.");
                    reply.setException(new RMIException(new Exception("Invalid Message")));
                    objectOutputStream.writeObject(reply);
                    objectOutputStream.close();
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Adds an instance of interfaceName with implementation implClassName to the local registry's lookup.
     * @param interfaceName
     * @param implClassName
     * @throws UnknownHostException
     */
    private void setUpRemoteObject(String interfaceName, String implClassName) throws UnknownHostException
    {
        try
        {
            Class<?> classToInstantiate = Class.forName("services." + implClassName);
            Object remoteObject = classToInstantiate.newInstance();
            serviceToObjectMap.put(interfaceName, remoteObject);
            
            CommunicationManager.registerService(globalRegistryInfo, dispatcherHostInfo, interfaceName, remoteObject.getClass().getSimpleName());
            System.out.println("Registered service!");
        }
        catch(ClassNotFoundException | NoClassDefFoundError e)
        {
            System.err.println("Class Not Found: " + implClassName );
        }
        catch(IllegalAccessException | InstantiationException e)
        {
            System.err.println("Can't instantiate " + implClassName);
        }
        catch (RMIException e)
        {
            System.err.println(e.toString());
        }
    }
    
    /**
     * Helps while parsing command line args.
     * @param interfaceName
     * @param implClassName
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws RMIException
     */
    private void setUpRemoteObjectSuppressCatches(String interfaceName, String implClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, RMIException
    {
            Class<?> classToInstantiate = Class.forName("services." + implClassName);
            Object remoteObject = classToInstantiate.newInstance();
            serviceToObjectMap.put(interfaceName, remoteObject);
            CommunicationManager.registerService(globalRegistryInfo, dispatcherHostInfo, interfaceName, remoteObject.getClass().getSimpleName());
            System.out.println("Registered service!");
    }
}
