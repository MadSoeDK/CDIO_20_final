package Model;

/**
 * Used in Model.Player Array, Controls Model.Account-class, used to move, earn & lose money.
 */
public class Player {

    private int placement;
    private final String name;
    int balance;
    int netWorth;
    int escapeAttempt;
    int turnsInRow;
    boolean bankrupt;
    boolean inJail;
    boolean hasJailFreeCard;

    public Player(String name, int balance) {
         this.balance = balance;
         this.name = name;
         this.netWorth = getPlayerBalance();
         this.bankrupt = false;
         hasJailFreeCard = false;
         this.escapeAttempt = 0;
         this.inJail=false;
         this.turnsInRow=0;
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
    public boolean getInJailStatus(){return inJail; }
    public void setInJailStatus(boolean status){inJail = status;}
    public int getEscapeAttempts(){return escapeAttempt;}
    public void setEscapeAttempts(int amount){escapeAttempt=amount;}
    public void incrementEscapeAttempts(){escapeAttempt++;}
    public int getTurnsInRow(){return turnsInRow;}
    public void setTurnsInRow(int amount){turnsInRow = amount;}
    public void incrementTurnsInRow(){turnsInRow++;}
    public void setHasJailFreeCard(boolean value) {
        hasJailFreeCard = value;
    }
    public boolean gethasJailFreecard() {
        return hasJailFreeCard;
    }
}
