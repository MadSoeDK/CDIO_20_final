/**
 * Is used within chancecard-array for multiple effects.
 */
public class ChanceCard {

    private String description;
    private String name;
    private int number;

    public ChanceCard(String description, String name, int number) {
        this.description = description;
        this.name = name;
        this.number = number;
    }
    public int getNumber() {
        return number;
    }
}
