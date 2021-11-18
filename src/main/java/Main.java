import gui_fields.*;

import java.awt.*;

public class Main extends Board{

    public static void main(String[] args) {
        Board board = new Board();
        board.newGame();
        Cup cup = new Cup();

        int sum = 0;
        int currentPlayer = 0;

        // Game loop
        while(true) {
            board.button(currentPlayer);
            cup.roll();
            sum = 3;

            // Get Player pre-turn information
            Player player = board.getPlayer(currentPlayer);
            int placement = player.getPlacement();

            // Remove player from board
            board.removePlayer(currentPlayer, placement);

            // Display dice roll on GUI
            board.setDice(sum);

            // Check for a complete round on board. Then recalibrate player placement
            if(placement + sum >= 24) {
                player.setPlacement(sum - 24);
                board.removePlayer(currentPlayer, placement);
                sum = 0;

                // Pass Start field and gain $2
                player.setPlayerBalance(2);
            }

            // Set new placement incremented by dice. Then get the new placement.
            player.setPlacement(sum);
            placement = player.getPlacement();

            // Update GUI with new placement
            board.movePlayer(currentPlayer, placement);

            Field field = board.getField(placement);

            // Check field type
            if (field instanceof Property) {

                // Typecast to Property
                Property property = (Property) field;

                // Field is owned by another player and it's not the current player
                if (property.getOwner() != player && property.getOwner() != null) {

                    // Get field owner
                    Player fieldOwner = property.getOwner();

                    // 1. Subtract rent from current player 2. add to field owner
                    player.setPlayerBalance(-property.getRent());
                    fieldOwner.setPlayerBalance(property.getRent());

                    //Update GUI-object and display the current player balance
                    board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                    fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());

                }

                // No one owns the Property
                if (property.getOwner() == null) {

                    // Subtract player balance from Property rent
                    player.setPlayerBalance(-property.getRent());

                    //Get the GUI-object and display the current player balance
                    player.getPlayer().setBalance(player.getPlayerBalance());

                    // Set Property owner
                    property.setOwner(player);

                    // Set GUI Field
                    GUI_Ownable ownable = (GUI_Ownable) board.getField(placement).field;
                    ownable.setOwnerName(player.getName());
                    ownable.setBorder(player.getPlayerColor());
                }
            }

            if (field instanceof Jail) {

                // Typecast to Property
                Jail jail = (Jail) field;

                // Subtract player balance from Property rent. 2. Update GUI
                player.setPlayerBalance(-jail.getRent());
                player.getPlayer().setBalance(player.getPlayerBalance());

                // Add money to Free Parking if landed on "Go To Jail"
                if (placement==18)
                {
                    FreeParking.setBalance(3);
                }

                // Set GUI Balance
                board.getField(12).getGUIField().setSubText("Modtag: "+String.valueOf(FreeParking.getBalance()));

                // Move to Jail field
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
                field.getGUIField().setSubText("Modtag: "+String.valueOf(FreeParking.getBalance()));

            }

            currentPlayer++;

            if(currentPlayer == board.amountofPlayers()) {
                currentPlayer = 0;
            }
        }
    }
}
