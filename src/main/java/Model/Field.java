package Model;

/**
 * Parent Model.Field Class
 */
public abstract class Field {
    protected String name;
    //protected GUI_Field field;
    protected String description;

    /*public GUI_Field getGUIField(){
        return field;
    }*/
    public String getName() {
        return name;
    }
}
