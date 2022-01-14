import Controller.CardController;
import Model.Cards.*;
import Model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class chanceCardTest {
    //creates new chancecard deck and player to access methods

    ChanceCardDeck deck = new ChanceCardDeck();
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