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
        if(type.equals("frederiksberg")) {
            player.setPlacement(value);
        } else if(value > -1 && value != 0) {
            value = player.getPlacement() + value;
            player.setPlacement(value);
        }
        else if(value == 0) {
            player.setPlacement(0);
        }
        else if(value < 0 && player.getPlacement() < 5) {
            value = player.getPlacement() + 40 + value;
            player.setPlacement(value);
        }
        else {
            value = player.getPlacement() + value;
            player.setPlayerBalance(value);
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
