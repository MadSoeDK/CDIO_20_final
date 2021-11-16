import gui_fields.GUI_Chance;
import java.awt.Color;

public class ChanceField extends Field {
    protected Card[] card;

    public ChanceField(GUI_Chance field, Card card, String name, Color color) {
        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setDescription("My description");

        this.field = field;
        this.name = name;
        this.fieldType = 3;
    }


}
