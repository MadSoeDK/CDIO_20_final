package Model.Board;

import Model.Player;

public class Ferry extends Ownable {

    private int numberFerryOwned = 0;

    public Ferry(String name, int placement, String color, int[] rent, int price) {
        super(name,placement,color,rent,price);
    }
/*
    public int getRent(int owners) {
        return rent[owners];
    }*/

    @Override
    public int getCurrentRent(){
        int rent;
        if (owner != null) {
            if (mortgage){
                return 0;
            }
            rent = RENT[numberFerryOwned];
            numberFerryOwned = 0;
        } else {
            rent = PRICE;
        }
        return rent;


        /*// Check amounts of other ferries owned
        int same_ferry_owner=0;
        int ferry_cost=500;

        for (var i=5; i<35; i+=10) {
            // Typecast
            Ferry property_check = (Ferry) board.getField(i);

            // Check if owner is the same
            if (property_check.getOwner() == property.getOwner()) {
                same_ferry_owner++;
                if (same_ferry_owner>1)
                {
                    ferry_cost=ferry_cost*2;
                }
            }
        }

        return ferry_cost;
    */
    }
    public void updateNumberOfFerriesOwned(Player player, Board board) {
        int placement = player.getPlacement();
        boolean firstTime = true;
        for (int i = 0; i<board.getFields().length; i++){
            if (board.getFields()[i] instanceof Ferry && ((Ownable) board.getFields()[i]).getOwner() == ((Ownable)board.getField(placement)).getOwner() && ((Ownable)board.getField(placement)).getOwner() != null) {
                if (firstTime) {
                    firstTime = false;
                    continue;
                }
                numberFerryOwned++;
            }
        }
    }

}
