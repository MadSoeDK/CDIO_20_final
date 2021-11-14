import gui_fields.GUI_Player;

public class Player {

    int placement;
    String name;
    GUI_Player player;
    Account account;

    public Player(String name, int balance) {
         account = new Account(balance);
         this.player = new GUI_Player(name, balance);
         this.name = name;
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
}
