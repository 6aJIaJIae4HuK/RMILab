package sample;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by BALALAIKA on 15.11.2015.
 */
public class Server {
    public static void main(String[] argv) {
        try {
            RMIServer server = new RMIServer();
            RMIServerInterface stub = (RMIServerInterface) UnicastRemoteObject.exportObject(server, 0);

            Registry registry = LocateRegistry.createRegistry(12345);
            registry.bind("RMIServerInterface", stub);

            server.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
