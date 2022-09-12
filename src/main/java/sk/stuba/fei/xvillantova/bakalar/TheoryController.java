package sk.stuba.fei.xvillantova.bakalar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class TheoryController {

    @FXML
    private BorderPane mainPane;


    @FXML
    void backButtonClicked(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(818);
        stage.setHeight(540);
        stage.setResizable(false);
        App.setRoot("primary");
    }

    @FXML
    void handleButtonTheory1(ActionEvent event) {
        TheoryLoader object = new TheoryLoader();
        Pane view = object.getPage("theoryPage1");
        mainPane.setCenter(view);
    }

    @FXML
    void handleButtonTheory2(ActionEvent event) {
        TheoryLoader object = new TheoryLoader();
        Pane view = object.getPage("theoryPage2");
        mainPane.setCenter(view);
    }

    @FXML
    void handleButtonTheory3(ActionEvent event) {
        TheoryLoader object = new TheoryLoader();
        Pane view = object.getPage("theoryPage3");
        mainPane.setCenter(view);
    }

    @FXML
    void handleButtonTheory4(ActionEvent event) {
        TheoryLoader object = new TheoryLoader();
        Pane view = object.getPage("theoryPage4");
        mainPane.setCenter(view);
    }

    @FXML
    void handleButtonTheory5(ActionEvent event) {
        TheoryLoader object = new TheoryLoader();
        Pane view = object.getPage("theoryPage5");
        mainPane.setCenter(view);
    }

    @FXML
    void handleButtonTheory6(ActionEvent event) {
        TheoryLoader object = new TheoryLoader();
        Pane view = object.getPage("theoryPage6");
        mainPane.setCenter(view);
    }

    @FXML
    void handleButtonTheory7(ActionEvent event) {
        TheoryLoader object = new TheoryLoader();
        Pane view = object.getPage("theoryPage7");
        mainPane.setCenter(view);
    }


}
