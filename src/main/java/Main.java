import Model.*;
import gui_fields.*;

/**
 * Main Class: Responsible for gamelogic. Coupled with Model.Board to get necessary information.
 * Turn Logic, Landing on fields, Winning
 */
public class Main {

    public static void main(String[] args) {

        GameController game = new GameController();
        game.startGame();

    }
}

