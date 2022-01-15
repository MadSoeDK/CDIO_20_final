package Model;

import java.util.Random;

/**
 * Provides method to produce a random int within 1-6.
 */
public class Die {

    private Random rand = new Random();
    private final int MAXVALUE = 6;

    public int roll() {
        return rand.nextInt(MAXVALUE) + 1;
    }
}
