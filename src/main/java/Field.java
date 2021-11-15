import gui_fields.GUI_Field;
import gui_fields.GUI_Jail;
import gui_main.GUI;

public abstract class Field {
    protected String name;
    protected int fieldType;

    /*public Field(String name, int fieldType) {

    }
*/
    public abstract String getName();
    public abstract int getType();

}
