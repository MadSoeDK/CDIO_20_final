package Model;

import java.util.Random;

/**
 * Produces a random int within 1-6.
 */
public class Die {

    // Initialize Random Object
    Random rand = new Random();

    // Initialize Variables
    private final int MAXVALUE = 6;

    public int getFacevalue() {
        return rand.nextInt(MAXVALUE) + 1;
    }
}
