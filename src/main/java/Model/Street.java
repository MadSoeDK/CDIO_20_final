package Model;

public class Street extends Ownable {

    protected int houses = 0;
    protected int HOUSEPRICE;

    public Street (String name, int placement, String color, int[] rent, int price, int housePrice) {
        super(name, placement, color, rent, price);
        this.HOUSEPRICE = housePrice;
    }
    public int getHousePrice() {
        return HOUSEPRICE;
    }
    public int getHouses() {return houses;}
}
