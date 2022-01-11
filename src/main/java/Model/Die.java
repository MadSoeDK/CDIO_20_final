package Model;

import java.util.Random;

/**
 * Produces a random int within 1-6.
 */
public class Die {

    // Initialize Random Object
    private Random rand = new Random();
    private int facevalue;
    private final int MAXVALUE = 6;

    public int roll() {
        return facevalue = rand.nextInt(MAXVALUE) + 1;
    }
    public int getFacevalue() {
        return facevalue;
    }
}
