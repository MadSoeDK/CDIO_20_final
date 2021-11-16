import gui_fields.*;
import gui_main.GUI;
import java.awt.Color;

public class Board {
    private GUI_Field[] GUIfields;
    private GUI gui;
    private int[] cards = new int[10];
    private Player[] player;
    private Color[] colors = {Color.green, Color.BLUE, Color.ORANGE, Color.RED};

    private Field[] fields = {
            new Start(new GUI_Start(),"Start", Color.WHITE, "Startfeltet"),
            new Property(new GUI_Street(),1, "Burgerbaren", Color.GRAY),
            new Property(new GUI_Street(),1, "Pizzahuset", Color.GRAY),
            new Property(new GUI_Street(),1, "Chance", Color.WHITE),
            new Property(new GUI_Street(),2, "Slikbutik", Color.CYAN),
            new Property(new GUI_Street(),2, "Iskiosk", Color.CYAN),
            new Property(new GUI_Street(),1, "Fængsel", Color.WHITE),
            new Property(new GUI_Street(),2, "Museeum", Color.PINK),
            new Property(new GUI_Street(),2, "Bibliotek", Color.PINK),
            new Property(new GUI_Street(),1, "Chance", Color.WHITE),
            new Property(new GUI_Street(),2, "Skateparken", Color.ORANGE),
            new Property(new GUI_Street(),2, "Swimmingpool", Color.ORANGE),
            new Property(new GUI_Street(),1, "Fri parkering", Color.WHITE),
            new Property(new GUI_Street(),3, "Spillehal", Color.RED),
            new Property(new GUI_Street(),3, "Kinobiograf", Color.RED),
            new Property(new GUI_Street(),1, "Chance", Color.WHITE),
            new Property(new GUI_Street(),3, "Legetøjsbutik", Color.YELLOW),
            new Property(new GUI_Street(),3, "Dyrehandel", Color.YELLOW),
            new Property(new GUI_Street(),1, "Gå i fængsel", Color.WHITE),
            new Property(new GUI_Street(),4, "Bowlinghal", Color.GREEN),
            new Property(new GUI_Street(),4, "Zoologisk have", Color.GREEN),
            new Property(new GUI_Street(),1, "Chance", Color.WHITE),
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
}