package Model;

import gui_fields.*;
import gui_main.GUI;
import java.awt.Color;
/**
 * Handles field-array, Model.Player-array, Gui-information.
 */
public class Board {
    private GUI_Field[] GUIfields;
    private GUI gui;
    private Player[] player;
    private Color[] colors = {Color.RED, Color.WHITE, Color.ORANGE, Color.MAGENTA};
    private int currentPlayer;
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

    /*public GUI_Field[] converter(Field[] fields) {
        GUIfields = new GUI_Field[fields.length];
        for(int i = 0; i < GUIfields.length; i++) {
            GUIfields[i] = fields[i].field;
        }
        return GUIfields;
    }

    public void newGame() {
        switch (gui.getUserSelection("How many players?", "2", "3", "4")) {
            case "2":
                createPlayer(2);
                break;
            case "3":
                createPlayer(3);
                break;
            case "4":
                createPlayer(4);
                break;
        }
    }

    public void createPlayer(int n) {
        player = new Player[n];
        for(int i = 0; i < n; i++) {
            gui.showMessage("Tilføj en spiller: ");
            player[i] = new Player(gui.getUserString(""), 35, colors[i]);
            gui.addPlayer(player[i].getPlayer());
            GUIfields[0].setCar(player[i].getPlayer(), true);
        }
    }

    public void guiMessage(String message){
        gui.showMessage(message);
    }

    public void removePlayer(int currentPlayer, int placement) {
        GUIfields[placement].setCar(player[currentPlayer].getPlayer(), false);
    }

    public int getCurrentPlayerVar (){
        return currentPlayer;
    }

    public void movePlayer(int currentPlayer, int placement) {
        GUIfields[placement].setCar(player[currentPlayer].getPlayer(), true);
    }

    public int amountofPlayers() {
        return player.length;
    }

    /*public void setDice(int sum) {
            gui.setDie(sum);
        }

    public void Button(int currentPlayer) {
        gui.getUserButtonPressed("Nu er det " + player[currentPlayer].getName() + "'s tur, rul terningen!", "Rul terning");
    }

    public Player getPlayer(int number) {
        return player[number];
    }*/

    public Field getField(int placement) {
            return fields[placement];
        }

    /*public Player getCurrentPlayer(){
        return player[currentPlayer];
    }

    public void updateCurrentPlayer(int currentPlayer){
        this.currentPlayer=currentPlayer;
    }*/

    public boolean checkWinner(){
        // If no winner
        boolean winner=false;
        for (int i=0; i< player.length; i++)
        {
            if (player[i].getPlayerBalance()<1){
                winner=true;
            }
        }
        return winner;
    }
    public Field[] getFields() {
        return fields;
    }
    public int getFieldsTotal() {
        return GUIfields.length;
    }
    public int getWinner(){
        int winner=0;
        int high_score=0;
        for (int i=0; i< player.length; i++)
        {
            if (player[i].getPlayerBalance()>high_score){
                winner=i;
                high_score=player[i].getPlayerBalance();
            }
        }
        return winner;
    }

    public ChanceCardDeck getChanceCardDeck(){
        return chanceCard;
    }
    
}