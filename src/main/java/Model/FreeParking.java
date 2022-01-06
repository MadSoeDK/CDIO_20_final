package Model;

import gui_fields.GUI_Tax;

import java.awt.*;

/**
 * Child of Model.Field, Is used within field array, Tracks balance and gives to player landing on it.
 */
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
    public static void resetBalance(){
        balance=0;
    }
}