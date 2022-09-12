package sk.stuba.fei.xvillantova.bakalar;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private void clickCalculation() throws IOException{
        App.setRoot("calculationSetup");
    }

    @FXML
    private void clickTheory(Event event) throws IOException{
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(978);
        stage.setHeight(640);
      //  stage.setResizable(true);
        App.setRoot("theory");
    }

    @FXML
    void clickInfo() throws IOException{
        App.setRoot("info");
    }

    @FXML
    void clickManual() throws IOException {
        App.setRoot("manual");
    }

}
