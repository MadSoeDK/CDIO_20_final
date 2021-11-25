/**
 * Represents a raflecup that contains dies.
 * Creates 1 die upon initialization
 */
public class Cup {

    private int sum;
    Die Die1;

    public Cup() {
        Die1 = new Die();
    }
    public int getSum() {
        return sum;
    }
    public void roll() {
        sum = Die1.getFacevalue();
    }
}