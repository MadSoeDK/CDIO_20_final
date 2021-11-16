import gui_fields.GUI_Jail;

import java.awt.Color;

public class Jail extends Field {

    protected int rent;

    public Jail(GUI_Jail field, int rent, String name, Color color) {

        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setDescription(name);
        field.setSubText(name);

        this.field = field;
        this.name = name;
        this.rent = rent;
    }

    public int getRent() {
        return rent;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
