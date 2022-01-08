package Model;

public class Ferry extends Ownable {

    int owners;

    public Ferry(String name, int placement, String color, int[] rent, int price) {
        super(name,placement,color,rent,price);
    }

    /*public int getRent(int owners) {
        return rent[owners];
    }

    private int getRent(Ferry property){

        // Check amounts of other ferries owned
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
    }*/
}
