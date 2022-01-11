import Model.*;
import Model.Board.*;
import gui_codebehind.GUI_BoardController;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class startFieldTest {
    //creates new board and player to acces methods
    Board board = new Board();
    private GUI_BoardController gui;
    private Color[] colors = {Color.RED, Color.WHITE, Color.ORANGE, Color.MAGENTA};
    private int i;
    Player player = new Player("", 30000);

    @Test
    //checks if player start on start field which has placement 0
    public void startField() {
        assertTrue(player.getPlacement() == 0);
    }

}