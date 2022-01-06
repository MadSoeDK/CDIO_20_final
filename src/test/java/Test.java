import Model.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FieldAmountTest {
    Board board = new Board();

    @Test
    public void fieldAmount() {
        assertTrue(board.getFieldsTotal() == 40);
    }

}
