import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import java.awt.Color;
public class Player {

    private int placement;
    private String name;
    private GUI_Player player;
    private Account account;
    private int playerindex;
    private Color color;

    public Player(String name, int balance, Color color) {
         account = new Account(balance);
         this.player = new GUI_Player(name, balance, new GUI_Car(color,color, GUI_Car.Type.CAR, GUI_Car.Pattern.FILL));
         this.name = name;
         this.color = color;
         playerindex = 0;
         playerindex++;
    }

    public int getPlayerBalance() {
        return account.getBalance();
    }

    public void setPlayerBalance(int amount) {
        account.setBalance(amount);
    }

    public GUI_Player getPlayer(){
        return this.player;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int sum) {
        placement += sum;
    }

    public String getName() {
        return name;
    }

    public int getPlayerindex() {
        return playerindex;
    }
}
