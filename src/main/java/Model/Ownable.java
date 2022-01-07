package Model;

import java.awt.*;

public abstract class Ownable extends Field {

    private Player owner;

    private Color color;
    private int[] rent;
    private int price;

    public Ownable() {

    }

    public void setOwner(Player player) {
        this.owner = player;
    }
    public Player getOwner() {
        return owner;
    }
}
