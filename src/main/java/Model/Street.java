package Model;

public class Street extends Ownable {

    protected int HOUSEPRICE;
    protected boolean mortgage;

    public Street (String name, int placement, String color, int[] rent, int price, int housePrice) {
        super(name, placement, color, rent, price);
        this.HOUSEPRICE = housePrice;
        this.mortgage = false;
    }
    public int getHousePrice() {
        return HOUSEPRICE;
    }
}
