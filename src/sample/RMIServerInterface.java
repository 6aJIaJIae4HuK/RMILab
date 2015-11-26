package sample;

import org.w3c.dom.Document;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

/**
 * Created by BALALAIKA on 11.11.2015.
 */
public interface RMIServerInterface extends Remote {
    public void registerClient(RMIClientInterface client) throws RemoteException;
    public void addCompetition(String name, LocalDateTime dateTime) throws RemoteException;
    public void addResultToCompetition(Competition competition, String participant, double time) throws RemoteException;
}
