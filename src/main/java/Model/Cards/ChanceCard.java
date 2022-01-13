package Model.Cards;

/**
 * Is used within chancecard-array for multiple effects.
 */
public abstract class ChanceCard {

    private String description;
    private String type;
    private int value;

    public ChanceCard(String description, String type, int value) {
        this.description = description;
        this.type = type;
        this.value = value;
    }
    public abstract String getDescription();
    public abstract String getType();
    public abstract int getValue();
}
