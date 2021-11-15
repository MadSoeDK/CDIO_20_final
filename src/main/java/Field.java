import gui_fields.GUI_Field;
import gui_fields.GUI_Jail;
import gui_fields.GUI_Street;

public abstract class Field {
    protected String name;
    protected int fieldType;
    protected GUI_Field field;
    protected int rent;
    protected Player owner;

    /*public Field(String name, int fieldType) {

    }
*/
    public abstract String getName();
    public abstract int getType();
    public abstract int getRent();
    public abstract void setOwner(Player owner);

}
