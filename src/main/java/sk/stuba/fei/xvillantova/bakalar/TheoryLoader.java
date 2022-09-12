package sk.stuba.fei.xvillantova.bakalar;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.net.URL;

public class TheoryLoader {
     Pane view;

    public  Pane getPage(String fileName){

        try{
            view = FXMLLoader.load(App.class.getResource(fileName + ".fxml"));
        } catch (Exception e){
            System.out.println("No page " + fileName + " please check FXMLLoader.");
        }

        return  view;

    }
}
