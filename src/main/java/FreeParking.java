import gui_fields.GUI_Start;
import gui_fields.GUI_Tax;

import java.awt.*;

public class FreeParking extends Field {

    public static int balance;

    public FreeParking(GUI_Tax field, int balance,String name, Color color, String description) {
        field.setTitle(name);
        field.setBackGroundColor(color);
        field.setDescription("Modtag penge ved at lande på dette felt");
        field.setSubText("Modtag: "+String.valueOf(balance));

        this.field = field;
        this.name = name;
        this.description = description;
        this.balance=balance;
    }

    public static void setBalance(int value){
        balance=balance+value;
    }
    public static int getBalance(){
        return balance;
    }
    public static void resetBalance(int value){
        balance=0;
    }
    public void updateGUIBalance(){
        field.setSubText("Modtag: "+String.valueOf(balance));
    }
}