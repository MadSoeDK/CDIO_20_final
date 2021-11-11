public class Player {
    Account account = new Account();
    public int getPlayerBalance(){
        return account.getBalance();
    }

    public void setPlayerBalance(int amount){
        account.setBalance(amount);
    }
}
