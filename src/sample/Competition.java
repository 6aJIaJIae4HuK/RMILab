package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by BALALAIKA on 11.11.2015.
 */
public class Competition implements Serializable {
    private StringProperty nameProperty = new SimpleStringProperty();

    private ObservableList<Result> results = FXCollections.observableArrayList();

    private IntegerProperty yearProperty = new SimpleIntegerProperty();
    private IntegerProperty dayProperty = new SimpleIntegerProperty();
    private IntegerProperty monthProperty = new SimpleIntegerProperty();

    private IntegerProperty hourProperty = new SimpleIntegerProperty();
    private IntegerProperty minuteProperty = new SimpleIntegerProperty();

    public Competition(String name, int year, int month, int day, int hour, int minute) {
        this.nameProperty.setValue(name);
        this.yearProperty.setValue(year);
        this.dayProperty.setValue(day);
        this.monthProperty.setValue(month);
        this.hourProperty.setValue(hour);
        this.minuteProperty.setValue(minute);
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public IntegerProperty getYearProperty() {
        return yearProperty;
    }

    public IntegerProperty getMonthProperty() {
        return monthProperty;
    }

    public IntegerProperty getDayProperty() {
        return dayProperty;
    }

    public IntegerProperty getHourProperty() {
        return hourProperty;
    }

    public IntegerProperty getMinuteProperty() {
        return minuteProperty;
    }

    public ObservableList<Result> getResults() {
        return results;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(nameProperty.getValue());
        stream.writeObject(LocalDateTime.of(yearProperty.getValue(), monthProperty.getValue(), dayProperty.getValue(), hourProperty.getValue(), minuteProperty.getValue()));
        stream.writeObject(new Integer(getResults().size()));
        for (Iterator<Result> it = results.iterator(); it.hasNext(); ) {
            Result result = it.next();
            stream.writeObject(result);
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        String name = (String)stream.readObject();
        if (nameProperty == null)
            nameProperty = new SimpleStringProperty();
        nameProperty.setValue(name);

        LocalDateTime dateTime = (LocalDateTime)stream.readObject();
        if (yearProperty == null)
            yearProperty = new SimpleIntegerProperty();
        yearProperty.setValue(dateTime.getYear());

        if (monthProperty == null)
            monthProperty = new SimpleIntegerProperty();
        monthProperty.setValue(dateTime.getMonthValue());

        if (dayProperty == null)
            dayProperty = new SimpleIntegerProperty();
        dayProperty.setValue(dateTime.getDayOfMonth());

        if (hourProperty == null)
            hourProperty = new SimpleIntegerProperty();
        hourProperty.setValue(dateTime.getHour());

        if (minuteProperty == null)
            minuteProperty = new SimpleIntegerProperty();
        minuteProperty.setValue(dateTime.getMinute());

        Integer sz = (Integer)stream.readObject();

        if (results == null)
            results = FXCollections.observableArrayList();
        results.clear();

        for (int i = 0; i < sz; i++) {
            Result result = (Result)stream.readObject();
            results.add(result);
        }
    }

    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Competition competition = (Competition)o;
        boolean res = true;
        res = res && (this.yearProperty.getValue().equals(competition.getYearProperty().getValue()));
        res = res && (this.monthProperty.getValue().equals(competition.getMonthProperty().getValue()));
        res = res && (this.dayProperty.getValue().equals(competition.getDayProperty().getValue()));
        res = res && (this.hourProperty.getValue().equals(competition.getHourProperty().getValue()));
        res = res && (this.minuteProperty.getValue().equals(competition.getMinuteProperty().getValue()));

        res = res && (this.results.size() == competition.getResults().size());

        for (int i = 0; i < results.size() && res; i++) {
            res = res && (this.results.get(i).equals(competition.getResults().get(i)));
        }

        return res;
    }
}
