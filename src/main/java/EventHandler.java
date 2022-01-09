import Model.*;
import gui_main.GUI;

public class EventHandler {

    private GUIController gui;

    public EventHandler(GUIController gui) {
        this.gui = gui;
    }

    public void fieldEffect(Player player, Street street) {
        if (street.getOwner() == null) { // No owner - ask to buy it
            buyField(player, street);
        } else { //Pay rent to owner
            Player fieldOwner = street.getOwner();

            // Check if Owner has Monopoly
            if (false /*board.hasMonopoly(placement)*/) {
                // 1. Subtract rent from current player 2. add to field owner
                player.setPlayerBalance(-street.getCurrentRent() * 2);
                fieldOwner.setPlayerBalance(street.getCurrentRent() * 2);
            } else {
                player.setPlayerBalance(-street.getCurrentRent());
                fieldOwner.setPlayerBalance(street.getCurrentRent());
            }
        }
    }
    public void fieldEffect(Player player, Ferry ferry) {
        // Check Ferry Rent
        int ferry_cost = ferry.getCurrentRent(); //getFerryRent()

        if (ferry.getOwner() == null) { // Noone owns Ferry
            buyField(player, ferry);

            //ferry_cost = getFerryRent(property);
            //updateFerryGUI(property, ferry_cost);

        } else {
            // Get field owner
            Player fieldOwner = ferry.getOwner();

            // 1. Subtract rent from current player 2. add to field owner
            player.setPlayerBalance(-ferry_cost);
            fieldOwner.setPlayerBalance(ferry_cost);
        }
    }
    public void fieldEffect(Player player, Brewery brewery, int sum) {
        // Typecast other company
        Ownable otherBrewery;

        // No one owns Company
        if (brewery.getOwner() == null) {
            buyField(player, brewery);
        }  else { // Other player Owns Company

            // Get field owner
            Player fieldOwner = brewery.getOwner();

            /*if (brewery.getPlacement() == 12) {
                otherBrewery = (Ownable) board.getField(27);
            } else {
                otherBrewery = (Ownable) board.getField(12);
            }*/

            // 1. Subtract rent from current player 2. add to field owner
            if (brewery.getOwner() == brewery.getOwner()) {
                player.setPlayerBalance(-brewery.getRent()[1] * sum);
                fieldOwner.setPlayerBalance(brewery.getRent()[1] * sum);
            } else {
                player.setPlayerBalance(-brewery.getRent()[0] * sum);
                fieldOwner.setPlayerBalance(brewery.getRent()[0] * sum);
            }
        }
    }
    public void fieldEffect(Player player, Jail jail) {
        // Add money to Free Parking if landed on "Go To Jail"
        if (jail.getPlacement() == 30) {
            gui.message(player.getName() + " rykker til fængsel og betaler $3");

            //FreeParking.setBalance(3);
        }
    }
    public void fieldEffect(Player player, Start start, int sum) {

    }
    public void fieldEffect(Player player, Tax tax) {
        if (tax.getPlacement() == 4) { // first tax field
            boolean answer = gui.getUserBool("Betal 10% eller 4000 kr?", "4000 kr.", "10%");
            if (answer) {
                player.setPlayerBalance(-4000);
            } else {
                //Pay 10%
            }
        } else { // last tax field
            player.setPlayerBalance(-2000);
        }
    }
    public void fieldEffect(Player player, ChanceField chanceField, int sum) {

    }
    public void buyField(Player player, Ownable field) {
        boolean answer = gui.getUserBool("Buy this field?", "Yes", "No");

        // Buy if yes
        if (answer) {
            player.setPlayerBalance(-field.getPrice());
            field.setOwner(player);
            gui.setOwner(player,field);
        }
    }
}
