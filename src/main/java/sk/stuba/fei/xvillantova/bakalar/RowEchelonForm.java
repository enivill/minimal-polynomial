package sk.stuba.fei.xvillantova.bakalar;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Iterator;

public class RowEchelonForm {

    private ArrayList<ArrayList<BigFraction>> matrix;
    private ArrayList<ArrayList<Complex>> matrixComplex;
    private ArrayList<String> steps;
    private ArrayList<ArrayList<ArrayList<BigFraction>>> matrixTransformation;
    private ArrayList<ArrayList<ArrayList<Complex>>> matrixTransformationComplex;
    private int numRows;
    private int numCols;
    private int rank;
    private String set;

    static class Coordinate {
        int row;
        int col;

        Coordinate(int r, int c) {
            row = r;
            col = c;
        }
    }

    /**
     * Either matrix or either matrixComplex always will be null. Depends on which number set are we in.
     */
    RowEchelonForm(ArrayList<ArrayList<BigFraction>> matrix, ArrayList<ArrayList<Complex>> matrixComplex, String set) {
        this.matrix = matrix;
        this.matrixComplex = matrixComplex;
        this.set = set;
        steps = new ArrayList<>();

        if (set.equals("C")) {
            correctZeroesInComplexMatrix(8);
            this.numRows = matrixComplex.size();
            this.numCols = matrixComplex.get(0).size();
            matrixTransformationComplex = new ArrayList<>();
            addMatrixToTransformationArrayComplex();
        } else {
            this.numRows = matrix.size();
            this.numCols = matrix.get(0).size();
            matrixTransformation = new ArrayList<>();
            addMatrixToTransformationArray();
        }

        steps.add(" H =  ");

        int submatrix = 0;
        Coordinate pivot = new Coordinate(0, 0);

        for (int x = 0; x < numCols; x++) {

            pivot = new Coordinate(pivot.row, x);

            //Step1
            //Begin with the leftmost nonzero column. This is a pivot column. The pivot position is at the top.
            for (int i = x; i < numCols; i++) {
                if (isColumnZeroes(pivot, submatrix) == false) {
                    break;
                } else {
                    pivot.col = i;
                }
            }
            //Step 2
            //Select a nonzero entry in the pivot column with the highest absolute value as a pivot.
            pivot = findPivot(pivot);


            if (set.equals("C")) {
                if (getCoordinateComplex(pivot).equals(Complex.ZERO)/*(roundComplex(getCoordinateComplex(pivot), 2).abs()) == 0.0*/) {
                    pivot.row++;
                    continue;
                }
            } else {
                if (getCoordinate(pivot).equals(BigFraction.ZERO)) {
                    pivot.row++;
                    continue;
                }
            }

            //If necessary, interchange rows to move this entry into the pivot position.
            //move this row to the top of the submatrix
            if (pivot.row != submatrix) {
                interchange(new Coordinate(submatrix, pivot.col), pivot);
            }

            //If the pivot is -1, force it to be 1
            //Force pivot to be 1

            // I comment this section, because We dont want to force it to be 1,
            // because we wanna solve by Gaussian elimination method (not Gauss–Jordan elimination, which leads to reduced row echelon form)

           /* if (!getCoordinate(pivot).equals(BigFraction.ONE) || getCoordinate(pivot).equals(BigFraction.MINUS_ONE)) {
                BigFraction scalar = getCoordinate(pivot).reciprocal();
                scale(pivot, scalar);
            }*/

            //Step 3
            //Use row replacement operations to create zeroes in all positions below the pivot.
            //belowPivot = belowPivot + (Pivot * -belowPivot)
            for (int i = pivot.row; i < numRows; i++) {
                if (i == pivot.row) {
                    continue;
                }
                Coordinate belowPivot = new Coordinate(i, pivot.col);

                if (set.equals("C")) {
                    //(roundComplex(getCoordinateComplex(belowPivot), 2).abs()) == 0.0
                    if (getCoordinateComplex(belowPivot).equals(Complex.ZERO)) {
                        continue;
                    }

                    Complex complement = getCoordinateComplex(belowPivot).negate().divide(getCoordinateComplex(pivot));
                    multiplyAndAddComplex(belowPivot, pivot, complement);
                } else {
                    if (getCoordinate(belowPivot).equals(BigFraction.ZERO)) {
                        continue;
                    }
                    BigFraction complement = (getCoordinate(belowPivot).negate().divide(getCoordinate(pivot)));
                    multiplyAndAdd(belowPivot, pivot, complement);
                }

            }
            //Step 4
            //Ignore the row containing the pivot position and cover all rows, if any, above it.
            //Apply steps 1-3 to the remaining submatrix. Repeat until there are no more nonzero entries.

            if ((pivot.row + 1) >= numRows) {
                break;
            }
            submatrix++;
            pivot.row++;

        }

        if(set.equals("C")){
            correctZeroesInComplexMatrix(8);
        }

        removeZeroRows();

        this.rank = findRank();

//        if (set.equals("C")) {
//            System.out.println("REF______\n" + this.toStringComplex());
//            System.out.println("MATRIX_TRANSFORMATION_______\n" + this.toStringTransMatricesComplex());
//            System.out.println("STEPS_______\n" + this.getSteps());
//        }

/*
        [[(2.0, 3.0), (0.0, 0.0), (0.0, 8.0)]
[(4.0, 2.0), (-5.0, -4.0), (9.0, 5.0)]
[(2.0, 0.0), (0.0, 1.0), (7.0, 4.0)]]
*/

    }

    public void addMatrixToTransformationArray() {

        ArrayList<ArrayList<BigFraction>> copy = new ArrayList<>();
        Iterator<ArrayList<BigFraction>> iterator = matrix.iterator();
        while (iterator.hasNext()) {
            copy.add((ArrayList<BigFraction>) iterator.next().clone());
        }
        matrixTransformation.add(copy);
    }

    public void addMatrixToTransformationArrayComplex() {

        ArrayList<ArrayList<Complex>> copy = new ArrayList<>();
        Iterator<ArrayList<Complex>> iterator = matrixComplex.iterator();
        while (iterator.hasNext()) {
            copy.add((ArrayList<Complex>) iterator.next().clone());
        }
        matrixTransformationComplex.add(copy);
    }

    public void multiplyAndAdd(Coordinate to, Coordinate from, BigFraction scalar) {
        ArrayList<BigFraction> row = matrix.get(to.row);
        ArrayList<BigFraction> rowMultiplied = matrix.get(from.row);

        for (int i = 0; i < numCols; i++) {
            row.set(i, modulo(row.get(i).add((rowMultiplied.get(i).multiply(scalar)))));
        }
        steps.add(" " + scalar + " * R" + (from.row+1) + " + R" + (to.row+1) + " -> " + " R" + (to.row+1) + " ");
        addMatrixToTransformationArray();
    }

    public void multiplyAndAddComplex(Coordinate to, Coordinate from, Complex scalar) {
        ArrayList<Complex> row = matrixComplex.get(to.row);
        ArrayList<Complex> rowMultiplied = matrixComplex.get(from.row);

        for (int i = 0; i < numCols; i++) {
            //we also round the complex number after multiplying
            Complex roundedComplexNumber = roundComplex(row.get(i).add(rowMultiplied.get(i).multiply(scalar)), 12);
            row.set(i, roundedComplexNumber);
        }
        steps.add(" (" + new ComplexFormat().format(roundComplex(scalar, 5)) + ") * R" + (from.row+1) +" + R" + (to.row+1) + " -> " + " R" + (to.row+1) + " ");
        correctZeroesInComplexMatrix(8);
        addMatrixToTransformationArrayComplex();
    }

    public void correctZeroesInComplexMatrix( int scale){

        for (ArrayList<Complex> row : matrixComplex) {
            for (Complex number: row) {
                if(roundComplex(number,scale).abs()==0){
                    row.set(row.indexOf(number), new Complex(0,0));
                }
            }
        }
    }

    public boolean isColumnZeroes(Coordinate a, int submatrix) {
        for (int i = submatrix; i < numRows; i++) {
            if (set.equals("C")) {
                //!(matrixComplex.get(i).get(a.col)).equals(Complex.ZERO)
                if ((roundComplex(matrixComplex.get(i).get(a.col), 12).abs()) != 0.0) {
                    return false;
                }
            } else {
                if (!matrix.get(i).get(a.col).equals(BigFraction.ZERO)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Coordinate findPivot(Coordinate a) {

        Coordinate pivot = new Coordinate(a.row, a.col);
        Coordinate current = new Coordinate(a.row, a.col);
        boolean noPivot = true;


        for (int i = a.row; i < numRows; i++) {
            current.row = i;
            if (set.equals("C")) {
                if ((getCoordinateComplex(current).equals(Complex.ONE) || getCoordinateComplex(current).equals((Complex.ONE).negate())) && (current.row == a.row)) {
                    noPivot = false;
                    break;
                }
                if (getCoordinateComplex(current).equals(Complex.ONE) || getCoordinateComplex(current).equals((Complex.ONE).negate())) {
                    interchange(current, a);
                    noPivot = false;
                    break;
                }
            } else {
                if ((getCoordinate(current).equals(BigFraction.ONE) || getCoordinate(current).equals(BigFraction.MINUS_ONE)) && (current.row == a.row)) {
                    noPivot = false;
                    break;
                }
                if (getCoordinate(current).equals(BigFraction.ONE) || getCoordinate(current).equals(BigFraction.MINUS_ONE)) {
                    interchange(current, a);
                    noPivot = false;
                    break;
                }
            }
        }
        if (noPivot) {
            current.row = a.row;
            for (int i = current.row; i < numRows; i++) {
                current.row = i;
                if (set.equals("C")) {
                    //!getCoordinateComplex(current).equals(Complex.ZERO)
                    if ((roundComplex(getCoordinateComplex(current), 12).abs()) != 0.0) {
                        pivot.row = i;
                        break;
                    }
                } else {
                    if (!getCoordinate(current).equals(BigFraction.ZERO)) {
                        pivot.row = i;
                        break;
                    }
                }
            }
        }
        return pivot;
    }

    public BigFraction getCoordinate(Coordinate a) {
        return matrix.get(a.row).get(a.col);
    }

    public Complex getCoordinateComplex(Coordinate a) {
        return matrixComplex.get(a.row).get(a.col);
    }

    public void interchange(Coordinate a, Coordinate b) {

        if (set.equals("C")) {
            ArrayList<Complex> temp = matrixComplex.get(a.row);
            matrixComplex.set(a.row, matrixComplex.get(b.row));
            matrixComplex.set(b.row, temp);
        } else {
            ArrayList<BigFraction> temp = matrix.get(a.row);
            matrix.set(a.row, matrix.get(b.row));
            matrix.set(b.row, temp);
        }

        int t = a.row;
        a.row = b.row;
        b.row = t;

        steps.add(" R" + (a.row+1) + " <-> " + "R" + (b.row+1) + " ");

        if (set.equals("C")) {
            addMatrixToTransformationArrayComplex();
        } else {
            addMatrixToTransformationArray();
        }
    }

    public void scale(Coordinate x, BigFraction d) {
        ArrayList<BigFraction> row = matrix.get(x.row);
        for (int i = 0; i < numCols; i++) {
            row.set(i, modulo(row.get(i).multiply(d)));
        }
        steps.add(" " + d + " * R" + (x.row+1) + " -> " + " R" + (x.row+1) + " ");
        addMatrixToTransformationArray();
    }



    public boolean isRowZeroes(Coordinate a) {
        for (int i = 0; i < numCols; i++) {
            if (set.equals("C")) {
                if (!matrixComplex.get(a.row).get(i).equals(Complex.ZERO)) {
                    return false;
                }
            } else {
                if (!matrix.get(a.row).get(i).equals(BigFraction.ZERO)) {
                    return false;
                }
            }

        }
        return true;
    }

    public void removeZeroRows() {
        for (int i = 0; i < numRows; i++) {
            Coordinate cord = new Coordinate(i, 0);
            if (isRowZeroes(cord)) {
                if (set.equals("C")) {
                    matrixComplex.remove(cord.row);
                } else {
                    matrix.remove(cord.row);
                }
                this.setNumRows(numRows - 1);
                i--;
            }
        }

      /*  if(set.equals("C")){
            addMatrixToTransformationArrayComplex();

        } else {
            addMatrixToTransformationArray();
        }
*/
    }

    public int findRank() {
        int count = 0;

        for (int i = 0; i < numRows; i++) {
            Coordinate cord = new Coordinate(i, 0);
            if (!isRowZeroes(cord)) {
                count++;
            }
        }
        return count;
    }

    public BigFraction modulo(BigFraction number) {

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

    public Complex roundComplex(Complex number, int scale) {

        double realPart = number.getReal();
        double imaginaryPart = number.getImaginary();

        return new Complex(Precision.round(realPart, scale), Precision.round(imaginaryPart, scale));
    }

    public String toString() {
        return matrix.toString().replace("], ", "]\n");
    }

    public String toStringComplex() {
        return matrixComplex.toString().replace("], ", "]\n");
    }

    public String toStringTransMatricesComplex() {
        return this.matrixTransformationComplex.toString().replace("], ", "]\n").replace("]]", "]]\n").replace("[[[", "[\n[[");
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public ArrayList<ArrayList<BigFraction>> getMatrix() {
        return matrix;
    }

    public ArrayList<ArrayList<Complex>> getMatrixComplex() {
        return matrixComplex;
    }

    public int getRank() {
        return rank;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public ArrayList<ArrayList<ArrayList<BigFraction>>> getMatrixTransformation() {
        return matrixTransformation;
    }

    public ArrayList<ArrayList<ArrayList<Complex>>> getMatrixTransformationComplex() {
        return matrixTransformationComplex;
    }
}
