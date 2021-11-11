import gui_fields.*;
import gui_main.GUI;
import javax.swing.*;
import java.awt.*;

public class Main extends Board{
    public static void main(String[] args) {
        Board board = new Board();
        board.setBoard();
        board.createPlayer();
        Cup cup = new Cup();

        GUI_Player[] player = new GUI_Player[4];

        /**
         * Game logik
         */
        int sum = 0;
        int currentPlayer = 0;
        while(true) {
            board.button();
            cup.roll();

            board.getPlayer(player, currentPlayer);
            sum = cup.getSum() + sum;
            board.movePlayer(currentPlayer, sum);

            if(currentPlayer == 4) {
                currentPlayer = 0;
            }
            currentPlayer++;
        }

    }
}
