import gui_fields.*;
import gui_main.GUI;
import java.lang.Math;
public class Board {
        GUI_Field[] fields = {
                new GUI_Start(),
                new GUI_Brewery(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Chance(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),
                new GUI_Jail(),

        };
        GUI gui = new GUI(fields);
        int[] cards = new int[100];

        void createPlayer() {
            gui.showMessage("Hvor mange spillere?");
            int numberOfPlayers = gui.getUserInteger("Indtast et tal.");
            int[] players = new int[numberOfPlayers];

            for(int i = 0; i < players.length; i++) {
                gui.showMessage("Vælg en spiller: ");
                String name = gui.getUserString("");
                GUI_Player player = new GUI_Player(name, 2000);
                gui.addPlayer(player);
            }
        }
        void getPlayer() {

        }
        void getPlayers() {

        }
        void getFields() {

        }
        void shuffleCard() {

        }
        void getCard() {

        }
    }