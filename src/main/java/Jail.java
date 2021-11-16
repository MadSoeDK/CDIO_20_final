import gui_fields.GUI_Jail;
import gui_fields.GUI_Start;

import java.awt.Color;

public class Jail extends Field {
    public Jail(GUI_Jail field, String name, Color color, String description) {
        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setDescription("Passér start og modtag $2.");
        field.setSubText("");

        this.field = field;
        this.name = name;
        this.description = description;
        this.fieldType = 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getType() {
        return fieldType;
    }

    @Override
    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public Player getOwner() {
        return null;
    }
}
