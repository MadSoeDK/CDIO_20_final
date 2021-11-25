import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {
    Die die = new Die();

    @Test
    void getFacevalue() {
        float[] counter = new float[6];
        for (int i = 0; i < 50000; i++) {
            int facevalue = die.getFacevalue();
            switch (facevalue) {
                case 1:
                    counter[0]++;
                    break;
                case 2:
                    counter[1]++;
                    break;
                case 3:
                    counter[2]++;
                    break;
                case 4:
                    counter[3]++;
                    break;
                case 5:
                    counter[4]++;
                    break;
                case 6:
                    counter[5]++;
                    break;
                default:
                    assertTrue(facevalue >=1 && facevalue <=6, "Value not between 1-6");
            }
        }
        for (float v : counter) {
            assertEquals(8300, v, 500, "Facevalue not between accepted delta (1%)");
            System.out.println(v + " - " + v / 500 + "%");
        }
    }
}