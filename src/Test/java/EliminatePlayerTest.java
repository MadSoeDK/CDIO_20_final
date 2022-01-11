import Model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EliminatePlayerTest {
    private Player[] players;

    //Had to hard code method as GameController is in deafualt package and can't be reached
    public void eliminatePlayer(Player player, int placement) {
        Player[] newPlayers = new Player[players.length - 1];
        int j = 0;
        for (int i = 0; i < players.length; i++) {
            if (players[i].getBankruptStatus()) {
                /*gui.removePlayer(player, placement);
                gui.getGuiPlayer(players[i]).setName(players[i].getName() + "\n[BANKEROT]");
                gui.getGuiPlayer(players[i]).setBalance(0);*/
            } else {
                newPlayers[j] = players[i];
                j++;
            }
        }
        players = newPlayers;
    }

    @Test
    public void eliminateTest() {
        //makes a Player array
        players = new Player[3];
        players[0] = new Player("1",30000);
        players[1] = new Player("2",30000);
        players[2] = new Player("3",30000);
        //bankrupts player and checks if player has been removed from array
        players[0].setBankruptStatus(true);
        eliminatePlayer(players[0],0 );
        assertTrue(players.length == 2);

    }
}
