/**
 * Represents a raflecup that contains dies.
 * Creates 2 dies upon initialization
 */
public class Cup {

    private final int QUANTITY = 2;
    private int sum;
    private int pair;

    private int facevalue1;
    private int facevalue2;

    Die Die1;
    Die Die2;

    public Cup() {
        Die1 = new Die();
        Die2 = new Die();
    }

    public int getSum() {
        facevalue1 = Die1.getFacevalue();
        facevalue2 = Die2.getFacevalue();
        sum = facevalue1 + facevalue2;
        return sum;
    }

    public int getFacevalue1() {
        return facevalue1;
    }

    public int getFacevalue2() {
        return facevalue2;
    }

    public int getPair() {
        if (facevalue1 == facevalue2) {
            pair = 1;
        } else {
            pair = 0;
        }
        return pair;
    }
}