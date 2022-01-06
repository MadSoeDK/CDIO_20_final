package Model;

import Model.Ownable;
import gui_fields.GUI_Tax;

import java.awt.*;

public class Tax extends Ownable {
    int tax;

    public Tax(GUI_Tax field, int tax) {
        field.setTitle("SKAT");
        field.setBackGroundColor(Color.white);
        field.setDescription("Betal indkomstskat:");
        this.tax = tax;
        this.field = field;
    }
    public int getTax() {
        return tax;
    }

}
