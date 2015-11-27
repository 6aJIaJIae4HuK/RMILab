package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by BALALAIKA on 11.11.2015.
 */
public class RMIServer implements RMIServerInterface {
    private List<Competition> competitions = new LinkedList<>();
    private BlockingQueue<RMIClientInterface> clients = new LinkedBlockingQueue<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private boolean isRunning = false;

    public RMIServer() throws ParserConfigurationException,
                              SAXException,
                              TransformerException,
                              IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("simpleXML.xml"));

        doc.getDocumentElement().normalize();
        NodeList competitionList = doc.getElementsByTagName("Competition");


        for (int i = 0; i < competitionList.getLength(); i++) {
            Element competition = (Element)competitionList.item(i);
            String discipline = competition.getAttribute("Discipline");
            LocalDateTime dateTime = LocalDateTime.parse(competition.getAttribute("DateTime"), formatter);
            Competition newCompetition = new Competition(discipline, dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute());
            NodeList results = competition.getElementsByTagName("Result");
            for (int j = 0; j < results.getLength(); j++) {
                Element result = (Element)results.item(j);
                String name = result.getAttribute("Participant");
                double time = Double.parseDouble(result.getAttribute("Time"));
                newCompetition.getResults().add(new Result(name, time));
            }
            competitions.add(newCompetition);
        }
    }

    public void addCompetition(String name, LocalDateTime dateTime) throws RemoteException {
        Competition newCompetition = new Competition(name, dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute());
        competitions.add(newCompetition);
        for (RMIClientInterface client : clients) {
            client.addCompetition(newCompetition);
        }
    }

    public void addResultToCompetition(Competition competition, String participant, double time) throws RemoteException {
        for (Iterator<Competition> it = competitions.iterator(); it.hasNext();) {
            Competition c = it.next();
            if (c.equals(competition)) {
                c.getResults().add(new Result(participant, time));
                break;
            }
        }
        for (RMIClientInterface client : clients) {
            client.addResultToCompetition(competition, participant, time);
        }
    }

    public void registerClient(RMIClientInterface client) throws RemoteException {
        for (Competition competition : competitions) {
            client.addCompetition(competition);
        }
        clients.add(client);
    }

    private Element addCompetition(Document doc, Element parent, LocalDateTime dateTime, String discipline) {
        Element competition = doc.createElement("Competition");
        competition.setAttribute("DateTime", dateTime.format(formatter));
        competition.setAttribute("Discipline", discipline);
        parent.appendChild(competition);
        return competition;
    }

    private void addResult(Document doc, Element parent, String name, String time) {
        Element result = doc.createElement("Result");
        result.setAttribute("Participant", name);
        result.setAttribute("Time", time);
        parent.appendChild(result);
    }

    public void start() {
        isRunning = true;
        while (isRunning) {
        }
        try {
            saveToXML();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopServer() {
        isRunning = false;
    }

    private void saveToXML() throws ParserConfigurationException,
                                     TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        Element root = doc.createElement("Competitions");
        doc.appendChild(root);

        for (Competition competition : competitions) {
            Element competitionNode = addCompetition(doc, root,
                    LocalDateTime.of(competition.getYearProperty().getValue(),
                                     competition.getMonthProperty().getValue(),
                                     competition.getDayProperty().getValue(),
                                     competition.getHourProperty().getValue(),
                                     competition.getMinuteProperty().getValue()),
                    competition.getNameProperty().getValue());

            for (Result result : competition.getResults()) {
                addResult(doc, competitionNode, result.getNameProperty().getValue(), result.getTimeProperty().getValue().toString());
            }
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("simpleXML.xml"));

        t.transform(source, result);
    }
}
