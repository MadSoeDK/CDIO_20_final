import gui_fields.GUI_Jail;

import java.awt.Color;

public class Jail extends Field {

    public Jail(GUI_Jail field, String name, Color color) {

        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setDescription("Gå i fængsel");
        field.setSubText("Gå i fængsel");

        this.field = field;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
