package Model;

import gui_fields.GUI_Street;
import java.awt.Color;

/**
 * Child of Model.Field, Used in field array, ownable game fields.
 */
public class Property extends Ownable {

    // Variables
    protected int rent;
    protected Color color;
    protected boolean mortgage;

    /**
     * Constructor
     * @param field
     * @param rent
     * @param name
     * @param color
     */

    // Constructor
    public Property (GUI_Street field, int rent, String name, Color color/*, String description*/) {
        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setSubText("$" + rent);
        field.setDescription("My description");


        this.field = field;
        this.rent = rent;
        this.name = name;
        this.color = color;
        this.mortgage = false;
    }

    // Methods
    public int getRent() {
        if(mortgage) {
            return 0;
        }
        return rent;
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
}
