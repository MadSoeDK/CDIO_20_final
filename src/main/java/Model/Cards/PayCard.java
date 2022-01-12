package Model.Cards;
import Model.Player;

public class PayCard extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int value;

    public PayCard(String description, String name, String type, int value) {
        super(description, name, type, value);
        this.value = value;
        this.description = description;
        this.name = name;
        this.type = type;
    }
    public int pay(Player player) {
        player.setPlayerBalance(-value);
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
