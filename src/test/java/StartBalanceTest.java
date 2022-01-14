import Model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class startBalanceTest {
    //creates new player to acces methods
    Player player = new Player("", 30000);

    @Test
    //checks if start balance is 30000kr
    public void startField() {
        assertTrue(player.getPlayerBalance() == 30000);
    }

}