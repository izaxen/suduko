package suduko.models;

public class ValidateCells {

    private int xPosition;
    private int yPosition;
    private int numberToValidate;
    private boolean correctNumber;

    public ValidateCells(int xPosition, int yPosition, int numberToValidate, boolean correctNumber) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.numberToValidate = numberToValidate;
        this.correctNumber = correctNumber;
    }

    public ValidateCells(int xPosition, int yPosition, int numberToValidate) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.numberToValidate = numberToValidate;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public boolean isCorrectNumber() {
        return correctNumber;
    }

    public void setCorrectNumber(boolean correctNumber) {
        this.correctNumber = correctNumber;
    }

    public int getNumberToValidate() {
        return numberToValidate;
    }

    public void setNumberToValidate(int numberToValidate) {
        this.numberToValidate = numberToValidate;
    }
}
