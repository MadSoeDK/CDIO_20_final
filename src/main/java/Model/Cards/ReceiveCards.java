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
    public int receiveLegation(Player player) {
        if((!(player.getNetWorth() > 15000))) {
            value = 0;
            player.setPlayerBalance(value);
        }
        return value;
    }
    public int receiveFromPlayers(Player player, Player[] players) {
        for(int i = 0; i < players.length; i++) {
            if(players[i] != player) {
                players[i].setPlayerBalance(-value);
            }
        }
        value = value * players.length;
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
