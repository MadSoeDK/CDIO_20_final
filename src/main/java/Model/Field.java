package Model;

import gui_fields.GUI_Field;

/**
 * Parent Model.Field Class
 */
public abstract class Field {
    protected String name;
    protected GUI_Field field;
    protected String description;

    public GUI_Field getGUIField(){
        return field;
    }
}
