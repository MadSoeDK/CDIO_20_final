import gui_fields.GUI_Field;

/**
 * Parent Field Class
 */
public abstract class Field {
    protected String name;
    protected GUI_Field field;
    protected String description;

    public GUI_Field getGUIField(){
        return field;
    }
}
