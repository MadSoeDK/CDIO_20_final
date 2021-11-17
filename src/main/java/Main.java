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
            sum = cup.getSum();

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

                // Subtract player balance from Property rent
                player.setPlayerBalance(jail.getRent());

                // Move to Jail field
                player.setPlacement(6);

                // Update GUI with new placement
                board.movePlayer(currentPlayer, placement);

            }

            currentPlayer++;

            if(currentPlayer == board.amountofPlayers()) {
                currentPlayer = 0;
            }

        }
    }
}
