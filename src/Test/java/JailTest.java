import Model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class jailTest {
    //creates new player to acces methods
    Player player = new Player("", 30000);

    @Test
    //checks if player is in jail
    public void startField() {
        player.setPlacement(30);
        player.testJail();
        assertTrue(player.getInJailStatus());
    }
}