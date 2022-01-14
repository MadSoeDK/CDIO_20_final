import Model.Board.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FieldAmountTest {
    //creates new board to acces methods
    Board board = new Board();

    @Test
    //checks if the created board has 40 fields
    public void fieldAmount() {
        assertTrue(board.getFields().length == 40);
    }

}
