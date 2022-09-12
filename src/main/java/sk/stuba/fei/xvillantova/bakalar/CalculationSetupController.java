package sk.stuba.fei.xvillantova.bakalar;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.math3.exception.MathParseException;
import sk.stuba.fei.xvillantova.bakalar.Exceptions.MatrixContainsFractionsException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CalculationSetupController implements Initializable {
    @FXML
    private ComboBox<String> setsCombo;
    @FXML
    private ComboBox<Dimension> dimensionCombo;
    @FXML
    private GridPane matrixPane;

    private ObservableList<Dimension> dimensions = FXCollections.observableArrayList();
    private ObservableList<String> sets = FXCollections.observableArrayList();
    private ArrayList<ArrayList<TextField>> matrixElements;
    private Matrix matrix;
    private RowEchelonForm REF;
    private MatrixEquation ME;

    private class Dimension {
        private String label;
        private int dimension;

        Dimension(String label, int dimension) {
            this.label = label;
            this.dimension = dimension;
        }
        String getLabel() {
            return label;
        }
        int getDimension() {
            return dimension;
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sets.addAll("R", "C" , "Z2", "Z3");
        setsCombo.setItems(sets);
        setsCombo.getSelectionModel().select(0);
        System.out.println("Default dimension: " + setsCombo.getValue());

        dimensions.addAll(new Dimension("3x3",3), new Dimension("4x4", 4), new Dimension("5x5", 5));
        dimensionCombo.setItems(dimensions);
        dimensionCombo.setConverter(new StringConverter<>() {

            @Override
            public String toString(Dimension object) {
                return object.getLabel();
            }

            @Override
            public Dimension fromString(String string) {
                return dimensionCombo.getItems().stream().filter(ap ->
                        ap.getLabel().equals(string)).findFirst().orElse(null);
            }
        });
        dimensionCombo.getSelectionModel().select(0);
        System.out.println("Default dimension: " + dimensionCombo.getValue().getLabel());
        matrixElements = new ArrayList<>();
        changeMatrixPane(3);

    }

    private void changeMatrixPane(int size){
        matrixPane.getChildren().removeAll(matrixPane.getChildren());
        matrixElements.clear();

        for(int i = 0; i < size; i++){
            matrixElements.add(new ArrayList<>());
            for(int j = 0; j < size; j++){
                TextField textField = new TextField("0");
                textField.focusedProperty().addListener(new ChangeListener<Boolean>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
                    {
                        if (newPropertyValue && textField.getText().trim().equals("0"))
                        {
                            textField.clear();
                        }
                        else
                        {
                            if(textField.getText() == null || textField.getText().trim().isEmpty()){
                                textField.setText("0");
                            }
                        }
                    }
                });
                matrixElements.get(i).add(textField);
                matrixPane.add(textField,j,i);
            }
        }
    }

    @FXML
    public void clickedDimensionCombo(){
        System.out.println("Selected dimension: " + dimensionCombo.getValue().getLabel() + " " + dimensionCombo.getValue().getDimension());
        changeMatrixPane(dimensionCombo.getValue().getDimension());
    }
    @FXML
    public void clickedSetsCombo() {
        System.out.println("Selected set: " + setsCombo.getValue());
    }
    @FXML
    public void buttonClicked(Event event) throws IOException {

        try {
            calculate();

            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setWidth(1000);
            stage.setHeight(800);
            stage.setResizable(true);
            createAndSetResultController();

        } catch (MatrixContainsFractionsException e){
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Skontrolujte zadané čísla");
            alert.setContentText("Pre pole Z2 a Z3 zadajte iba celé čísla.\n" +
                    "Nepoužívajte zlomky");
            alert.showAndWait();

        } catch (Exception e){
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Skontrolujte zadané čísla");
            alert.setContentText("pole R:\n" +
                    "celé čisla alebo zlomky v tvare 'x/y'\n" +
                    "napr: 1/2, -3/4, -1/-2\n\n" +
                    "pole C:\n" +
                    "celé čísla a komplexné čísla v tvare 'x+yi'\n" +
                    "ak reálna časť je 0, píšte: 0+2i\n" +
                    "ak imaginárna časť je 1, píšte: 4+1i\n" +
                    " ");
            alert.showAndWait();
        }
    }

    private void createAndSetResultController() throws IOException{

        ResultController resultController = new ResultController(REF, matrix, ME);

        FXMLLoader loader  = new FXMLLoader(
                getClass().getResource(
                        "result.fxml"
                )
        );
        loader.setController(resultController);
        Parent root = loader.load();
        App.setRoot(root);
    }

    @FXML
    void backButtonClicked() throws IOException {
        App.setRoot("primary");
    }

    private void calculate() throws MatrixContainsFractionsException {
        this.matrix = new Matrix(matrixElements, dimensionCombo.getValue().getDimension(), setsCombo.getValue());

        if(setsCombo.getValue().equals("C")){
            System.out.println("Matrices\n" + matrix.toStringMatricesComplex() + "\n");
            System.out.println("Unreduced Matrix\n___________________\n" + matrix.toStringComplex(matrix.getCoefficientMatrixComplex()) + "\n");
        } else {
            System.out.println("Matrices\n" + matrix.toStringMatrices() + "\n");
            System.out.println("Unreduced Matrix\n___________________\n" + matrix.toString(matrix.getCoefficientMatrix()) + "\n");
        }

        this.REF = new RowEchelonForm(matrix.getCoefficientMatrix(),matrix.getCoefficientMatrixComplex(), setsCombo.getValue());
        this.ME = new MatrixEquation(REF.getMatrix(), REF.getMatrixComplex(), REF.getRank(), setsCombo.getValue());

        if(setsCombo.getValue().equals("C")){
            System.out.println(REF.toStringComplex());
            System.out.println("SOLUTION" + ME.getVariableVectorComplex());

        }else{
            System.out.println(REF.toString());
            System.out.println("SOLUTION" + ME.getVariableVector());
        }
    }
}
