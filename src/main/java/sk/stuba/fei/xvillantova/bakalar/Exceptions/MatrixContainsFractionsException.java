package sk.stuba.fei.xvillantova.bakalar.Exceptions;

public class MatrixContainsFractionsException extends Exception {
    public MatrixContainsFractionsException() {
    }

    public MatrixContainsFractionsException(String message) {
        super(message);
    }

    public MatrixContainsFractionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatrixContainsFractionsException(Throwable cause) {
        super(cause);
    }
}
