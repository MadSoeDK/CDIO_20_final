package Model;

import gui_fields.GUI_Shipping;

import java.awt.*;

public class Ferry extends Ownable {
    protected GUI_Shipping field;

    public Ferry(GUI_Shipping field) {
        this.field = field;
    }
}
