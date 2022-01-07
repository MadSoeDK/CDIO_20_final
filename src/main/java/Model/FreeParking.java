package Model;

import java.awt.*;

/**
 * Child of Model.Field, Is used within field array, Tracks balance and gives to player landing on it.
 */
public class FreeParking extends Field {

    public static int balance;

    public FreeParking(int balance,String name, String description) {

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