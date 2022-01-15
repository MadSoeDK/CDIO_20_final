import Model.Board.Board;
import Model.Board.Field;

public class Board_Test {
    public static void main(String[] args) {

        Board board = new Board();

        Field[] fields = board.getFields();

        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getClass().getSimpleName());
        }

    }
}
