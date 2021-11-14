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

        Player[] players = board.getPlayers();

        while(true) {
            board.button();
            cup.roll();
            sum = cup.getSum();
            System.out.println(sum);

            board.getPlayer(currentPlayer).setPlacement(sum);
            int placement = board.getPlayer(currentPlayer).getPlacement();
            board.movePlayer(currentPlayer, sum, placement);

            if(currentPlayer == players.length) {
                currentPlayer = 0;
            }
            currentPlayer++;
        }
    }
}
