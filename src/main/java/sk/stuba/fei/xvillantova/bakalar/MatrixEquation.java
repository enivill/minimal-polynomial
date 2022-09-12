package sk.stuba.fei.xvillantova.bakalar;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Arrays;

public class MatrixEquation {

    /**
     * Ax=b matrix equation
     * A ---> coefficientMatrix
     * x ---> variableVector
     * b ---> in our case always a 0 vector
     */

    private static final String SUBSCRIPT_ZERO = "\u2080";
    private static final String SUBSCRIPT_ONE = "\u2081";
    private static final String SUBSCRIPT_TWO = "\u2082";
    private static final String SUBSCRIPT_THREE = "\u2083";
    private static final String SUBSCRIPT_FOUR = "\u2084";
    private static final String SUBSCRIPT_FIVE = "\u2085";

    private static final ArrayList<String> subscripts = new ArrayList<>(
            Arrays.asList(SUBSCRIPT_ZERO, SUBSCRIPT_ONE, SUBSCRIPT_TWO, SUBSCRIPT_THREE, SUBSCRIPT_FOUR, SUBSCRIPT_FIVE));


    private ArrayList<ArrayList<BigFraction>> coefficientMatrix;
    private ArrayList<ArrayList<Complex>> coefficientMatrixComplex;
    private String set;
    private ArrayList<BigFraction> variableVector;
    private ArrayList<Complex> variableVectorComplex;
    private ArrayList<Integer> pivots = new ArrayList<>();
    private int rank;
    private int cols;
    private BigFraction solution = BigFraction.ONE;
    private Complex solutionC = Complex.ONE;
    private ArrayList<String> MEsteps;

    public MatrixEquation(ArrayList<ArrayList<BigFraction>> matrix, ArrayList<ArrayList<Complex>> matrixComplex, int rank, String set) {
        System.out.println("MATRIX EQUATION\n_________________________________\n");
        MEsteps = new ArrayList<>();
        this.rank = rank;
        this.set = set;

        System.out.println("RANK: " + this.rank + " SET: " + this.set);

        if (set.equals("C")) {
            this.coefficientMatrixComplex = matrixComplex;
            this.cols = matrixComplex.get(0).size();
        } else {
            this.coefficientMatrix = matrix;
            this.cols = matrix.get(0).size();

        }

        initializeVariableVector();
        //System.out.println("EMPTY VARIABLE VECTOR:  " + this.getVariableVectorComplex());
        findPivotColumns();
        System.out.println("PIVOT COLUMNS: " + pivots);
        setVariablesInNonPivotColumns();
        //System.out.println("VARIABLE NON PIVOT COL: " + this.getVariableVectorComplex());
        setVariablesInPivotColumns();
        //System.out.println("VARIABLE IN PIVOT COL: " + this.getVariableVectorComplex());
    }

    public void initializeVariableVector() {
        if (set.equals("C")) {
            this.variableVectorComplex = new ArrayList<>();
            for (int i = 0; i < cols; i++) {
                variableVectorComplex.add((Complex.ONE).negate());
            }
        } else {
            this.variableVector = new ArrayList<>();
            for (int i = 0; i < cols; i++) {
                variableVector.add(BigFraction.MINUS_ONE);
            }
        }
    }

    /**
     * String text contains equation with variable names
     * String text2 contains equation with the value of the variables
     */
    public void setVariablesInPivotColumns() {
        //each row of the matrix starting from the bottom
        String text = "";
        String text2 = "";

        if (set.equals("C")) {
            for (int i = rank - 1; i >= 0; i--) {
                solutionC = Complex.ZERO;
                text += " a" + subscripts.get(pivots.get(i)) + " = [";
                text2 += " a" + subscripts.get(pivots.get(i)) + " = [";

                //pivot element let be equal to the negation of the other elements sum
                for (int j = (pivots.get(i)) + 1; j < cols; j++) {
                    solutionC = solutionC.add((variableVectorComplex.get(j).multiply(coefficientMatrixComplex.get(i).get(j))).negate());
                    text += " ( " + new ComplexFormat().format(coefficientMatrixComplex.get(i).get(j).negate()) + " ) a" + subscripts.get(j);

                    text2 += " " + "[ (" + new ComplexFormat().format(coefficientMatrixComplex.get(i).get(j).negate()) + ") * (" + new ComplexFormat().format(variableVectorComplex.get(j)) + ") ]";
                    if (j + 1 < cols) {
                        text += " + ";
                        text2 += " + ";
                    }
                }
                text += " ] / (" + new ComplexFormat().format(coefficientMatrixComplex.get(i).get(pivots.get(i))) + ") ";
                text2 += " ] / (" + new ComplexFormat().format(coefficientMatrixComplex.get(i).get(pivots.get(i))) + ") ";
                //rounded Complex
                solutionC = roundComplex(solutionC.divide(coefficientMatrixComplex.get(i).get(pivots.get(i))),10);
                variableVectorComplex.set(pivots.get(i), solutionC);
                MEsteps.add(text);
                MEsteps.add(text2);
                MEsteps.add("a" + subscripts.get(pivots.get(i)) + " = " + new ComplexFormat().format(solutionC));

                text = "";
                text2 = "";
            }
        } else {
            for (int i = rank - 1; i >= 0; i--) {
                solution = BigFraction.ZERO;
                text += " a" + subscripts.get(pivots.get(i)) + " = [";
                text2 += " a" + subscripts.get(pivots.get(i)) + " = [";

                //pivot element let be equal to the negation of the other elements sum
                for (int j = (pivots.get(i)) + 1; j < cols; j++) {
                    solution = modulo(solution.add((variableVector.get(j).multiply(coefficientMatrix.get(i).get(j))).negate()));
                    text += " (" + coefficientMatrix.get(i).get(j).negate() + ") a" + subscripts.get(j);
                    text2 += " " + "[ (" + coefficientMatrix.get(i).get(j).negate() + ") * (" + variableVector.get(j) + ") ]";
                    if (j + 1 < cols) {
                        text += " + ";
                        text2 += " + ";
                    }
                }
                text += " ] / (" + coefficientMatrix.get(i).get(pivots.get(i)) + ") ";
                text2 += " ] / (" + coefficientMatrix.get(i).get(pivots.get(i)) + ") ";
                solution = modulo(solution.divide(coefficientMatrix.get(i).get(pivots.get(i))));
                variableVector.set(pivots.get(i), solution);
                MEsteps.add(text);
                MEsteps.add(text2);
                MEsteps.add("a" + subscripts.get(pivots.get(i)) + " = " + solution);

                text = "";
                text2 = "";
            }
        }
    }

    /**
     * To get the non-zero polynomial with the smallest degree:
     * Set the first variable to 1 where in the column isn't a pivot
     * Other non pivot column variables set to 0
     */
    public void setVariablesInNonPivotColumns() {
        if (set.equals("C")) {
            for (int i = 0; i < variableVectorComplex.size(); i++) {
                if (!pivots.contains(i)) {
                    variableVectorComplex.set(i, solutionC);
                    MEsteps.add("a" + subscripts.get(i) + " = " + new ComplexFormat().format(solutionC));
                    if (solutionC == Complex.ONE) {
                        solutionC = Complex.ZERO;
                    }
                }
            }
        } else {
            for (int i = 0; i < variableVector.size(); i++) {
                if (!pivots.contains(i)) {
                    variableVector.set(i, solution);
                    MEsteps.add("a" + subscripts.get(i) + " = " + solution);
                    if (solution == BigFraction.ONE) {
                        solution = BigFraction.ZERO;
                    }
                }
            }
        }

    }

    public void findPivotColumns() {
        for (int i = 0; i < rank; i++) {
            for (int j = 0; j < cols; j++) {
                if (set.equals("C")) {
                    if (!coefficientMatrixComplex.get(i).get(j).equals(Complex.ZERO)) {
                        pivots.add(j);
                        break;
                    }
                } else {
                    if (!coefficientMatrix.get(i).get(j).equals(BigFraction.ZERO)) {
                        pivots.add(j);
                        break;
                    }
                }
            }
        }
    }

    public BigFraction modulo(BigFraction number) {

        if (set.equals("Z2")) {
            BigFraction remainder = new BigFraction(Math.floorMod(number.intValue(), 2));
            System.out.println(number + " modulo " + set + " = " + remainder);
            return remainder;
        }

        if (set.equals("Z3")) {
            BigFraction remainder = new BigFraction(Math.floorMod(number.intValue(), 3));
            System.out.println(number + " modulo " + set + " = " + remainder);
            return remainder;
        }

        return number;
    }

    public ArrayList<BigFraction> getVariableVector() {
        return variableVector;
    }

    public ArrayList<Complex> getVariableVectorComplex() {
        return variableVectorComplex;
    }

    public ArrayList<String> getMEsteps() {
        return MEsteps;
    }

    public Complex roundComplex(Complex number, int scale){

        double realPart = number.getReal();
        double imaginaryPart = number.getImaginary();

        return new Complex(Precision.round(realPart,scale), Precision.round(imaginaryPart,scale));
    }


}
