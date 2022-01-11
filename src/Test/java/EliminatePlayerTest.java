import Model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EliminatePlayerTest {
    private GameController gameController;
    private Player[] players;


    @Test
    public void eliminateTest() {
        players = new Player[3];
        players[0] = new Player("1",30000);
        players[1] = new Player("2",30000);
        players[2] = new Player("3",30000);
        players[0].setBankruptStatus(true);
        gameController.eliminatePlayer(players[0], 0);
        assertTrue(players.length == 2);

    }
}
