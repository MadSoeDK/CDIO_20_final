package Model;

import java.awt.Color;

public class Property extends Ownable {

    protected int rent;
    protected Color color;
    protected boolean mortgage;

    public Property (int rent, String name, Color color) {
        super(name,rent, color);
        this.mortgage = false;
    }

    public int getRent() {
        if(mortgage == true) {
            return 0;
        }
        return rent;
    }
    public String getName() {
        return name;
    }
    public void setMortgage(boolean status) {
        this.mortgage = status;
    }
    /*public void setOwner(Player player) {
        this.owner = player;
    }
    public Player getOwner() {
        return owner;
    }*/
    public Color getColor() {
        return color;
    }

}*/
