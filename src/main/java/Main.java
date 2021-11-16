import gui_fields.*;
import gui_main.GUI;
import javax.swing.*;
import java.awt.*;

public class Main extends Board{
    public static void main(String[] args) {

        Board board = new Board();
        board.newGame();
        Cup cup = new Cup();

        int sum = 0;
        int currentPlayer = 0;

        /**
         * Game logik
         */

        while(true) {
            board.button(currentPlayer);
            cup.roll();
            sum = 2;//cup.getSum();

            int placement = board.getPlayer(currentPlayer).getPlacement();

            board.removePlayer(currentPlayer, sum, placement);

            board.setDice(sum);

            if(placement + sum >= 24) {
                board.getPlayer(currentPlayer).setPlacement(sum - 24);
                board.removePlayer(currentPlayer, sum, placement);
                sum = 0;
                // Pass Start field
                board.getPlayer(currentPlayer).setPlayerBalance(+2);
            }
            board.getPlayer(currentPlayer).setPlacement(sum);
            placement = board.getPlayer(currentPlayer).getPlacement();
            board.movePlayer(currentPlayer, sum, placement);

            //System.out.println(board.getField(placement).fieldType);
            //Get field and switch fieldtype


            Field field = board.getField(placement);


            if (field instanceof Property) {

                Property property = (Property) field;

                // Purchased
                if (property.getOwner() != null) {

                    // get field owner
                    Player fieldOwner = property.getOwner();

                    // Subtract from currentplayer add to owner
                    board.getPlayer(currentPlayer).setPlayerBalance(-property.getRent());
                    board.getPlayer(fieldOwner.getPlayerindex()).setPlayerBalance(property.getRent());

                    //Get the GUI-object and display the current player balance
                    board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                    board.getPlayer(fieldOwner.getPlayerindex()).getPlayer().setBalance(board.getPlayer(fieldOwner.getPlayerindex()).getPlayerBalance());

                } else {
                    // Not purchased
                    // Subtract player balance from Property rent
                    board.getPlayer(currentPlayer).setPlayerBalance(property.getRent());

                    //Get the GUI-object and display the current player balance
                    board.getPlayer(currentPlayer).getPlayer().setBalance(property.getRent());

                    // Set Property owner
                    property.setOwner(board.getPlayer(currentPlayer));

                    // Set GUI Field
                    GUI_Ownable ownable = (GUI_Ownable) board.getField(placement).field;
                    ownable.setOwnerName(board.getPlayer(currentPlayer).getName());
                    ownable.setBorder(Color.BLACK);
                }


            }

            /*switch (board.getField(placement).fieldType) {
                //Felt er ikke købt
                case 3:
                    //Subtract player balance from field rent
                    board.getPlayer(currentPlayer).setPlayerBalance(-board.getField(placement).getRent());

                    //Get the GUI-object and display the current player balance
                    board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());

                    // Set field owner
                    board.getField(placement).setOwner(board.getPlayer(currentPlayer));

                    // Set GUI Field
                    GUI_Ownable ownable = (GUI_Ownable) board.getField(placement).field;
                    ownable.setOwnerName(board.getPlayer(currentPlayer).getName());
                    ownable.setBorder(Color.BLACK);

                    //Update field type to purchased
                    board.getField(placement).setFieldType(4);
                    break;
                //Feltet er købt
                case 4:
                    // get field owner
                    Player fieldOwner = board.getField(placement).getOwner();

                    // Subtract from currentplayer add to owner
                    board.getPlayer(currentPlayer).setPlayerBalance(-board.getField(placement).getRent());
                    board.getPlayer(fieldOwner.getPlayerindex()).setPlayerBalance(board.getField(placement).getRent());

                    //Get the GUI-object and display the current player balance
                    board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                    board.getPlayer(fieldOwner.getPlayerindex()).getPlayer().setBalance(board.getPlayer(fieldOwner.getPlayerindex()).getPlayerBalance());
                    break;
                default:
                    System.out.println("Ikke genkendelig felttype");
            }*/

            currentPlayer++;

            if(currentPlayer == board.amountofPlayers()) {
                currentPlayer = 0;
            }

        }
    }
}
