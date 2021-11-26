package Model;

import gui_fields.GUI_Start;

import java.awt.*;

/**
 * Child of Model.Field, Effect is controlled in Main.
 */
public class Start extends Field {
    public Start(GUI_Start field, String name, Color color, String description) {
        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setDescription("Passér start og modtag $2.");
        field.setSubText("");

        this.field = field;
        this.name = name;
        this.description = description;
    }
}