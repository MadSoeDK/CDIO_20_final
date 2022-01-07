package Model;

import gui_fields.GUI_Jail;

import java.awt.Color;

/**
 * Child of Model.Field, Sends players back and increments Model.FreeParking
 */
public class Jail extends Field {

    protected int rent;

    public Jail(int rent, String name) {
        this.name = name;
        this.rent = rent;
    }

    public int getRent() {
        return rent;
    }
}
