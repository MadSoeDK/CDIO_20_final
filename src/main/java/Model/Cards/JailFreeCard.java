package Model.Cards;
import Model.*;

public class JailFreeCard extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int value;

    public JailFreeCard(String description, String name, String type, int value) {
        super(description, name, type, value);
        this.value = value;
        this.description = description;
        this.name = name;
        this.type = type;
    }
    /*
    We store this in a boolean 'hasJailFreeCard'
    In options when in jail, choose
     */
    public void setPlayerFree(Player player) {
        player.setHasJailFreeCard(true);
    }
    public String getName() {
        return name;
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
