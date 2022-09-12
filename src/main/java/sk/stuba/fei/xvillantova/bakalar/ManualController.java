package sk.stuba.fei.xvillantova.bakalar;


import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class ManualController {

    @FXML
    void backButtonClicked(Event event) throws IOException {
      /*  Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(830);
        stage.setHeight(550);
        stage.setResizable(false);*/
        App.setRoot("primary");
    }

}
