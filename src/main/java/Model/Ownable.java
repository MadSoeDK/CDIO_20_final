package Model;

import java.awt.*;

public abstract class Ownable extends Field {

    private final String COLOR;
    private final int[] RENT;
    private final int PRICE;

    private Player owner;

    private int houses = 0;

    private boolean mortgage;
    private int mortgageRent = 0;

    public Ownable(String name, int placement, String color, int[] rent, int price) {
        super(name, placement);
        this.COLOR = color;
        this.RENT = rent;
        this.PRICE = price;
        this.mortgage = false;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }
    public Player getOwner() {
        return owner;
    }
    public String getColor() {
        return COLOR;
    }
    public int[] getRent() {
        return RENT;
    }
    public int getCurrentRent() {
        int rent;
        if (owner != null) {
            if (mortgage){
                return mortgageRent;
            }
            rent = RENT[houses];
        } else {
            rent = PRICE;
        }
        return rent;
    }
    public int getPrice() {
        return PRICE;
    }
    public int getHouses() {
        return houses;
    }
    public void changeOwner(Player player) {
        owner = null;
    }
    public void setMortage(Player player) {
        this.mortgage = true;
    }
}
