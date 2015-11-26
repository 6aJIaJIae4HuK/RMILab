package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by BALALAIKA on 11.11.2015.
 */
public class Result implements Serializable {
    private StringProperty nameProperty = new SimpleStringProperty();
    private DoubleProperty timeProperty = new SimpleDoubleProperty();

    public Result(String name, double time) {
        this.nameProperty.setValue(name);
        this.timeProperty.setValue(time);
    }

    public StringProperty getNameProperty() {
        return nameProperty;
    }

    public DoubleProperty getTimeProperty() {
        return timeProperty;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(nameProperty.getValue());
        stream.writeObject(timeProperty.getValue());
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        String name = (String)stream.readObject();
        Double time = (Double)stream.readObject();
        if (nameProperty == null)
            nameProperty = new SimpleStringProperty();
        nameProperty.setValue(name);

        if (timeProperty == null)
            timeProperty = new SimpleDoubleProperty();
        timeProperty.setValue(time);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Result result = (Result)o;
        boolean res = true;
        res = res && (this.nameProperty.getValue().equals(result.getNameProperty().getValue()));
        res = res && (this.timeProperty.getValue().equals(result.getTimeProperty().getValue()));
        return res;
    }
}
