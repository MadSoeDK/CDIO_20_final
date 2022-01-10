package Model;

/**
 * Used in Model.Player Array, Controls Model.Account-class, used to move, earn & lose money.
 */
public class Player {

    private int placement;
    private final String name;
    int balance;
    int netWorth;
    boolean bankrupt;
    boolean hasJailFreeCard;

    public Player(String name, int balance) {
         this.balance = balance;
         this.name = name;
         this.netWorth = getPlayerBalance();
         this.bankrupt = false;
         hasJailFreeCard = false;
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
    public void setNetWorth(int addValue) {
        netWorth = addValue;
    }
    public int getNetWorth() {
        return netWorth;
    }
    public void setBankruptStatus(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }
    public boolean getBankruptStatus() {
        return bankrupt;
    }
    public void setHasJailFreeCard(boolean value) {
        hasJailFreeCard = value;
    }
    public boolean gethasJailFreecard() {
        return hasJailFreeCard;
    }
}
