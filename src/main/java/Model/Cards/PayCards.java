package Model.Cards;
import Model.Player;

public class PayCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int amount;

    public PayCards(String description, String name, String type, int amount) {
        super(description, name, type, amount);
        this.amount = amount;
        this.description = description;
        this.name = name;
        this.type = type;
    }
    public int pay(Player player) {
        player.setPlayerBalance(-amount);
        return amount;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    /*
    public int payAmount() {
        int value = 0;
        switch(name) {
            case "Øl":
                value = 200;
                break;
            case "Reparation":
                value = 3000;
                break;

        }
    }*/
}
