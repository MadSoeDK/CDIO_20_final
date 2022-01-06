package Model;

import Model.Account;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import java.awt.Color;

/**
 * Used in Model.Player Array, Controls Model.Account-class, used to move, earn & lose money.
 */
public class Player {

    // Variables
    private int placement;
    private String name;
    private GUI_Player player;
    private Account account;

    // Constructor
    public Player(String name, int balance, Color color) {
         account = new Account(balance);
         this.player = new GUI_Player(name, balance, new GUI_Car(color,color, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL));
         this.name = name;
    }

    // Methods
    public int getPlayerBalance() {
        return account.getBalance();
    }
    public void setPlayerBalance(int amount) {account.setBalance(amount);}
    public GUI_Player getPlayer(){
        return this.player;
    }
    public int getPlacement() {
        return placement;
    }
    public void gotoPlacement(int placement) { this.placement = placement; }
    public void setPlacement(int sum) {
        placement += sum;
    }
    public String getName() {
        return name;
    }
    public Color getPlayerColor() {
        return player.getPrimaryColor();
    }

}