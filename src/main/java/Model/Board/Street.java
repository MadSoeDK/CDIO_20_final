package Model.Board;

public class Street extends Ownable {

    protected int HOUSEPRICE;
    private int houseAmount=0;

    public Street (String name, int placement, String color, int[] rent, int price, int housePrice) {
        super(name, placement, color, rent, price);
        this.HOUSEPRICE = housePrice;
    }
    public int getHousePrice() {
        return HOUSEPRICE;
    }
    public void setHouseAmount(int newHouseAmount) {

    }
    public void incrementHouseAmount(){
        houseAmount++;
        System.out.println(houseAmount);
    }
}
