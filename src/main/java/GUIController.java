import Model.ChanceField;
import Model.Field;
import Model.Player;
import Model.Property;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;

public class GUIController {

    private GUI gui;
    private GUI_Player[] guiPlayers;
    private String[] playernames;
    private GUI_Field[] guiFields;
    private final Color[] colors = {Color.RED, Color.WHITE, Color.ORANGE, Color.MAGENTA};

    public GUIController(Field[] fields) {
        GUI_Field[] guiBoard = createBoard(fields);
        gui = new GUI(guiBoard);
    }

    public GUI_Field[] createBoard(Field[] fields) {
        guiFields = new GUI_Field[fields.length];

        for (int i = 0; i < fields.length; i++) {
            switch (fields[i].getClass().getSimpleName()) {
                case "Start":
                    guiFields[i] = new GUI_Start();
                    break;
                case "Property":
                    guiFields[i] = new GUI_Street();
                    ((GUI_Ownable)guiFields[i]).setRent(((Property) fields[i]).getRent() + "$");
                    guiFields[i].setBackGroundColor(((Property) fields[i]).getColor());
                    break;
                case "ChanceField":
                    guiFields[i] = new GUI_Chance();
                    guiFields[i].setBackGroundColor(Color.WHITE);
                    break;
                case "Jail":
                    guiFields[i] = new GUI_Jail();
                    break;
                case "Ferry":
                    guiFields[i] = new GUI_Shipping();
                    guiFields[i].setBackGroundColor(Color.WHITE);
                    break;
                case "FreeParking":
                    guiFields[i] = new GUI_Tax();
                    guiFields[i].setBackGroundColor(Color.WHITE);
                    break;
                case "Tax":
                    guiFields[i] = new GUI_Tax();
                    guiFields[i].setBackGroundColor(Color.ORANGE);
                    break;
            }
            //guiFields[i].setTitle(fields[i].getName());
            //guiFields[i].setDescription(fields[i].getName());
        }
        return guiFields;
    }

    public void createPlayers() {
        int qty = Integer.parseInt(gui.getUserSelection("How many players?", "2", "3", "4"));

        guiPlayers = new GUI_Player[qty];

        for (int i = 0; i < qty; i++) {
            gui.showMessage("Indtast spiller navn: ");

            GUI_Car playerCar = new GUI_Car(colors[i], colors[i], GUI_Car.Type.TRACTOR, GUI_Car.Pattern.FILL);

            guiPlayers[i] = new GUI_Player(gui.getUserString(""), 10, playerCar);

            gui.addPlayer(guiPlayers[i]);

            GUI_Field field = gui.getFields()[0];
            field.setCar(guiPlayers[i], true);
        }
    }

    public void movePlayer(Player player, int prepos, int pospos) {
        //guiFields[placement].setCar(guiPlayers[currentPlayer], true);

        GUI_Player playerToMove = new GUI_Player("");
        GUI_Field to = gui.getFields()[prepos];

        for (int i = 0; i < guiPlayers.length; i++) {
            if (guiPlayers[i].getName().equals(player.getName())) {
                playerToMove = guiPlayers[i];

                GUI_Field from = gui.getFields()[pospos];

                from.setCar(playerToMove, false);
            }
        }
        to.setCar(playerToMove, true);

    }

    public void showDie(int sum) {
        gui.setDie(sum);
    }

    public void Button(String msg, String buttonText) {
        gui.getUserButtonPressed(msg, buttonText);
    }

    public void guiMessage(String message){
        gui.showMessage(message);
    }

    public GUI_Field getGuiField(int placement) {
        return guiFields[placement];
    }

    public String[] getPlayernames() {
        playernames = new String[guiPlayers.length];
        for (int i = 0; i < guiPlayers.length; i++) {
            playernames[i] = guiPlayers[i].getName();
        }
        return playernames;
    }

    public GUI_Player getGuiPlayer(Player currentplayer) {
        GUI_Player guiplayer = null;
        for (int i = 0; i < guiPlayers.length; i++) {
            if (currentplayer.getName().equals(playernames[i])) {
                guiplayer = guiPlayers[i];
            }
        }
        return guiplayer;
    }

    public void setguiPlayerBalance(Player player, int amount) {
        for (int i = 0; i < playernames.length; i++) {
            if(player.getName().equals(playernames[i])) {
                guiPlayers[i].setBalance(amount);
            }
        }
    }

    public Color getPlayerColor(Player player) {
        Color color = new Color(0);
        for (int i = 0; i < playernames.length; i++) {
            if (player.getName().equals(playernames[i])) {
                color = guiPlayers[i].getPrimaryColor();
            }
        }
        return color;
    }
}
