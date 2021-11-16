import gui_fields.GUI_Chance;

import java.awt.*;

public class Card extends ChanceField {
    protected int value;

    public Card(GUI_Chance field, Card card, String name, Color color, int value) {
        super(field, name, fieldType, color);
        this.card = new Card[20];
        this.value = value;
        iniCards();
    }
    public void iniCards() {
    }

}
