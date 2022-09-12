/**
 * This class controls the  result window
 * representation of the calculated data
 * result.fxml
 * style.css
 * Matrix, MatrixEquation, RowEchelonForm classes
 */
package sk.stuba.fei.xvillantova.bakalar;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.complex.Complex;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ResultController implements Initializable {
    @FXML
    private Button backButton = new Button();
    @FXML
    private ScrollPane scrollResult = new ScrollPane();
    @FXML
    private VBox vBoxResult = new VBox();

    private static final String MIDDLE_DOT = "\u00b7";
    private static final String TILDE = "\u007e";
    private static final String ELEMENT_OF = "\u2208";
    private static final String NULL_MATRIX = "\u014C";

    private static final String SUPERSCRIPT_ZERO = "\u2070";
    private static final String SUPERSCRIPT_ONE = "";          //"\u00B9";
    private static final String SUPERSCRIPT_TWO = "\u00B2";
    private static final String SUPERSCRIPT_THREE = "\u00B3";
    private static final String SUPERSCRIPT_FOUR = "\u2074";
    private static final String SUPERSCRIPT_FIVE = "\u2075";

    private static final ArrayList<String> superscripts = new ArrayList<>(
            Arrays.asList(SUPERSCRIPT_ZERO, SUPERSCRIPT_ONE, SUPERSCRIPT_TWO, SUPERSCRIPT_THREE, SUPERSCRIPT_FOUR, SUPERSCRIPT_FIVE));

    private static final String SUBSCRIPT_ZERO = "\u2080";
    private static final String SUBSCRIPT_ONE = "\u2081";
    private static final String SUBSCRIPT_TWO = "\u2082";
    private static final String SUBSCRIPT_THREE = "\u2083";
    private static final String SUBSCRIPT_FOUR = "\u2084";
    private static final String SUBSCRIPT_FIVE = "\u2085";

    private static final ArrayList<String> subscripts = new ArrayList<>(
            Arrays.asList(SUBSCRIPT_ZERO, SUBSCRIPT_ONE, SUBSCRIPT_TWO, SUBSCRIPT_THREE, SUBSCRIPT_FOUR, SUBSCRIPT_FIVE));

    private RowEchelonForm REF;
    private Matrix M;
    private MatrixEquation ME;
    private Integer dimension;
    private String set;

    private ArrayList<ArrayList<BigFraction>> matrixNoZeroRows = new ArrayList<>();
    private ArrayList<ArrayList<Complex>> matrixNoZeroRowsComplex = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Label>>> labels = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Label>>> activeLabels = new ArrayList<>();
    private ArrayList<ArrayList<Label>> activeCoeff = new ArrayList<>();
    private ArrayList<GridPane> gridPanes = new ArrayList<>();

    private Image leftBracket = new Image(getClass().getResourceAsStream("images/left-parentheses-logo-white.png"));
    private Image rightBracket = new Image(getClass().getResourceAsStream("images/right-parentheses-logo-white.png"));

    @FXML
    void backButtonClicked(Event event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setWidth(818);
        stage.setHeight(540);
        stage.setResizable(false);
        App.setRoot("calculationSetup");
    }

    ResultController(RowEchelonForm REF, Matrix M, MatrixEquation ME) {
        this.REF = REF;
        this.M = M;
        this.ME = ME;
        this.set = M.getSet();
        if (set.equals("C")) {
            this.dimension = (M.getMatricesComplex().size()) - 1;
            this.matrixNoZeroRowsComplex = REF.getMatrixComplex();
        } else {
            this.dimension = (M.getMatrices().size()) - 1;
            this.matrixNoZeroRows = REF.getMatrix();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // scrollResult.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vBoxResult.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //scrollResult.setMinSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vBoxResult.setMinWidth(600);
        //File file = new File(getClass().getResource("images/square_brackets_left.png"));
        //  System.out.println(file.exists());
        buildScene();
    }

    private void buildScene() {
        headerPane();
        multiplicationPanes();
        coefficientMatrixPane();
        matrixEquationPane();
        parametersPane();
        finalResultPane();
        //System.out.println(ME.getMEsteps());
    }

    private void headerPane() {

        Label labelMinPoly = new Label(" mA(λ) =  a" + SUBSCRIPT_ZERO + "+ a" + SUBSCRIPT_ONE + "λ + a" + SUBSCRIPT_TWO + "λ" + SUPERSCRIPT_TWO + "+ a" + SUBSCRIPT_THREE + "λ" + SUPERSCRIPT_THREE);
        Label labelMatrixPoly = new Label(" mA(A) =  a" + SUBSCRIPT_ZERO + "I" + subscripts.get(dimension) + " + a" + SUBSCRIPT_ONE + "A + a" + SUBSCRIPT_TWO + "A" + SUPERSCRIPT_TWO + "+ a" + SUBSCRIPT_THREE + "A" + SUPERSCRIPT_THREE);
        Label labelNumbers = new Label("a" + SUBSCRIPT_ZERO + ", a" + SUBSCRIPT_ONE + ", a" + SUBSCRIPT_TWO + ", a" + SUBSCRIPT_THREE);
        Label labelEquation = new Label(" a" + SUBSCRIPT_ZERO + "I" + subscripts.get(dimension) + " + a" + SUBSCRIPT_ONE + "A + a" + SUBSCRIPT_TWO + "A" + SUPERSCRIPT_TWO + "+ a" + SUBSCRIPT_THREE + "A" + SUPERSCRIPT_THREE);
        for (int i = 4; i <= dimension; i++) {
            labelMinPoly.setText(labelMinPoly.getText() + "+ a" + subscripts.get(i) + "λ" + superscripts.get(i) + " ");
            labelMatrixPoly.setText(labelMatrixPoly.getText() + "+ a" + subscripts.get(i) + "A" + superscripts.get(i) + " ");
            labelNumbers.setText(labelNumbers.getText() + ", a" + subscripts.get(i));
            labelEquation.setText(labelEquation.getText() + "+ a" + subscripts.get(i) + "A" + superscripts.get(i));
        }
        labelEquation.setText(labelEquation.getText() + " = 0" + subscripts.get(dimension) + "x" + subscripts.get(dimension) + " ");

        Label headerLabel_1 = new Label("\nRiešenie: \n" +
                "Našou úlohou je určiť minimálny polynóm. Označovať  ho budeme mA(λ). \n" +
                "Minimálny polynóm je stupňa najviac n (dôsledok Cayleyho-Hamiltonovej vety), preto \n\n");

        Label headerLabel_2 = new Label("\nZ toho definujeme maticový polynóm, ktorý vyzerá nasledovne: \n\n");
        Label headerLabel_3 = new Label("\nVieme, že mA(A) = 0 a hľadáme teda čísla " + labelNumbers.getText() + ", tak aby \n\n");
        Label headerLabel_4 = new Label("Vstupná matica : \n");
        Label headerLabel_5 = new Label("\nMatica I" + subscripts.get(dimension) + " je jednotková (identická) matica veľkosti " + dimension + ":\n");

        headerLabel_1.getStyleClass().add("theory-label");
        headerLabel_2.getStyleClass().add("theory-label");
        headerLabel_3.getStyleClass().add("theory-label");
        headerLabel_4.getStyleClass().add("theory-label");
        headerLabel_5.getStyleClass().add("theory-label");
        labelMinPoly.getStyleClass().add("theory-label-blue");
        labelMatrixPoly.getStyleClass().add("theory-label-blue");
        labelEquation.getStyleClass().add("theory-label-blue");

        vBoxResult.getChildren().addAll(headerLabel_1, labelMinPoly, headerLabel_2, labelMatrixPoly, headerLabel_3, labelEquation, headerLabel_5);
        identityMatrixPane();
        vBoxResult.getChildren().add(headerLabel_4);
        inputMatrixPane();
    }

    private void inputMatrixPane() {
        FlowPane fp = new FlowPane();
        GridPane gp = new GridPane();
        Label matrixName = new Label();
        Label numberSetLabel = new Label(ELEMENT_OF + set);

        if (set.equals("C")) {
            makeLabelsFromMatrixAndAddToGrid(gp, null, M.getMatricesComplex().get(1));
        } else {
            makeLabelsFromMatrixAndAddToGrid(gp, M.getMatrices().get(1), null);
        }
        matrixName.getStyleClass().add("theory-label-blue");
        matrixName.setText(" A = ");
        gridPanes.add(gp);
        ImageView imageViewLeft = createImageview("left", dimension);
        ImageView imageViewRight = createImageview("right", dimension);
        fp.getChildren().addAll(matrixName, imageViewLeft, gp, imageViewRight, numberSetLabel);
        vBoxResult.getChildren().add(fp);
        vBoxResult.getChildren().add(new Label("\n"));
    }

    private void identityMatrixPane() {
        FlowPane fp = new FlowPane();
        GridPane gp = new GridPane();

        if (set.equals("C")) {
            makeLabelsFromMatrixAndAddToGrid(gp, null, M.getMatricesComplex().get(0));
        } else {
            makeLabelsFromMatrixAndAddToGrid(gp, M.getMatrices().get(0), null);
        }
        Label label = new Label();
        label.getStyleClass().add("theory-label-blue");
        label.setText(" I" + subscripts.get(dimension) + " = ");

        gridPanes.add(gp);
        ImageView imageViewLeft = createImageview("left", dimension);
        ImageView imageViewRight = createImageview("right", dimension);
        fp.getChildren().addAll(label, imageViewLeft, gp, imageViewRight);
        vBoxResult.getChildren().add(fp);
        vBoxResult.getChildren().add(new Label("\n"));
    }

    private void multiplicationPanes() {

        Label multiplicationLabel = new Label("Matice A" + superscripts.get(2) + ", A" + superscripts.get(3));

        for (int i = 4; i <= dimension; i++) {
            multiplicationLabel.setText(multiplicationLabel.getText() + ", A" + superscripts.get(i));
        }
        multiplicationLabel.setText(multiplicationLabel.getText() + " vypočítame pomocou násobenie matíc:\n");
        multiplicationLabel.getStyleClass().add("theory-label");

        vBoxResult.getChildren().add(multiplicationLabel);

        for (int i = 2; i < dimension + 1; i++) {

            FlowPane multiplicationFlow = new FlowPane();
            GridPane multiplicandGrid = new GridPane();
            GridPane multiplierGrind = new GridPane();
            GridPane productGrid = new GridPane();

            Label text = new Label();
            text.getStyleClass().add("theory-label-blue");
            text.setText(" A " + MIDDLE_DOT + " A" + superscripts.get(i - 1) + " = A" + superscripts.get(i) + "  ");
            Label multiplicationSign = new Label(MIDDLE_DOT);
            Label equalitySign = new Label(" = ");

            if (set.equals("C")) {
                fillGridAndArrayWithLabels(multiplicandGrid, null, M.getMatricesComplex().get(1));
                fillGridAndArrayWithLabels(multiplierGrind, null, M.getMatricesComplex().get(i - 1));
                fillGridAndArrayWithActiveLabels(productGrid, null, M.getMatricesComplex().get(i));
            } else {
                fillGridAndArrayWithLabels(multiplicandGrid, M.getMatrices().get(1), null);
                fillGridAndArrayWithLabels(multiplierGrind, M.getMatrices().get(i - 1), null);
                fillGridAndArrayWithActiveLabels(productGrid, M.getMatrices().get(i), null);
            }

            ImageView imageViewLeft1 = createImageview("left", dimension);
            ImageView imageViewRight1 = createImageview("right", dimension);
            ImageView imageViewLeft2 = createImageview("left", dimension);
            ImageView imageViewRight2 = createImageview("right", dimension);
            ImageView imageViewLeft3 = createImageview("left", dimension);
            ImageView imageViewRight3 = createImageview("right", dimension);

            gridPanes.add(productGrid);
            multiplicationFlow.getChildren().addAll(text, new HBox(imageViewLeft1, multiplicandGrid, imageViewRight1), multiplicationSign, new HBox(imageViewLeft2, multiplierGrind, imageViewRight2), equalitySign, new HBox(imageViewLeft3, productGrid, imageViewRight3));
            vBoxResult.getChildren().add(multiplicationFlow);
            vBoxResult.getChildren().add(new Label("\n"));
        }
    }

    private void coefficientMatrixPane() {

        Label coeffMatrixLabel = new Label("Teraz napíšeme maticu homogénnej sústavy lineárnych rovníc (pre každú pozíciu (ij) jedna rovnica),");
        Label coeffMatrixLabel2 = new Label(" označíme ju H a pomocou ");
        Label gaussLabel = new Label(" Gaussovej eliminačnej metódy ");
        Label coeffMatrixLabel3 = new Label(" maticu upravíme na stupňovitý tvar\n ");

        coeffMatrixLabel.getStyleClass().add("theory-label");
        coeffMatrixLabel2.getStyleClass().add("theory-label");
        coeffMatrixLabel3.getStyleClass().add("theory-label");
        gaussLabel.getStyleClass().add("theory-label-blue");

        vBoxResult.getChildren().addAll(coeffMatrixLabel, new HBox(coeffMatrixLabel2, gaussLabel, coeffMatrixLabel3));

        FlowPane flowPane = new FlowPane();
        int matrixTransSize;
        if (set.equals("C")) {
            matrixTransSize = REF.getMatrixTransformationComplex().size();
        } else {
            matrixTransSize = REF.getMatrixTransformation().size();
        }

        for (int i = 0; i < matrixTransSize; i++) {

            GridPane gridPane = new GridPane();
            Label step = new Label();
            step.getStyleClass().add("theory-label-step");

            if (i == 0) {
                if (set.equals("C")) {
                    addCoefficientActiveLabelsAndAddToGrid(gridPane, null, REF.getMatrixTransformationComplex().get(i));
                } else {
                    addCoefficientActiveLabelsAndAddToGrid(gridPane, REF.getMatrixTransformation().get(i), null);
                }
            } else {
                if (set.equals("C")) {
                    makeLabelsFromMatrixAndAddToGrid(gridPane, null, REF.getMatrixTransformationComplex().get(i));
                } else {
                    makeLabelsFromMatrixAndAddToGrid(gridPane, REF.getMatrixTransformation().get(i), null);
                }
            }
            step.setText(REF.getSteps().get(i));
            ImageView imageViewLeft = createImageview("left", (dimension * dimension) + 2);
            ImageView imageViewRight = createImageview("right", (dimension * dimension) + 2);
            VBox transitonLabelBox = new VBox();

            if (i == 0) {
                transitonLabelBox.getChildren().addAll(step);
            } else {
                transitonLabelBox.getChildren().addAll(new Label(TILDE), step);
            }

            transitonLabelBox.getStyleClass().add("vbox-transition");
            flowPane.getChildren().addAll(transitonLabelBox, new HBox(imageViewLeft, gridPane, imageViewRight));
        }
        vBoxResult.getChildren().add(flowPane);
    }

    private void parametersPane() {

        Label parametersLabel3 = new Label("\nAby sme dostali nenulový polynóm čo najmenšieho stupňa, volíme parametre nasledovne: \n");
        parametersLabel3.getStyleClass().add("theory-label");

        vBoxResult.getChildren().add(parametersLabel3);

        VBox parametersVBox = new VBox();
        parametersVBox.getStyleClass().add("vbox-parameters");

        for (int i = 0; i < ME.getMEsteps().size(); i++) {
            Label step = new Label();
            step.getStyleClass().add("theory-label-without-bg");
            step.setText(ME.getMEsteps().get(i));
            parametersVBox.getChildren().add(step);
        }
        vBoxResult.getChildren().add(parametersVBox);
    }

    private void matrixEquationPane() {

        Label parametersLabel = new Label("\nDostali sme stupňovitú maticu, označíme ju B. \n" +
                "Hodnosť matice B je");
        Label rankLabel = new Label(" h(B) = " + REF.getRank() + " \n");
        Label parametersLabel2 = new Label("\nSústavu lineárnych rovníc môžeme zapísať v maticovom tvare Bx=b,\n" +
                "kde B je matica sústavy (naša stupňovitá matica), x (neznáme) a b (0) sú vektory. ");

        parametersLabel.getStyleClass().add("theory-label");
        parametersLabel2.getStyleClass().add("theory-label");
        rankLabel.getStyleClass().add("theory-label-blue");

        vBoxResult.getChildren().addAll(parametersLabel, rankLabel, parametersLabel2);

        FlowPane fp = new FlowPane();
        GridPane coeffMatrixGridP = new GridPane();

        if (set.equals("C")) {
            makeLabelsFromMatrixAndAddToGrid(coeffMatrixGridP, null, matrixNoZeroRowsComplex);
        } else {
            makeLabelsFromMatrixAndAddToGrid(coeffMatrixGridP, matrixNoZeroRows, null);
        }
        //fillGridAndArrayWithLabels(gp, M.getMatrices().get(i));
        Label matrixName = new Label();
        matrixName.getStyleClass().add("theory-label-blue");
        matrixName.setText(" B" + MIDDLE_DOT + "x = b =>");

        ImageView imageViewLeft = createImageview("left", REF.getRank());
        ImageView imageViewRight = createImageview("right", REF.getRank());
        ImageView imageViewLeft2 = createImageview("left", dimension + 1);
        ImageView imageViewRight2 = createImageview("right", dimension + 1);
        fp.getChildren().addAll(matrixName, imageViewLeft, coeffMatrixGridP, imageViewRight, new Label(MIDDLE_DOT), imageViewLeft2, createVectorArrayGrid(), imageViewRight2, new Label(" = " + NULL_MATRIX));
        vBoxResult.getChildren().add(fp);
    }

    private GridPane createVectorArrayGrid() {

        ArrayList<String> vectorArray = new ArrayList<>();
        GridPane vectorGridP = new GridPane();

        String variable = "";
        for (int i = 0; i <= dimension; i++) {
            variable = "a" + subscripts.get(i);
            vectorArray.add(variable);
            variable = "";
        }
        for (int i = 0; i < vectorArray.size(); i++) {
            Label element = new Label();
            element.setText((vectorArray.get(i)));
            setGridPaneAndLabelStyle(vectorGridP, element);
            vectorGridP.add(element, 0, i);
        }
        return vectorGridP;
    }

    private void finalResultPane() {
        Label finalLabel = new Label("\nRiešenie minimálneho polynómu je: \n\n");
        finalLabel.getStyleClass().add("theory-label");
        vBoxResult.getChildren().add(finalLabel);

        Label result = new Label();
        Label minPoly = new Label();
        String resultStr = " mA(λ) = ";
        String minPolyStr = " mA(λ) = ";
        /**
         * Vseobecne riesenie
         * General solution with variable names
         */
        int varVectorSize;
        if (set.equals("C")) {
            varVectorSize = ME.getVariableVectorComplex().size() - 1;
        } else {
            varVectorSize = ME.getVariableVector().size() - 1;
        }
        for (int i = varVectorSize; i >= 0; i--) {
            if (i != 0) {
                resultStr += "a" + subscripts.get(i) + " λ" + superscripts.get(i);
                resultStr += " + ";
            } else {
                resultStr += "a" + subscripts.get(i);
                resultStr += " = >";
            }
        }
        /**
         * The SOLUTION - minimal polynomial
         */
        for (int i = varVectorSize; i >= 0; i--) {
            if (set.equals("C")) {
                if (!ME.getVariableVectorComplex().get(i).equals(Complex.ZERO)) {
                    if (i != 0) {
                        //we dont want to print out the numbers 1 and -1
                        if (ME.getVariableVectorComplex().get(i).equals(Complex.ONE)) {
                            minPolyStr += " λ" + superscripts.get(i);
                        } else if (ME.getVariableVectorComplex().get(i).equals((Complex.ONE).negate())) {
                            minPolyStr += " - λ" + superscripts.get(i);
                        } else {
                            minPolyStr += "( " + new ComplexFormat().format(ME.getVariableVectorComplex().get(i)) + " ) λ" + superscripts.get(i);

                        }
                        boolean needPlusSign = false;
                        for (int j = i - 1; j >= 0; j--) {
                            //if the next number is not null and is a positive number then write a plus sign
                            if (!ME.getVariableVectorComplex().get(j).equals(Complex.ZERO)) {
                                needPlusSign = true;
                            }
                        }
                        if (needPlusSign) {
                            minPolyStr += " + ";
                        } else {
                            minPolyStr += " ";
                        }
                    } else {
                        minPolyStr += " ( " + new ComplexFormat().format(ME.getVariableVectorComplex().get(i)) + " ) ";
                    }
                }
            } else {
                if (!ME.getVariableVector().get(i).equals(BigFraction.ZERO)) {
                    if (i != 0) {
                        //we dont want to print out the numbers 1 and -1
                        if (ME.getVariableVector().get(i).equals(BigFraction.ONE)) {
                            minPolyStr += " λ" + superscripts.get(i);
                        } else if (ME.getVariableVector().get(i).equals(BigFraction.MINUS_ONE)) {
                            minPolyStr += " - λ" + superscripts.get(i);
                        } else {
                            minPolyStr += ME.getVariableVector().get(i) + " λ" + superscripts.get(i);
                        }
                        boolean needPlusSign = false;
                        for (int j = i - 1; j >= 0; j--) {
                            //if the next number is not null and is a positive number then write a plus sign
                            if (!ME.getVariableVector().get(j).equals(BigFraction.ZERO)) {
                                if (ME.getVariableVector().get(j).getNumeratorAsInt() > 0) {
                                    needPlusSign = true;
                                } else {
                                    break;
                                }
                            }
                        }
                        if (needPlusSign) {
                            minPolyStr += " + ";
                        } else {
                            minPolyStr += " ";
                        }
                    } else {
                        minPolyStr += ME.getVariableVector().get(i) + " ";
                    }
                }
            }
        }
        result.setText(resultStr);
        minPoly.setText(minPolyStr);
        minPoly.getStyleClass().add("result");
        vBoxResult.getChildren().add(result);
        vBoxResult.getChildren().add(minPoly);
    }

    private void addCoefficientActiveLabelsAndAddToGrid(GridPane grid, ArrayList<ArrayList<BigFraction>> matrix, ArrayList<ArrayList<Complex>> matrixComplex) {

        //activeCoeff.add(new ArrayList<>());
        int matrixTransSize;
        int matrixTransRowSize;

        if (set.equals("C")) {
            matrixTransSize = matrixComplex.size();
            matrixTransRowSize = matrixComplex.get(0).size();
        } else {
            matrixTransSize = matrix.size();
            matrixTransRowSize = matrix.get(0).size();
        }
        for (int i = 0; i < matrixTransSize; i++) {
            activeCoeff.add(new ArrayList<>());
            for (int j = 0; j < matrixTransRowSize; j++) {
                Label activeLabel = new Label();
                activeLabel.getStyleClass().add("active-label");
                activeLabel.setOnMouseEntered(event -> {
                    findCoeffActiveLabelsPosition(event);
                });
                activeLabel.setOnMouseExited(event -> {
                    findCoeffActiveLabelsPosition(event);
                });
                if (set.equals("C")) {
                    activeLabel.setText(new ComplexFormat().format(matrixComplex.get(i).get(j)));
                } else {
                    activeLabel.setText((matrix.get(i).get(j)).toString());
                }
                activeCoeff.get(i).add(activeLabel);
                setGridPaneAndLabelStyle(grid, activeLabel);
                grid.add(activeLabel, j, i);
            }
        }
    }

    private void findCoeffActiveLabelsPosition(Event event) {
        int count;
        /**
         *  i -th row
         *  count -th col
         */
        for (int i = 0; i < activeCoeff.size(); i++) {
            count = 0;
            for (Label l : activeCoeff.get(i)) {
                if (l.equals(event.getSource())) {
                    repaintMatrixRelatedWithCoeffCol(count, event);
                    return;
                }
                count++;
            }
        }
    }

    private void repaintMatrixRelatedWithCoeffCol(int col, Event event) {

        String style = "";

        if (event.getEventType().getName().equals("MOUSE_ENTERED")) {
            style = "-fx-background-color : -fx-yellow;-fx-background-radius: 3;-fx-text-fill: -fx-dark-blue;";
        }
        if (event.getEventType().getName().equals("MOUSE_EXITED")) {
            //style = "-fx-background-color : transparent;";
        }
        gridPanes.get(col).setStyle(style);

        for (ArrayList<Label> row: activeCoeff
             ) {
            row.get(col).setStyle(style);
        }
    }

    private void fillGridAndArrayWithLabels(GridPane grid, ArrayList<ArrayList<BigFraction>> matrix, ArrayList<ArrayList<Complex>> matrixComplex) {

        labels.add(new ArrayList<>());

        int matrixSize;

        if (set.equals("C")) {
            matrixSize = matrixComplex.size();
        } else {
            matrixSize = matrix.size();
        }
        for (int i = 0; i < matrixSize; i++) {
            labels.get(labels.size() - 1).add(new ArrayList<>());
            for (int j = 0; j < matrixSize; j++) {
                Label label = new Label();
                if (set.equals("C")) {
                    label.setText(new ComplexFormat().format(matrixComplex.get(i).get(j)));
                } else {
                    label.setText((matrix.get(i).get(j)).toString());
                }
                labels.get(labels.size() - 1).get(i).add(label);
                setGridPaneAndLabelStyle(grid, label);
                grid.add(label, j, i);
            }
        }
    }

    private void fillGridAndArrayWithActiveLabels(GridPane grid, ArrayList<ArrayList<BigFraction>> matrix, ArrayList<ArrayList<Complex>> matrixComplex) {

        activeLabels.add(new ArrayList<>());

        int matrixSize;

        if (set.equals("C")) {
            matrixSize = matrixComplex.size();
        } else {
            matrixSize = matrix.size();
        }

        for (int i = 0; i < matrixSize; i++) {
            activeLabels.get(activeLabels.size() - 1).add(new ArrayList<>());
            for (int j = 0; j < matrixSize; j++) {
                Label activeLabel = new Label();
                activeLabel.getStyleClass().add("active-label");

                activeLabel.setOnMouseEntered(event -> {
                            findActiveLabelsPosition(event);
                        }
                );
                activeLabel.setOnMouseExited(event -> {
                    findActiveLabelsPosition(event);
                });

                if (set.equals("C")) {
                    activeLabel.setText(new ComplexFormat().format(matrixComplex.get(i).get(j)));
                } else {
                    activeLabel.setText((matrix.get(i).get(j)).toString());
                }
                activeLabels.get(activeLabels.size() - 1).get(i).add(activeLabel);

                setGridPaneAndLabelStyle(grid, activeLabel);

                grid.add(activeLabel, j, i);
            }
        }
    }

    private void findActiveLabelsPosition(Event event) {
        int count;
        /**
         *  i -th Matrix
         *  j -th row
         *  count -th col
         */
        for (int i = 0; i < activeLabels.size(); i++) {
            for (int j = 0; j < activeLabels.get(0).size(); j++) {
                count = 0;
                for (Label l : activeLabels.get(i).get(j)) {
                    if (l.equals(event.getSource())) {
                        repaintRelatedLabels(i, j, count, event);
                        return;
                    }
                    count++;
                }
            }
        }
    }

    private void repaintRelatedLabels(int product, int row, int col, Event event) {

        String style = "";

        if (event.getEventType().getName().equals("MOUSE_ENTERED")) {
            style = "-fx-background-color : -fx-yellow; -fx-background-radius: 3;-fx-text-fill: -fx-dark-blue;";
        }
        if (event.getEventType().getName().equals("MOUSE_EXITED")) {
            //style = "-fx-background-color : transparent;";
        }

        activeLabels.get(product).get(row).get(col).setStyle(style);

        for (int i = 0; i < labels.get(0).get(0).size(); i++) {
            labels.get(product * 2).get(row).get(i).setStyle(style);
            labels.get(product * 2 + 1).get(i).get(col).setStyle(style);
        }
    }

    private void makeLabelsFromMatrixAndAddToGrid(GridPane grid, ArrayList<ArrayList<BigFraction>> matrix, ArrayList<ArrayList<Complex>> matrixComplex) {

        int matrixSize;
        int matrixRowSize;

        if (set.equals("C")) {
            matrixSize = matrixComplex.size();
            matrixRowSize = matrixComplex.get(0).size();
        } else {
            matrixSize = matrix.size();
            matrixRowSize = matrix.get(0).size();
        }

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixRowSize; j++) {
                Label element = new Label();
                if (set.equals("C")) {
                    //element.setText((matrixComplex.get(i).get(j)).toString());
                    element.setText(new ComplexFormat().format(matrixComplex.get(i).get(j)));
                } else {
                    element.setText((matrix.get(i).get(j)).toString());
                }

                setGridPaneAndLabelStyle(grid, element);

                grid.add(element, j, i);
            }
        }
    }

    private void setGridPaneAndLabelStyle(GridPane gridPane, Label label) {
        gridPane.setGridLinesVisible(false);
        gridPane.setHgap(8);
        gridPane.setVgap(6);
        gridPane.alignmentProperty().set(Pos.CENTER);
        gridPane.setFillWidth(label, true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
    }

    private ImageView createImageview(String orientation, int sizing) {
        ImageView iv = new ImageView();

        if (orientation.equals("left")) {
            iv.setImage(leftBracket);
        }
        if (orientation.equals("right")) {
            iv.setImage(rightBracket);
        }

        iv.setPreserveRatio(false);
        iv.setFitWidth(20);
        iv.setFitHeight((sizing) * 23);
        iv.setSmooth(true);
        iv.setCache(true);

        return iv;
    }

    public String toStringLabels(ArrayList<ArrayList<ArrayList<Label>>> list) {
        return list.toString().replace("], ", "]\n").replace("]]", "]]\n").replace("[[[", "[\n[[");
    }
       /*  public void inputAndIdentityMatrixPane() {
          for (int i = 0; i < 2; i++) {
              FlowPane fp = new FlowPane();
              GridPane gp = new GridPane();
              makeLabelsFromMatrixAndAddToGrid(gp, M.getMatrices().get(i), null);
              Label label = new Label();
              label.getStyleClass().add("theory-label-blue");
              if (i == 0) {
                  label.setText(" I = ");
              } else {
                  label.setText(" A = ");
              }
              gridPanes.add(gp);
              ImageView imageViewLeft = new ImageView();
              fp.getChildren().addAll(label, gp);
              vBoxResult.getChildren().add(fp);
          }
      }
  */
}
