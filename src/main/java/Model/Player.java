package Model;

/**
 * Used in Model.Player Array, Controls Model.Account-class, used to move, earn & lose money.
 */
public class Player {

    private int placement;
    private final String name;
    int balance;

    public Player(String name, int balance) {
         this.balance = balance;
         this.name = name;
    }

    public int getPlayerBalance() {
        return balance;
    }
    public void setPlayerBalance(int amount) { balance += amount; }
    public int getPlacement() {
        return placement;
    }
    public void gotoPlacement(int placement) { this.placement = placement; }
    public void setPlacement(int sum) {
        //placement += sum;
        placement = sum;
    }
    public String getName() {
        return name;
    }
}
