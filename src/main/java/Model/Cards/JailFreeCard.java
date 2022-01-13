package Model.Cards;
import Model.*;

public class JailFreeCard extends ChanceCard {
    private String description;
    private String type;
    private int value;

    public JailFreeCard(String description, String type, int value) {
        super(description, type, value);
        this.value = value;
        this.description = description;
        this.type = type;
    }
    /*
    We store this in a boolean 'hasJailFreeCard'
    In options when in jail, choose
     */
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
