package Model.Cards;
import Model.*;

public class ReceiveCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int amount;
    private Player player;

    public ReceiveCards(String description, String name, String type, int amount) {
        super(description, name, type, amount);
    }
    public void receive() {
        player.setPlayerBalance(amount);
    }
}
