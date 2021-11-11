import java.util.Random;

public class Die {

    // Initialize Random Object
    Random rand = new Random();

    // Initialize Variables
    private final int MAXVALUE = 6;

    public int getFacevalue() {
        return rand.nextInt(MAXVALUE) + 1;
    }


}
