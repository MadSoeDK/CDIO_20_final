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
            new Start(new GUI_Start(),"Start", Color.WHITE, "Startfeltet"),
            new Property(new GUI_Street(),1, "Blå", Color.CYAN),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),1, "Blå", Color.CYAN),
            new Tax(new GUI_Tax(), 2000),
            new Ferry(new GUI_Shipping()),
            new Property(new GUI_Street(),1, "Orange", Color.ORANGE),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),1, "Orange", Color.ORANGE),
            new Property(new GUI_Street(),1, "Orange", Color.ORANGE),
            new Jail(new GUI_Jail(), 0,"På besøg", Color.WHITE),
            new Property(new GUI_Street(),1, "Gul", Color.YELLOW),
            new Property(new GUI_Street(),1, "SQUASH", Color.WHITE),
            new Property(new GUI_Street(),2, "Gul", Color.YELLOW),
            new Property(new GUI_Street(),1, "Gul", Color.YELLOW),
            new Ferry(new GUI_Shipping()),
            new Property(new GUI_Street(),1, "Grå", Color.GRAY),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),1, "Grå", Color.GRAY),
            new Property(new GUI_Street(),1, "Grå", Color.GRAY),
            new FreeParking(new GUI_Tax(),0,"Fri Parkering",Color.WHITE,"Modtag Penge"),
            new Property(new GUI_Street(),1, "Rød", Color.RED),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),2, "Rød", Color.RED),
            new Property(new GUI_Street(),2, "Rød", Color.RED),
            new Ferry(new GUI_Shipping()),
            new Property(new GUI_Street(),2, "Hvid", Color.WHITE),
            new Property(new GUI_Street(),3, "COLA", Color.WHITE),
            new Property(new GUI_Street(),3, "Hvid", Color.WHITE),
            new Property(new GUI_Street(),3, "HVID", Color.WHITE),
            new Jail(new GUI_Jail(), 3, "Gå i fængsel", Color.WHITE),
            new Property(new GUI_Street(),4, "Gul", Color.YELLOW),
            new Property(new GUI_Street(),4, "GUL", Color.YELLOW),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),4, "Gul", Color.YELLOW),
            new Ferry(new GUI_Shipping()),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),5, "Lilla", Color.PINK),
            new Property(new GUI_Street(),5, "2000 Skat", Color.WHITE),
            new Property(new GUI_Street(),5, "Lilla", Color.PINK),
    };

    public Board() {
        gui = new GUI(converter(fields));
    }

    /**
     * Returns an array of the correct GUI_Fields type for the GUI to use
     * @param fields
     * @return
     */
    public GUI_Field[] converter(Field[] fields) {
        GUIfields = new GUI_Field[40];
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

    public void button(int currentPlayer) {
        gui.getUserButtonPressed("Nu er det " + player[currentPlayer].getName() + "'s tur, rul terningen!", "Rul terning");
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

    public void setDice(int sum) {
            gui.setDie(sum);
        }

    public Player getPlayer(int number) {
        return player[number];
    }

    public Field getField(int placement) {
            return fields[placement];
        }

    public Player getCurrentPlayer(){
        return player[currentPlayer];
    }

    public void updateCurrentPlayer(int currentPlayer){
        this.currentPlayer=currentPlayer;
    }

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