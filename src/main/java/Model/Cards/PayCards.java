package Model.Cards;
import Model.Player;

public class PayCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int amount;
    private Player player;

    public PayCards(String description, String name, String type, int amount) {
        super(description, name, type, amount);
        this.player = player;
    }
    public void pay() {
        player.setPlayerBalance(-amount);
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
