import gui_fields.GUI_Chance;
import gui_fields.GUI_Jail;
import java.awt.*;
//import java.awt.Color;
public class ChanceField extends Field{

    public ChanceField(GUI_Chance field) {
        field.setBackGroundColor(Color.white);
        field.setDescription("Træk et kort");
        this.field = field;
    }

}
