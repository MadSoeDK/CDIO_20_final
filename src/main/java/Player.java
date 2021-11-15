import gui_fields.GUI_Player;

public class Player {

    private int placement;
    private String name;
    private GUI_Player player;
    private Account account;
    private int playerindex = 0;

    public Player(String name, int balance) {
         account = new Account(balance);
         this.player = new GUI_Player(name, balance);
         this.name = name;
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
