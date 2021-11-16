import gui_fields.GUI_Street;
import java.awt.Color;

/**
 *
 */
public class Property extends Field {
    protected Color color;
    protected int rent;
    protected Player owner;

    /**
     * Constructor
     * @param field
     * @param rent
     * @param name
     * @param color
     */
    public Property(GUI_Street field, int rent, String name, Color color/*, String description*/) {
        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setSubText("$" + rent);
        field.setDescription("My description");

        this.field = field;
        this.rent = rent;
        this.name = name;
        this.fieldType = 3;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getType() {
        return fieldType;
    }
    public int getRent() {
        return rent;
    }
    public void setOwner(Player player) {
        this.owner = player;
    }
    @Override
    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }
    public Player getOwner() {
        return owner;
    }


}
