package Model;

import java.awt.*;

public abstract class Ownable extends Field {

    protected Player owner;
    protected Color color;

    public void setOwner(Player player) {
        this.owner = player;
    }
    public Player getOwner() {
        return owner;
    }/*
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
*/
}
