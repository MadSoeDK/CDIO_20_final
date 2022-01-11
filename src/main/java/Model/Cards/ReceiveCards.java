package Model.Cards;
import Model.*;

public class ReceiveCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int value;

    public ReceiveCards(String description, String name, String type, int value) {
        super(description, name, type, value);
        this.value = value;
        this.description = description;
        this.name = name;
        this.type = type;
    }
    public int receive(Player player) {
        player.setPlayerBalance(value);
        return value;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getType() {
        return description;
    }
    public int getValue() {
        return value;
    }
}
