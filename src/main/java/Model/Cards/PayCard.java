package Model.Cards;
import Model.Player;

public class PayCard extends ChanceCard {
    private String description;
    private String type;
    private int value;

    public PayCard(String description, String type, int value) {
        super(description, type, value);
        this.value = value;
        this.description = description;
        this.type = type;
    }
    public String getDescription() {
        return description;
    }
    public String getType() {
        return type;
    }
    public int getValue() {
        return value;
    }
}
