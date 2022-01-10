package Model.Cards;
import Model.*;

public class MoveCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int value;
    private Player player;

    public MoveCards(String description, String name, String type, int value) {
        super(description, name, type, value);
    }
    public void move() {
        player.setPlacement(value);
    }
}
