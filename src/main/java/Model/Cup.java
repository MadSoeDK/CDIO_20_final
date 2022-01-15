package Model;

/**
 * Represents a raflecup that contains dies.
 * Creates 1 die upon initialization
 */
public class Cup {

    private int sum;
    private final Die die1;
    private final Die die2;

    int[] facevalues;

    public Cup() {
        die1 = new Die();
        die2 = new Die();
    }
    public int getSum() {
        return sum;
    }
    public int[] getFacevalues() {
        return facevalues;
    }
    public void roll() {
        int fv1 = die1.roll();
        int fv2 = die2.roll();

        sum = fv1 + fv2;

        facevalues = new int[2];
        facevalues[0] = fv1;
        facevalues[1] = fv2;
    }
    public boolean getPair() {
        return facevalues[0] == facevalues[1];
    }
}