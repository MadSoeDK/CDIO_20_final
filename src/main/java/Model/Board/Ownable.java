package Model.Board;

import Model.Player;

public abstract class Ownable extends Field {

    private final String COLOR;
    private final int[] RENT;
    private final int PRICE;

    private Player owner;

    private boolean mortgage;

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
                return 0;
            }
            rent = RENT[0];
        } else {
            rent = PRICE;
        }
        return rent;
    }
    public int getPrice() {
        return PRICE;
    }
    public void changeOwner(Player player) {
        owner = null;
    }
    public void setMortgage(boolean value) {
        this.mortgage = value;
    }
    public boolean getMortgage() {
        return mortgage;
    }
}
