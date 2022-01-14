import Controller.CardController;
import Model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class chanceCardTest {
    //creates new chancecard deck, card controller and player to access methods
    CardController controller = new CardController();
    Player player = new Player("", 30000);
    Player[] players = new Player[1];


    @Test
    //checks if get out of jail card updates player attribut
    public void startField() {
        players[0] = player;
        controller.doCardAction(player,players, 34);
        assertTrue(player.gethasJailFreecard());
    }

}