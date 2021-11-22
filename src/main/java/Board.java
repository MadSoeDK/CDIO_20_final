import gui_fields.*;
import gui_main.GUI;
import java.awt.Color;

public class Board {
    private GUI_Field[] GUIfields;
    private GUI gui;
    private ChanceCardDeck chanceCard;
    private Player[] player;
    private Color[] colors = {Color.green, Color.BLUE, Color.ORANGE, Color.RED};

    private Field[] fields = {
            new Start(new GUI_Start(),"Start", Color.WHITE, "Startfeltet"),
            new Property(new GUI_Street(),1, "Burgerbaren", Color.GRAY),
            new Property(new GUI_Street(),1, "Pizzahuset", Color.GRAY),
            new ChanceField(new GUI_Chance()),
            //new Property(new GUI_Street(),1, "Chance", Color.WHITE),
            new Property(new GUI_Street(),2, "Slikbutik", Color.CYAN),
            new Property(new GUI_Street(),2, "Iskiosk", Color.CYAN),
            new Jail(new GUI_Jail(), 0,"På besøg", Color.WHITE),
            new Property(new GUI_Street(),2, "Museeum", Color.PINK),
            new Property(new GUI_Street(),2, "Bibliotek", Color.PINK),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),2, "Skateparken", Color.ORANGE),
            new Property(new GUI_Street(),2, "Swimmingpool", Color.ORANGE),
            new FreeParking(new GUI_Tax(),0,"Fri Parkering",Color.WHITE,"Modtag Penge"),
            new Property(new GUI_Street(),3, "Spillehal", Color.RED),
            new Property(new GUI_Street(),3, "Kinobiograf", Color.RED),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),3, "Legetøjsbutik", Color.YELLOW),
            new Property(new GUI_Street(),3, "Dyrehandel", Color.YELLOW),
            new Jail(new GUI_Jail(), 3, "Gå i fængsel", Color.WHITE),
            new Property(new GUI_Street(),4, "Bowlinghal", Color.GREEN),
            new Property(new GUI_Street(),4, "Zoologisk have", Color.GREEN),
            new ChanceField(new GUI_Chance()),
            new Property(new GUI_Street(),5, "Vandland", Color.BLUE),
            new Property(new GUI_Street(),5, "Strandpromenade", Color.BLUE),
    };

    public Board() {
        gui = new GUI(converter(fields));
    }

    public GUI_Field[] converter(Field[] fields) {
        GUIfields = new GUI_Field[24];
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

    public Player[] getPlayers() {
            return player;
        }

    public Field[] getFields() {
            return fields;
        }

    public Field getField(int placement) {
            return fields[placement];
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
    /*
    public ChanceCard chanceCard() {
        ChanceCardDeck chanceCard = new ChanceCardDeck();
        chanceCard.drawCard();
        gui.displayChanceCard("Du trak " + chanceCard.drawCard());
    }
    */

}