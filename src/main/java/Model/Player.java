package Model;

/**
 * Provides player functionality
 */
public class Player {

    private int placement;
    private final String NAME;
    int balance;
    int netWorth = getPlayerBalance();
    int escapeAttempt = 0;
    int turnsInRow = 0;
    boolean bankrupt = false;
    boolean inJail = false;
    boolean hasJailFreeCard = false;

    public Player(String name, int balance) {
         this.balance = balance;
         this.NAME = name;
    }

    public int getPlayerBalance() {
        return balance;
    }
    public void setPlayerBalance(int amount) { balance += amount; }
    public int getPlacement() {
        return placement;
    }
    public void setPlacement(int placement) {
        this.placement = placement;
    }
    public String getName() {
        return NAME;
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
