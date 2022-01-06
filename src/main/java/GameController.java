import Model.*;
import gui_fields.GUI_Ownable;

public class GameController {

    Board board;
    Cup cup;

    public GameController() {
        board = new Board();
        cup = new Cup();
    }

    public void startGame() {

        // Initializing Variables
        int sum = 0;
        int currentPlayer = 0;
        int same_color_owner=0;
        int rent_mutiplier=1;

        board.newGame();

        while (!board.checkWinner()) {
            // Roll Model.Cup
            board.button(currentPlayer);
            cup.roll();
            sum = 5;//cup.getSum();

            // Get Model.Player pre-turn information
            Player player = board.getPlayer(currentPlayer);
            int placement = player.getPlacement();

            // Remove player from board
            board.removePlayer(currentPlayer, placement);

            // Display dice roll on GUI
            board.setDice(sum);

            // Check for a complete lap around on board. Then recalibrate player placement
            if(placement + sum >= 40) {
                player.setPlacement(sum - 40);
                board.removePlayer(currentPlayer, placement);
                sum = 0;

                // Pass Model.Start field and gain $2
                player.setPlayerBalance(2);
            }

            // Set new placement incremented by dice. Then get the new placement.
            player.setPlacement(sum);
            placement = player.getPlacement();

            // Update GUI with new placement
            board.movePlayer(currentPlayer, placement);
            Field field = board.getField(placement);


            if (field instanceof ChanceField) {

                // Draw Chance Card
                board.getChanceCardDeck().useChanceCard();
                player.getPlayer().setBalance(player.getPlayerBalance());
                placement = player.getPlacement();
                field = board.getField(placement);
                sum=0;
            }

            // Check field type
            if (field instanceof Property) {

                // Typecast to Model.Property
                Property property = (Property) field;

                // Model.Field is owned by another player and it's not the current player
                if (property.getOwner() != player && property.getOwner() != null) {

                    // Get field owner
                    Player fieldOwner = property.getOwner();

                    // Check if both fields of same color is owned by the same player
                    if (placement-2<0) {
                        for (int i=0; i<(placement+3) ;i++) {
                            // Check 2 field in either direction
                            if (board.getField(i) instanceof Property){
                                // Typecast to Model.Property
                                Property property_check = (Property) board.getField(i);

                                // Check if owner/color is the same
                                if (property_check.getColor()==property.getColor() && property_check.getOwner()==property.getOwner())
                                {
                                    same_color_owner++;
                                }
                            }
                        }
                    } else {
                        for (int i=placement-2; i<placement+3 ;i++) {
                            // Check 2 field in either direction
                            if ( i < 24 ) {
                                if (board.getField(i) instanceof Property) {
                                    // Typecast to Model.Property
                                    Property property_check = (Property) board.getField(i);
                                    // Check if owner/color is the same
                                    if (property_check.getColor() == property.getColor() && property_check.getOwner() == property.getOwner()) {
                                        same_color_owner++;
                                    }
                                }
                            }
                        }
                    }

                    // Increase rent if owner owns entire color
                    if (same_color_owner==2) {
                        rent_mutiplier=2;
                    } else {
                        rent_mutiplier=1;
                    }

                    // 1. Subtract rent from current player 2. add to field owner
                    player.setPlayerBalance(-property.getRent()*rent_mutiplier);
                    fieldOwner.setPlayerBalance(property.getRent()*rent_mutiplier);

                    //Update GUI-object and display the current player balance
                    board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                    fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());

                    // Reset
                    same_color_owner=0;
                }

                // No one owns the Model.Property
                if (property.getOwner() == null) {

                    // Subtract player balance from Model.Property rent
                    player.setPlayerBalance(-property.getRent());

                    //Get the GUI-object and display the current player balance
                    player.getPlayer().setBalance(player.getPlayerBalance());

                    // Set Model.Property owner
                    property.setOwner(player);

                    // Set GUI Model.Field
                    GUI_Ownable ownable = (GUI_Ownable) board.getField(placement).getGUIField();
                    ownable.setOwnerName(player.getName());
                    ownable.setBorder(player.getPlayerColor());
                }
            }

            if (field instanceof Jail) {

                // Typecast to Model.Property
                Jail jail = (Jail) field;

                // Subtract player balance from Model.Property rent. 2. Update GUI
                player.setPlayerBalance(-jail.getRent());
                player.getPlayer().setBalance(player.getPlayerBalance());

                // Add money to Free Parking if landed on "Go To Jail"
                if (placement==30) {
                    FreeParking.setBalance(2000);
                }

                // Set GUI Balance
                board.getField(20).getGUIField().setSubText("Modtag: "+String.valueOf(FreeParking.getBalance()/1000)+"K");

                // Move to Model.Jail field
                player.gotoPlacement(10);
                placement = player.getPlacement();

                // Update GUI with new placement
                board.movePlayer(currentPlayer, placement);
                board.removePlayer(currentPlayer, 30);
            }

            if (field instanceof FreeParking) {

                // Give money to player
                player.setPlayerBalance(FreeParking.getBalance());
                player.getPlayer().setBalance(player.getPlayerBalance());

                // Reset Free Parking
                FreeParking.resetBalance();

                // Set GUI Balance
                field.getGUIField().setSubText("Modtag: "+String.valueOf(FreeParking.getBalance()));

            }

            if (field instanceof Ferry) {

                // Typecast to Model.Property
                Ferry property = (Ferry) field;

                // Check Ferry Rent
                int ferry_cost = getFerryRent(property);

                // Noone owns Ferry
                if (property.getOwner() == null)
                {
                    // Subtract player balance from Model.Property rent
                    player.setPlayerBalance(-4000);

                    //Get the GUI-object and display the current player balance
                    player.getPlayer().setBalance(player.getPlayerBalance());

                    // Set Model.Property owner
                    property.setOwner(player);

                    // Set GUI Model.Field
                    GUI_Ownable ownable = (GUI_Ownable) board.getField(placement).getGUIField();
                    ownable.setOwnerName(player.getName());
                    ownable.setBorder(player.getPlayerColor());

                    ferry_cost = getFerryRent(property);
                    updateFerryGUI(property, ferry_cost);
                }
                else // Other player Owns Ferry
                {
                    // Get field owner
                    Player fieldOwner = property.getOwner();

                    // 1. Subtract rent from current player 2. add to field owner
                    player.setPlayerBalance(-ferry_cost);
                    fieldOwner.setPlayerBalance(ferry_cost);

                    //Update GUI-object and display the current player balance
                    board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                    fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());
                }
            }


            if (field instanceof Company) {

                // Typecast to Model.Property
                Company property = (Company) field;

                // Check Ferry Rent
                int company_cost = sum*100;

                // Noone owns Ferry
                if (property.getOwner() == null)
                {
                    // Subtract player balance from Model.Property rent
                    player.setPlayerBalance(-500);

                    //Get the GUI-object and display the current player balance
                    player.getPlayer().setBalance(player.getPlayerBalance());

                    // Set Model.Property owner
                    property.setOwner(player);

                    // Set GUI Model.Field
                    GUI_Ownable ownable = (GUI_Ownable) board.getField(placement).getGUIField();
                    ownable.setOwnerName(player.getName());
                    ownable.setBorder(player.getPlayerColor());

                    //ferry_cost = getFerryRent(property);
                    //updateFerryGUI(property, ferry_cost);
                }
                else // Other player Owns Company
                {
                    // Get field owner
                    Player fieldOwner = property.getOwner();

                    // 1. Subtract rent from current player 2. add to field owner
                    //player.setPlayerBalance(-ferry_cost);
                    //fieldOwner.setPlayerBalance(ferry_cost);

                    //Update GUI-object and display the current player balance
                    board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                    fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());
                }
            }


            // Chance Model.Player Turn/Reset to first player
            currentPlayer++;
            if(currentPlayer == board.amountofPlayers()) {
                currentPlayer = 0;
            }
            board.updateCurrentPlayer(currentPlayer);
        }
        while (true) {
            // Show Winner - has to be in while loop, or the winner text will be removed
            board.guiMessage(board.getPlayer(board.getWinner()).getName()+" HAS WON THE GAME!");
        }
    }

    public void moveplayer() {

    }

    private int getFerryRent(Ferry property){

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
    }

    private void updateFerryGUI(Ferry property, int ferry_cost){
        // Update Cost
        for (var i=5; i<35; i+=10) {
            // Typecast
            Ferry property_check = (Ferry) board.getField(i);

            // Check if owner is the same
            if (property_check.getOwner() == property.getOwner()) {
                property_check.getGUIField().setSubText("$"+String.valueOf(ferry_cost));
            }
        }
    }

    public Player checkWinner() {
        //board.guiMessage(board.getPlayer(board.getWinner()).getName()+" HAS WON THE GAME!");
        board.getCurrentPlayer().getPlayerBalance();
        return board.getCurrentPlayer();
    }
}

