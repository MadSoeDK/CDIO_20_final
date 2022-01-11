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
    }
    public String getName() {
        return name;
    }
    public int move(Player player) {
        player.setPlacement(value);
        return value;
    }
}
