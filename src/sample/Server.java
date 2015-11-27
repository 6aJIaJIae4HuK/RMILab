package sample;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

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

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    server.start();
                }
            };
            (new Thread(runnable)).start();
            Scanner scanner = new Scanner(System.in);
            String line;
            while (!(line = scanner.nextLine()).equals("exit"));
            server.stopServer();
            registry.unbind("RMIServerInterface");
            UnicastRemoteObject.unexportObject(server, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
