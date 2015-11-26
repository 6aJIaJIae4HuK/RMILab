package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main extends Application {

    RMIClient rmiClient = null;

    private Stage primaryStage = null;
    private GridPane rootLayout = null;

    private ObservableList<Competition> competitions = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("fwegsr");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(Main.class.getResource("sample.fxml"));
            rootLayout = (GridPane)fxmlLoader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            Controller controller = fxmlLoader.getController();
            controller.setApplication(this);

            rmiClient = new RMIClient(this);
            rmiClient.connectToServer();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Competition> getCompetitions() {
        return competitions;
    }

    public RMIClient getClient() {
        return rmiClient;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
