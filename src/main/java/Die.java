import java.util.Random;

public class Die {

    // Initialize Random Object
    Random rand = new Random();

    // Initialize Variables
    private final int MAXVALUE = 6;
    private int facevalue;

    public int getFacevalue() {
        facevalue = rand.nextInt(MAXVALUE) + 1;
        // System.out.println(facevalue);
        return facevalue;
    }
}
