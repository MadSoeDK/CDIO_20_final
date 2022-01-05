package Model;

import gui_fields.GUI_Shipping;

import java.awt.*;

public class Ferry extends Ownable {
    public Ferry(GUI_Shipping field) {
        field.setBackGroundColor(Color.white);
        field.setDescription("Træk et kort");
        this.field = field;
    }
}
