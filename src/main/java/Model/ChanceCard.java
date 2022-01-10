package Model;

/**
 * Is used within chancecard-array for multiple effects.
 */
public class ChanceCard {

    private String description;
    private String name;
    private int number;
    private String type;

    public ChanceCard(String description, String name, int number, String type) {
        this.description = description;
        this.name = name;
        this.number = number;
        this.type = type;
    }
    public int getNumber() {
        return number;
    }
}
