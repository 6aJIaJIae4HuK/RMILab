package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

public class Controller {
    @FXML
    private TableView<Competition> CompetitionTable;

    @FXML
    private PTableColumn<Competition, String> CompetitionNameField;

    @FXML
    private PTableColumn<Competition, Number> DayField;

    @FXML
    private PTableColumn<Competition, Number> MonthField;

    @FXML
    private PTableColumn<Competition, Number> YearField;

    @FXML
    private PTableColumn<Competition, Number> HourField;

    @FXML
    private PTableColumn<Competition, Number> MinuteField;

    @FXML
    private TableView<Result> ResultTable;

    @FXML
    private PTableColumn<Result, String> ParticipantNameField;

    @FXML
    private PTableColumn<Result, Number> TimeField;

    @FXML
    private TextField CompetitionNameTextField;

    @FXML
    private TextField DayTextField;

    @FXML
    private TextField MonthTextField;

    @FXML
    private TextField YearTextField;

    @FXML
    private TextField HourTextField;

    @FXML
    private TextField MinuteTextField;

    @FXML
    private TextField ParticipantNameTextField;

    @FXML
    private TextField TimeTextField;

    @FXML
    private void initialize() {
        CompetitionNameField.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        DayField.setCellValueFactory(cellData -> cellData.getValue().getDayProperty());
        MonthField.setCellValueFactory(cellData -> cellData.getValue().getMonthProperty());
        YearField.setCellValueFactory(cellData -> cellData.getValue().getYearProperty());
        HourField.setCellValueFactory(cellData -> cellData.getValue().getHourProperty());
        MinuteField.setCellValueFactory(cellData -> cellData.getValue().getMinuteProperty());

        ParticipantNameField.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        TimeField.setCellValueFactory(cellData -> cellData.getValue().getTimeProperty());
    }

    @FXML
    private void addCompetition() {
        String name = CompetitionNameTextField.getText();
        try {
            int day = Integer.parseInt(DayTextField.getText());
            int hour = Integer.parseInt(HourTextField.getText());
            int year = Integer.parseInt(YearTextField.getText());
            int minute = Integer.parseInt(MinuteTextField.getText());
            int month = Integer.parseInt(MonthTextField.getText());
            main.getClient().getServer().addCompetition(name, LocalDateTime.of(year, month, day, hour, minute));
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Not an integer", ButtonType.OK);
            alert.showAndWait();
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addResult() {
        String name = ParticipantNameTextField.getText();
        try {
            double time = Double.parseDouble(TimeTextField.getText());
            main.getClient().getServer().addResultToCompetition(CompetitionTable.getSelectionModel().selectedItemProperty().getValue(), name, time);
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Not a double", ButtonType.OK);
            alert.showAndWait();
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Main main = null;

    public void setApplication(Main main) {
        this.main = main;
        CompetitionTable.setItems(main.getCompetitions());

        CompetitionTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Competition>() {
            @Override
            public void changed(ObservableValue<? extends Competition> observable, Competition oldValue, Competition newValue) {
                ResultTable.setItems(newValue.getResults());
            }
        });
    }
}
