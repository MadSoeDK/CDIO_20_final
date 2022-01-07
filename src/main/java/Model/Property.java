package Model;

import java.awt.Color;

public class Property extends Ownable {

    protected int rent;
    protected Color color;

    public Property (int rent, String name, Color color) {
        this.rent = rent;
        this.name = name;
        this.color = color;
    }

    public int getRent() {
        return rent;
    }
    public Color getColor() {
        return color;
    }

}
