package Model.Board;

public class FreeParking extends Field {

    public static int balance;

    public FreeParking(String name, int placement) {
        super(name, placement);
    }
    public static void setBalance(int value){
        balance=balance+value;
    }
    public static int getBalance(){
        return balance;
    }
    public static void resetBalance(){
        balance=0;
    }
}