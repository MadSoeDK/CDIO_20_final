package Model;

import Model.Ownable;

import java.awt.*;

public class Tax extends Ownable {
    int tax;

    public Tax(int tax) {
       /*field.setTitle("SKAT");
        field.setBackGroundColor(Color.white);
        field.setDescription("Betal indkomstskat:");*/
        this.tax = tax;
        //this.field = field;
    }
    public int getTax() {
        return tax;
    }

}
