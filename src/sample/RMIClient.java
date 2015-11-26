package sample;

import javafx.application.Platform;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by BALALAIKA on 11.11.2015.
 */
public class RMIClient implements RMIClientInterface {
    private List<Competition> competitions = new LinkedList<>();

    private RMIServerInterface server = null;
    private RMIClientInterface stub = null;

    private Main application = null;

    public RMIClient(Main application) {
        this.application = application;
    }

    public void addCompetition(Competition competition) throws RemoteException {
        competitions.add(competition);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                application.getCompetitions().add(competition);
            }
        });
    }

    public void addCompetition(String name, LocalDateTime dateTime) throws RemoteException {
        Competition newCompetition = new Competition(name, dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute());
        competitions.add(newCompetition);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                application.getCompetitions().add(newCompetition);
            }
        });
    }

    public void addResultToCompetition(Competition competition, String participant, double time) throws RemoteException {
        for (Iterator<Competition> it = competitions.iterator(); it.hasNext();) {
            Competition c = it.next();
            if (c.equals(competition)) {
                c.getResults().add(new Result(participant, time));
                break;
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Iterator<Competition> it = application.getCompetitions().iterator(); it.hasNext(); ) {
                    Competition c = it.next();
                    if (c.equals(competition)) {
                        c.getResults().add(new Result(participant, time));
                    }
                }
            }
        });
    }

    public void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry(null, 12345);
            server = (RMIServerInterface)registry.lookup("RMIServerInterface");
            stub = (RMIClientInterface) UnicastRemoteObject.exportObject(this, 0);
            server.registerClient(stub);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
