package Model.Cards;
import Model.*;

public class JailFreeCards extends ChanceCard {
    private String description;
    private String name;
    private String type;
    private int value;
    private Player player;

    public JailFreeCards(String description, String name, String type, int value) {
        super(description, name, type, value);
    }
    /*
    We store this in a boolean 'hasJailFreeCard'
    In options when in jail, choose
     */
    public void setPlayerFree(Player player) {
        player.setHasJailFreeCard(true);
    }
}
