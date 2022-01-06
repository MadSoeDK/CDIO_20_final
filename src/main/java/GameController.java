import Model.*;
import gui_fields.GUI_Ownable;

public class GameController {
    Board board;
    Cup cup;

    // Initializing Variables
    int sum = 0;
    int currentPlayer = 0;
    int same_color_owner = 0;
    int rent_mutiplier = 1;

    public GameController() {
        board = new Board();
        cup = new Cup();
        board.newGame();
    }

    public void playGame() {
        do {
            takeTurn();
        } while (!board.checkWinner());
    }

    public void takeTurn() {
        // Roll Model.Cup
        board.button(currentPlayer);
        cup.roll();
        sum = cup.getSum();

        // Get Player pre-turn information
        Player player = board.getPlayer(currentPlayer);
        int placement = player.getPlacement();

        moveplayer(placement, player);

        Field field = board.getField(placement);

        checkFieldType(field, placement, player);

        nextTurn();

        checkWinner();

    }

    public void nextTurn() {
        // Chance Model.Player Turn/Reset to first player
        currentPlayer++;
        if (currentPlayer == board.amountofPlayers()) {
            currentPlayer = 0;
        }
        board.updateCurrentPlayer(currentPlayer);
    }

    public void checkFieldType(Field field, int placement, Player player) {
        // Check field type
        if (field instanceof Property) {

            // Typecast to Model.Property
            Property property = (Property) field;

            // Model.Field is owned by another player and it's not the current player
            if (property.getOwner() != player && property.getOwner() != null) {

                // Get field owner
                Player fieldOwner = property.getOwner();

                // Check if both fields of same color is owned by the same player
                if (placement - 2 < 0) {
                    for (int i = 0; i < (placement + 3); i++) {
                        // Check 2 field in either direction
                        if (board.getField(i) instanceof Property) {
                            // Typecast to Model.Property
                            Property property_check = (Property) board.getField(i);

                            // Check if owner/color is the same
                            if (property_check.getColor() == property.getColor() && property_check.getOwner() == property.getOwner()) {
                                same_color_owner++;
                            }
                        }
                    }
                } else {
                    for (int i = placement - 2; i < placement + 3; i++) {
                        // Check 2 field in either direction
                        if (i < 24) {
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
                if (same_color_owner == 2) {
                    rent_mutiplier = 2;
                } else {
                    rent_mutiplier = 1;
                }

                // 1. Subtract rent from current player 2. add to field owner
                player.setPlayerBalance(-property.getRent() * rent_mutiplier);
                fieldOwner.setPlayerBalance(property.getRent() * rent_mutiplier);

                //Update GUI-object and display the current player balance
                board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());

                // Reset
                same_color_owner = 0;
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

            // Add money to Free Parking if landed on "Go To Model.Jail"
            if (placement == 18) {
                FreeParking.setBalance(3);
            }

            // Set GUI Balance
            board.getField(12).getGUIField().setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));

            // Move to Model.Jail field
            player.gotoPlacement(6);
            placement = player.getPlacement();

            // Update GUI with new placement
            board.movePlayer(currentPlayer, placement);
            board.removePlayer(currentPlayer, 18);
        }

        if (field instanceof FreeParking) {

            // Give money to player
            player.setPlayerBalance(FreeParking.getBalance());
            player.getPlayer().setBalance(player.getPlayerBalance());

            // Reset Free Parking
            FreeParking.resetBalance();

            // Set GUI Balance
            field.getGUIField().setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));

        }
    }

    public void moveplayer(int placement, Player player) {
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
    }

    public void checkWinner() {
        //board.guiMessage(board.getPlayer(board.getWinner()).getName()+" HAS WON THE GAME!");
        if(board.getCurrentPlayer().getPlayerBalance() < 11) {
            board.guiMessage(board.getPlayer(board.getWinner()).getName()+ "WON THE GAME!");
        }
    }
    public int netWorth(Player player) {
        int netWorth = 0;

        for(int i = 0; i < board.getFieldsTotal(); i++) {
            //Type casting field to Ownable
            if(board.getField(i) instanceof Ownable) {
                Ownable property = (Ownable) board.getField(i);
                if(player == property.getOwner()) {
                    netWorth += property.
                }
            }
        }
    }
}
