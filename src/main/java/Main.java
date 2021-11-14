import gui_fields.*;
import gui_main.GUI;
import javax.swing.*;
import java.awt.*;

public class Main extends Board{
    public static void main(String[] args) {
        Board board = new Board();
        board.newGame();
        Cup cup = new Cup();

        Player[] players = board.getPlayers();

        int sum = 0;
        int currentPlayer = 0;

        /**
         * Game logik
         */

        while(true) {
            board.button();
            cup.roll();
            sum = cup.getSum();

            int placement = board.getPlayer(currentPlayer).getPlacement();

            board.removePlayer(currentPlayer, sum, placement);

            if(placement + sum >= 24) {
                //placement = placement + sum - 24;
                board.getPlayer(currentPlayer).setPlacement((placement + sum) - 24);
                board.removePlayer(currentPlayer, sum, placement);
                sum = 0;
            }
            System.out.println(sum);
            board.getPlayer(currentPlayer).setPlacement(sum);
            placement = board.getPlayer(currentPlayer).getPlacement();
            board.movePlayer(currentPlayer, sum, placement);


            currentPlayer++;

            if(currentPlayer == players.length) {
                currentPlayer = 0;
            }

        }
    }
}
