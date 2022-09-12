package sk.stuba.fei.xvillantova.bakalar;

import javafx.scene.control.TextField;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.fraction.BigFractionFormat;
import sk.stuba.fei.xvillantova.bakalar.Exceptions.MatrixContainsFractionsException;
import java.util.ArrayList;

public class Matrix {

    private int size;
    private String set;
    /**
     * matrices - contains the identity matrix, input matrix and the multiplications
     */
    private ArrayList<ArrayList<ArrayList<BigFraction>>> matrices;
    private ArrayList<ArrayList<ArrayList<Complex>>> matricesComplex;

    /**
     * f(A) = x0*I + x1*(A^1) + x2*(A^2) + x3*(A^3) = b -> Vector equation.
     * Ax = b -> Matrix equation where A is the coefficient matrix, A= [ I|A1|A2|A3 ]
     */
    private ArrayList<ArrayList<BigFraction>> coefficientMatrix;
    private ArrayList<ArrayList<Complex>> coefficientMatrixComplex;


    Matrix(ArrayList<ArrayList<TextField>> textFields, int size, String set) throws MatrixContainsFractionsException {
        this.size = size;
        this.set = set;

        if (set.equals("C")) {
            this.matricesComplex = new ArrayList<>();
            createIdentityMatrixComplex();
            convertTextFieldsToComplexArray(textFields);
            createMatrixMultiplications();
            createCoefficientMatrix();

        } else {
            this.matrices = new ArrayList<>();
            createIdentityMatrix();
            convertTextFieldsToFractionArray(textFields);
            createMatrixMultiplications();
            createCoefficientMatrix();
        }
    }

    private void convertTextFieldsToFractionArray(ArrayList<ArrayList<TextField>> textFields) throws MatrixContainsFractionsException{
        matrices.add(new ArrayList<>());

        for (int i = 0; i < size; i++) {
            matrices.get(1).add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                //numberStr = in.nextLine();
                BigFraction fraction = new BigFractionFormat().parse(textFields.get(i).get(j).getText());
                if (set.equals("Z2") || set.equals("Z3")){
                    if(!checkFractionDenominatorIsOne(fraction)){
                        throw new MatrixContainsFractionsException();
                    }
                    fraction = modulo(fraction);
                }
                matrices.get(1).get(i).add(fraction);
              //  matrices.get(1).get(i).add(stringToBigFraction(textFields.get(i).get(j).getText()));
            }
        }
    }

    private void convertTextFieldsToComplexArray(ArrayList<ArrayList<TextField>> textFields) {
        matricesComplex.add(new ArrayList<>());

        for (int i = 0; i < size; i++) {
            matricesComplex.get(1).add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                Complex c = new ComplexFormat().parse(textFields.get(i).get(j).getText());
                matricesComplex.get(1).get(i).add(c);
            }
        }
    }

    private void createIdentityMatrix() {
        ArrayList<ArrayList<BigFraction>> identityMatrix = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            identityMatrix.add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    identityMatrix.get(i).add(BigFraction.ONE);
                } else {
                    identityMatrix.get(i).add(BigFraction.ZERO);
                }
            }
        }
        this.matrices.add(identityMatrix);
    }

    private void createIdentityMatrixComplex() {
        ArrayList<ArrayList<Complex>> identityMatrix = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            identityMatrix.add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    identityMatrix.get(i).add(Complex.ONE);
                } else {
                    identityMatrix.get(i).add(Complex.ZERO);
                }
            }
        }
        this.matricesComplex.add(identityMatrix);
    }

    private void createMatrixMultiplications() {
        for (int i = 1; i < size; i++) {
            System.out.println("MATRIX^" + 1 + " X MATRIX^" + i);
            if (set.equals("C")) {
                matricesComplex.add(multiplyMatricesComplex(matricesComplex.get(1), matricesComplex.get(i)));
            } else {
                matrices.add(multiplyMatrices(matrices.get(1), matrices.get(i)));
            }
        }
    }

    private void createCoefficientMatrix() {
        if (set.equals("C")) {
            this.coefficientMatrixComplex = new ArrayList<>();
        } else {
            this.coefficientMatrix = new ArrayList<>();
        }

        int count = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (set.equals("C")) {
                    coefficientMatrixComplex.add(new ArrayList<>());
                } else {
                    coefficientMatrix.add(new ArrayList<>());
                }

                for (int k = 0; k <= size; k++) {
                    if (set.equals("C")) {
                        coefficientMatrixComplex.get(count).add(matricesComplex.get(k).get(j).get(i));
                    } else {
                        coefficientMatrix.get(count).add(matrices.get(k).get(j).get(i));
                    }
                }
                count++;
            }
        }
    }

    /**
     * Multiplies two same size matrices.
     *
     * @param multiplicand Multiplicand Matrix - ArrayList of arrayLists
     * @param multiplier   Multiplier Matrix   - ArrayList of arrayLists
     * @return product      A new ArrayList
     */
    private ArrayList<ArrayList<BigFraction>> multiplyMatrices(ArrayList<ArrayList<BigFraction>> multiplicand, ArrayList<ArrayList<BigFraction>> multiplier) {

        // multiplicand x multiplier = product

        ArrayList<ArrayList<BigFraction>> product = new ArrayList<>();
        BigFraction partialProduct;

        for (int i = 0; i < size; i++) {
            product.add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                partialProduct = BigFraction.ZERO;
                for (int k = 0; k < size; k++) {
                    partialProduct = partialProduct.add((multiplicand.get(i).get(k)).multiply(multiplier.get(k).get(j)));
                    partialProduct = modulo(partialProduct);
                }
                product.get(i).add(partialProduct);
            }
        }
        return product;
    }

    private ArrayList<ArrayList<Complex>> multiplyMatricesComplex(ArrayList<ArrayList<Complex>> multiplicand, ArrayList<ArrayList<Complex>> multiplier) {

        // multiplicand x multiplier = product

        ArrayList<ArrayList<Complex>> product = new ArrayList<>();
        Complex partialProduct;

        for (int i = 0; i < size; i++) {
            product.add(new ArrayList<>());
            for (int j = 0; j < size; j++) {
                partialProduct = Complex.ZERO;
                for (int k = 0; k < size; k++) {
                    partialProduct = partialProduct.add((multiplicand.get(i).get(k)).multiply(multiplier.get(k).get(j)));
                }
                product.get(i).add(partialProduct);
            }
        }
        return product;
    }

    private BigFraction modulo(BigFraction number) {

        if (set.equals("Z2")) {
            BigFraction remainder = new BigFraction(Math.floorMod(number.intValue(), 2));
            //System.out.println(number + " modulo " + set + " = " + remainder);
            return remainder;
        }

        if (set.equals("Z3")) {
            BigFraction remainder = new BigFraction(Math.floorMod(number.intValue(), 3));
            //System.out.println(number + " modulo " + set + " = " + remainder);
            return remainder;
        }
        return number;
    }

    private boolean checkFractionDenominatorIsOne(BigFraction fraction){
        return fraction.getDenominatorAsInt() == 1;
    }

    /**
     * Converts String to BigFraction
     *
     * @param str String which has a form: "Integer" or "Integer/Integer".
     *            "Integer/Integer" - splits to numerator and denominator
     * @return
     */
    private BigFraction stringToBigFraction(String str) {

        BigFraction frac;

        if (str.contains("/")) {
            String split[] = str.split("/");
            String numerator = split[0];
            String denominator = split[1];
            frac = new BigFraction(Integer.parseInt(numerator), Integer.parseInt(denominator));
        } else {
            frac = new BigFraction(Integer.parseInt(str), 1);
        }
        return frac;
    }

    /**
     * Converts the 2D ArrayList to String
     * Every ArrayList goes to new line
     *
     * @return String
     */
    public String toString(ArrayList<ArrayList<BigFraction>> arrayList) {
        return arrayList.toString().replace("], ", "]\n");
    }

    public String toStringComplex(ArrayList<ArrayList<Complex>> arrayList) {
        return arrayList.toString().replace("], ", "]\n");
    }

    public String toStringMatrices() {
        return this.matrices.toString().replace("], ", "]\n").replace("]]", "]]\n").replace("[[[", "[\n[[");
    }

    public String toStringMatricesComplex() {
        return this.matricesComplex.toString().replace("], ", "]\n").replace("]]", "]]\n").replace("[[[", "[\n[[");
    }

    public ArrayList<ArrayList<BigFraction>> getCoefficientMatrix() {
        return coefficientMatrix;
    }

    public ArrayList<ArrayList<Complex>> getCoefficientMatrixComplex() {
        return coefficientMatrixComplex;
    }

    public ArrayList<ArrayList<ArrayList<BigFraction>>> getMatrices() {
            return matrices;
    }

    public ArrayList<ArrayList<ArrayList<Complex>>> getMatricesComplex() {
            return matricesComplex;
    }

    public String getSet() {
        return set;
    }
}


