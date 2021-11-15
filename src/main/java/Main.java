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
            sum = cup.getSum();

            int placement = board.getPlayer(currentPlayer).getPlacement();

            board.removePlayer(currentPlayer, sum, placement);

            board.setDice(sum);

            if(placement + sum >= 24) {
                board.getPlayer(currentPlayer).setPlacement(sum - 24);
                board.removePlayer(currentPlayer, sum, placement);
                sum = 0;
            }
            board.getPlayer(currentPlayer).setPlacement(sum);
            placement = board.getPlayer(currentPlayer).getPlacement();
            board.movePlayer(currentPlayer, sum, placement);

            //Get field and switch fieldtype
            switch (board.getField(placement).fieldType) {
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
                    break;
                case 4:


                default:
                    System.out.println("Ikke genkendelig felttype");
            }

            System.out.println(board.getPlayer(currentPlayer).getPlayerBalance());
            //board.getPlayer(currentPlayer).setPlayerBalance(board.getPlayer(currentPlayer).getPlayerBalance() - 3);

            currentPlayer++;

            if(currentPlayer == board.amountofPlayers()) {
                currentPlayer = 0;
            }

        }
    }
}
