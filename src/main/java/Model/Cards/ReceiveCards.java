package Model.Cards;
import Model.*;

public class ReceiveCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int amount;

    public ReceiveCards(String description, String name, String type, int amount) {
        super(description, name, type, amount);
        this.amount = amount;
        this.description = description;
        this.name = name;
        this.type = type;
    }
    public int receive(Player player) {
        player.setPlayerBalance(amount);
        return amount;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
}
