package Model.Cards;

/**
 * Is used within chancecard-array for multiple effects.
 */
public abstract class ChanceCard {

    private final String DESCRIPTION;
    private final String TYPE;
    private final int VALUE;

    public ChanceCard(String description, String type, int value) {
        this.VALUE = value;
        this.DESCRIPTION = description;
        this.TYPE = type;
    }
    public String getDescription() {
        return DESCRIPTION;
    }
    public String getType() {
        return TYPE;
    }
    public int getValue() {
        return VALUE;
    }
}
