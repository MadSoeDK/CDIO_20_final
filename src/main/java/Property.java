import gui_fields.GUI_Street;
import java.awt.Color;

/**
 * Child of Field, Used in field array, ownable game fields.
 */
public class Property extends Field {

    // Variables
    protected int rent;
    protected Player owner;
    protected Color color;

    /**
     * Constructor
     * @param field
     * @param rent
     * @param name
     * @param color
     */

    // Constructor
    public Property(GUI_Street field, int rent, String name, Color color/*, String description*/) {
        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setSubText("$" + rent);
        field.setDescription("My description");

        this.field = field;
        this.rent = rent;
        this.name = name;
        this.color = color;
    }

    // Methods
    public int getRent() {
        return rent;
    }
    public void setOwner(Player player) {
        this.owner = player;
    }
    public Player getOwner() {
        return owner;
    }
    public Color getColor() {
        return color;
    }
}
