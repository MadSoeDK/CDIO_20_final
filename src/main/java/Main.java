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
                case 3:
                    //Felter er ikke købt
                    board.getPlayer(currentPlayer).setPlayerBalance(-3);
                    break;
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
