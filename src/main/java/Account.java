import static java.lang.Math.abs;

/**
 * Account handles player balance
 */
public class Account {

    private int balance;

    public Account() {
        balance = 35;
    }

    /**
     * Changes the balance. The balance can't be negative, and is then set to 0.
     *
     * @param amount Positive or negative integer
     */
    public void setBalance(int amount) {
        if (amount < 0 && amount > balance) {
            balance = 0;
        } else {
            balance = balance + amount;
        }
    }

    public int getBalance() {
        return balance;
    }
}
