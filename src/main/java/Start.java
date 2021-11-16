import gui_fields.GUI_Start;
import gui_fields.GUI_Street;

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
}
