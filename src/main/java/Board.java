import gui_fields.*;
import gui_main.GUI;
import java.awt.Color;

public class Board {

    private GUI_Field[] GUIfields;
    private GUI gui;
    private int[] cards = new int[10];
    private Player[] player;

    private Field[] fields = {
            new Property(new GUI_Street(),1, "Start", Color.WHITE),
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

        void newGame() {
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

    void createPlayer(int n) {
            player = new Player[n];
            for(int i = 0; i < n; i++) {
                gui.showMessage("Tilføj en spiller: ");
                player[i] = new Player(gui.getUserString(""), 35);
                gui.addPlayer(player[i].getPlayer());
                GUIfields[0].setCar(player[i].getPlayer(), true);
            }
        }
        void button(int currentPlayer) {
            gui.getUserButtonPressed("Nu er det " + player[currentPlayer].getName() + "'s tur, rul terningen!", "Rul terning");
        }
        void removePlayer(int currentPlayer, int sum, int placement) {
            GUIfields[placement].setCar(player[currentPlayer].getPlayer(), false);
        }
        void movePlayer(int currentPlayer, int sum, int placement) {
            //fields[placement + sum].setCar(player[currentPlayer].getPlayer(), true);
            GUIfields[placement].setCar(player[currentPlayer].getPlayer(), true);
        }
        void setDice(int sum) {
            gui.setDie(sum);
        }
        Player getPlayer(int number) {
            return player[number];
        }

        Player[] getPlayers() {
            return player;
        }
        public Field[] getFields() {
            return fields;
        }
        public Field getField(int placement) {
            return fields[placement];
        }
        public int amountofPlayers() {
            return player.length;
        }
        void check(int number) {

        }/*
        void shuffleCard() {

        }
        String getCard() {

        }*/

    /**
     * Sætter
     *//*
    public Board() {
        //titler
        fields[0].setTitle("Start");
        fields[1].setTitle("Burgerbaren");
        fields[2].setTitle("Pizzahuset");
        fields[3].setTitle("Chance");
        fields[4].setTitle("Slikbutik");
        fields[5].setTitle("Iskiosk");
        fields[6].setTitle("Fængsel");
        fields[7].setTitle("Museeum");
        fields[8].setTitle("Bibliotek");
        fields[9].setTitle("Chance");
        fields[10].setTitle("Skateparken");
        fields[11].setTitle("Swimmingpool");
        fields[12].setTitle("Fri parkering");
        fields[13].setTitle("Spillehal");
        fields[14].setTitle("Kinobiograf");
        fields[15].setTitle("Chance");
        fields[16].setTitle("Legetøjsbutik");
        fields[17].setTitle("Dyrehandel");
        fields[18].setTitle("Gå i fængsel");
        fields[19].setTitle("Bowlinghal");
        fields[20].setTitle("Zoologisk have");
        fields[21].setTitle("Chance");
        fields[22].setTitle("Vandland");
        fields[23].setTitle("Strandpromenade");

        //baggrundsfarve
        fields[0].setBackGroundColor(Color.WHITE);
        fields[1].setBackGroundColor(Color.GRAY);
        fields[2].setBackGroundColor(Color.GRAY);
        fields[3].setBackGroundColor(Color.WHITE);
        fields[4].setBackGroundColor(Color.CYAN);
        fields[5].setBackGroundColor(Color.CYAN);
        fields[6].setBackGroundColor(Color.WHITE);
        fields[7].setBackGroundColor(Color.PINK);
        fields[8].setBackGroundColor(Color.PINK);
        fields[9].setBackGroundColor(Color.WHITE);
        fields[10].setBackGroundColor(Color.ORANGE);
        fields[11].setBackGroundColor(Color.ORANGE);
        fields[12].setBackGroundColor(Color.WHITE);
        fields[13].setBackGroundColor(Color.RED);
        fields[14].setBackGroundColor(Color.RED);
        fields[15].setBackGroundColor(Color.WHITE);
        fields[16].setBackGroundColor(Color.YELLOW);
        fields[17].setBackGroundColor(Color.YELLOW);
        fields[18].setBackGroundColor(Color.WHITE);
        fields[19].setBackGroundColor(Color.GREEN);
        fields[20].setBackGroundColor(Color.GREEN);
        fields[21].setBackGroundColor(Color.WHITE);
        fields[22].setBackGroundColor(Color.BLUE);
        fields[23].setBackGroundColor(Color.BLUE);

        //priser
        fields[1].setSubText("$1");
        fields[2].setSubText("$1");
        fields[4].setSubText("$2");
        fields[5].setSubText("$2");
        fields[7].setSubText("$2");
        fields[8].setSubText("$2");
        fields[10].setSubText("$2");
        fields[11].setSubText("$2");
        fields[13].setSubText("$3");
        fields[14].setSubText("$3");
        fields[16].setSubText("$3");
        fields[17].setSubText("$3");
        fields[19].setSubText("$4");
        fields[20].setTitle("$4");
        fields[22].setTitle("$5");
        fields[23].setTitle("$5");
    }*/

          /*GUI_Field[] fields = {
                new GUI_Start(),
                new GUI_Street(),
                new GUI_Street(),
                new GUI_Chance(),
                new GUI_Street(),
                new GUI_Street(),
                new GUI_Jail(),
                new GUI_Street(),
                new GUI_Street(),
                new GUI_Chance(),
                new GUI_Street(),
                new GUI_Street(),
                new GUI_Tax(),
                new GUI_Street(),
                new GUI_Street(),
                new GUI_Chance(),
                new GUI_Street(),
                new GUI_Street(),
                new GUI_Jail(),
                new GUI_Street(),
                new GUI_Street(),
                new GUI_Chance(),
                new GUI_Street(),
                new GUI_Street(),
        };*/
}