import gui_fields.GUI_Chance;
import gui_fields.GUI_Field;
import gui_fields.GUI_Jail;
import gui_fields.GUI_Start;
import gui_main.GUI;
public class Board {
    static void iniGUI() {
        GUI_Field[] fields = {
                new GUI_Start(),
                new GUI_Jail(),
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
    }
    static void iniPlayer(int playerAmount) {
        if(playerAmount >= 2 && playerAmount <= 4) {
            int[] players = new int[playerAmount];

            for(int i = 0; i < players.length; i++) {

            }
        }
    }
}