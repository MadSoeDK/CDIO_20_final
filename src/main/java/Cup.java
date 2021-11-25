/**
 * Represents a raflecup that contains dies.
 * Creates 1 die upon initialization
 */
public class Cup {

    private int sum;

    Die Die1;
    //Die Die2;

    public Cup() {
        Die1 = new Die();
        //Die2 = new Die();
    }

    public int getSum() {
        return sum;
    }

    /*public boolean getPair() {
        if (Die1.getFacevalue() == Die2.getFacevalue()) {
            return true;
        } else {
            return false;
        }
    }*/

    public void roll() {
        sum = Die1.getFacevalue();
    }
}