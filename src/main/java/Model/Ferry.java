package Model;

import gui_fields.GUI_Shipping;
import gui_fields.GUI_Street;

import java.awt.*;

public class Ferry extends Ownable {
    int rent=500;
    String name="Scandlines";
    /**
     * Constructor
     *
     * @param field
     * @param //rent
     * @param //name
     * @param //color
     */

    public Ferry(GUI_Shipping field, int rent, String name, Color color) {
        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setSubText("$" + rent);
        field.setDescription("My description");

        this.field = field;
        this.rent = rent;
        this.name = name;
        this.color = color;
    }

    public int getRent()
    {
        // Check owner of other ferries
        //if ()
        return rent;
    }
    public void setRent(int newRent) {
        rent=newRent;
    }
}
