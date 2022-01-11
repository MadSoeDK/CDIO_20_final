package Model.Cards;
import Model.*;

public class JailFreeCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int value;

    public JailFreeCards(String description, String name, String type, int value) {
        super(description, name, type, value);
        this.value = value;
    }
    /*
    We store this in a boolean 'hasJailFreeCard'
    In options when in jail, choose
     */
    public void setPlayerFree(Player player) {
        player.setHasJailFreeCard(true);
    }
}
