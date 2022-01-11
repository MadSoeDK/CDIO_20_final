package Model.Cards;
import Model.*;

public class MoveCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int value;

    public MoveCards(String description, String name, String type, int value) {
        super(description, name, type, value);
        this.value = value;
        this.description = description;
        this.name = name;
        this.type = type;
    }
    public int move(Player player) {
        type = "move";
        if(value > -1) {
            player.setPlacement(+value);
        } else if (value < 0) {
            if(player.getPlacement() < 0) {
                value = player.getPlacement() - value;
                player.setPlacement(value);
            } else {
                player.setPlayerBalance(-value);
            }
        }
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
