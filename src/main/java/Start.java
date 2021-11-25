import gui_fields.GUI_Start;

import java.awt.*;

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
