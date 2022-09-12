package sk.stuba.fei.xvillantova.bakalar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        InputStream iconStream = getClass().getResourceAsStream("images/lambda_filled_50px.png");
        Image image = new Image(iconStream);
        primaryStage.getIcons().add(image);

        scene = new Scene(loadFXML("primary"), 800, 500);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static void setRoot(Parent parent) {
        scene.setRoot(parent);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Closing the app.\n Bye... :) Dovidenia... :) Viszlat... :) ");
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}