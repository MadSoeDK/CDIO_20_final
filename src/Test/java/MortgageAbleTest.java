import Model.Board.*;
import Model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MortgageAbleTest {
    private int[] rent = new int[1];
    Player player = new Player("", 30000);

    private Ownable property = new Ownable("property", 1, "Blue", rent, 1000) {
        @Override
        public int getCurrentRent() {
            return super.getCurrentRent();
        }
        @Override
        public void setMortgage(boolean value) {
            super.setMortgage(value);
        }
        @Override
        public void setOwner(Player player) {
            super.setOwner(player);
        }
    };
    @Test
    public void mortgageAble() {
        rent[0] = 100;
        property.setOwner(player);
        property.setMortgage(true);
        assertTrue(property.getCurrentRent() == 0);
    }

}
