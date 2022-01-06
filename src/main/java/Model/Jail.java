package Model;

import gui_fields.GUI_Jail;

import java.awt.Color;

/**
 * Child of Model.Field, Sends players back and increments Model.FreeParking
 */
public class Jail extends Field {

    protected int rent;

    public Jail(int rent, String name, Color color) {

        /*field.setTitle(name);
        field.setBackGroundColor(color);
        field.setDescription(name);
        field.setSubText(name);

        this.field = field;*/
        this.name = name;
        this.rent = rent;
    }

    public int getRent() {
        return rent;
    }
}
