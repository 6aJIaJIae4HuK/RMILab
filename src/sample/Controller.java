package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

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
