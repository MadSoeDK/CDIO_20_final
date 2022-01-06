package Model;

import gui_fields.GUI_Chance;
import gui_fields.GUI_Jail;
import java.awt.*;
//import java.awt.Color;

/**
 * Is used within the Fieldarray, is used to draw a chance card when landed on.
 */
public class ChanceField extends Field {


    public ChanceField(GUI_Chance field) {
        field.setBackGroundColor(Color.white);
        field.setDescription("Træk et kort");
        this.field = field;
    }

}
