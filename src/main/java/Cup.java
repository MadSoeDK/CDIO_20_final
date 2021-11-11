/**
 * Represents a raflecup that contains dies.
 * Creates 2 dies upon initialization
 */
public class Cup {

    private int sum;

    Die Die1;
    Die Die2;

    public Cup() {
        Die1 = new Die();
        Die2 = new Die();
    }

    public int getSum() {
        sum = Die1.getFacevalue() + Die2.getFacevalue();
        return sum;
    }

    public boolean getPair() {
        if (Die1.getFacevalue() == Die2.getFacevalue()) {
            return true;
        } else {
            return false;
        }
    }
}