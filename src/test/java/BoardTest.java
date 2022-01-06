/*import gui_fields.GUI_Street;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class BoardTest {
    Die die = new Die();
    Player player = new Player("Player", 0, Color.black);
    private Player[] players;
    GUI_Street testField = new GUI_Street();


    @Tes
    public void movePlayer() {
        int placement = 0;
        int value = die.getFacevalue();
        player.setPlacement(value);
        assertTrue("Player didn't move correct", player.getPlacement() == placement + value);
    }
    @Test
    public void createPlayerTest() {
        int amountOfPlayers = 4;
        players = new Player[amountOfPlayers];

        for(int i = 0; i < amountOfPlayers; i++) {
                players[i] = new Player("", 0, Color.black);
        }
        assertEquals(4, players.length);
    }
    @Test
    public void ownerOfProperty() {
        Player player1 = new Player("Martin", 0, Color.red);
        Property house = new Property(testField, 0, "", Color.black);
        house.setOwner(player1);

        assertTrue("The selected player is not the owner of the property.", house.getOwner() == player1);
    }
}
 */