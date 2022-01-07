package Model;

import java.awt.Color;

public class Board {

    private ChanceCardDeck chanceCard = new ChanceCardDeck(this);


    private Field[] fields = {
            new Start("Start", Color.WHITE, "Startfeltet"),
            new Property(1, "Blå", Color.CYAN),
            new ChanceField(),
            new Property(1, "Blå", Color.CYAN),
            new Tax(2000),
            new Ferry(),
            new Property(1, "Orange", Color.ORANGE),
            new ChanceField(),
            new Property(1, "Orange", Color.ORANGE),
            new Property(1, "Orange", Color.ORANGE),
            new Jail(0,"På besøg", Color.WHITE),
            new Property(1, "Gul", Color.YELLOW),
            new Property(1, "SQUASH", Color.WHITE),
            new Property(2, "Gul", Color.YELLOW),
            new Property(1, "Gul", Color.YELLOW),
            new Ferry(),
            new Property(1, "Grå", Color.GRAY),
            new ChanceField(),
            new Property(1, "Grå", Color.GRAY),
            new Property(1, "Grå", Color.GRAY),
            new FreeParking(0,"Fri Parkering",Color.WHITE,"Modtag Penge"),
            new Property(1, "Rød", Color.RED),
            new ChanceField(),
            new Property(1, "Rød", Color.RED),
            new Property(1, "Rød", Color.RED),
            new Ferry(),
            new Property(1, "Hvid", Color.WHITE),
            new Property(1, "COLA", Color.WHITE),
            new Property(2, "Hvid", Color.WHITE),
            new Property(1, "HVID", Color.WHITE),
            new Jail(3, "Gå i fængsel", Color.WHITE),
            new Property(1, "Gul", Color.YELLOW),
            new Property(1, "GUL", Color.YELLOW),
            new ChanceField(),
            new Property(1, "Gul", Color.YELLOW),
            new Ferry(),
            new ChanceField(),
            new Property(1, "Lilla", Color.PINK),
            new Property(1, "2000 Skat", Color.WHITE),
            new Property(1, "Lilla", Color.PINK),
    };

    public Board() {
        //gui = new GUI(converter(fields));
    }

    public Field getField(int placement) {
            return fields[placement];
    }

    public Field[] getFields() {
        return fields;
    }

    public ChanceCardDeck getChanceCardDeck(){
        return chanceCard;
    }
    
}