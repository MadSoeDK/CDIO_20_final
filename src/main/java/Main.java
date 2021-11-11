import gui_fields.*;
import gui_main.GUI;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.setBoard();
        board.createPlayer();
        Cup cup = new Cup();

        /**
         * Game logik
         */
        int currentPlayer = 0;
        while(true) {
            board.button();
            cup.roll();
            board.movePlayer(currentPlayer, cup.getSum());
            currentPlayer++;

            if(currentPlayer == 4) {
                currentPlayer = 0;
            }
        }

    }
}
