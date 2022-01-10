import Model.Field;
import Model.Ownable;
import Model.Player;
//import Model.Property;
import Model.Street;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;

public class GUIController {

    private GUI gui;
    private GUI_Player[] guiPlayers;
    private String[] playernames;
    private GUI_Field[] guiFields;
    private final Color[] colors = {Color.RED, Color.WHITE, Color.ORANGE, Color.MAGENTA, Color.BLACK, Color.BLUE};

    public GUIController(Field[] fields) {
        GUI_Field[] guiBoard = createBoard(fields);
        gui = new GUI(guiBoard,Color.orange);
    }

    public GUI_Field[] createBoard(Field[] fields) {
        guiFields = new GUI_Field[fields.length];

        for (int i = 0; i < fields.length; i++) {
            switch (fields[i].getClass().getSimpleName()) {
                case "Start":
                    guiFields[i] = new GUI_Start();
                    break;
                case "Street":
                    guiFields[i] = new GUI_Street();
                    //((GUI_Ownable)guiFields[i]).setRent(((Ownable) fields[i]).getCurrentRent() + "kr.");
                    guiFields[i].setBackGroundColor(convertColor(((Street) fields[i]).getColor()));
                    guiFields[i].setSubText("Pris: " + ((Ownable) fields[i]).getPrice() + " kr.");
                    break;
                case "ChanceField":
                    guiFields[i] = new GUI_Chance();
                    guiFields[i].setSubText("");
                    guiFields[i].setBackGroundColor(Color.BLACK);
                    guiFields[i].setForeGroundColor(Color.GREEN);
                    break;
                case "Jail":
                    guiFields[i] = new GUI_Jail();
                    //guiFields[i].setSubText("");
                    break;
                case "Ferry":
                    guiFields[i] = new GUI_Shipping();
                    guiFields[i].setBackGroundColor(Color.WHITE);
                    guiFields[i].setSubText("Pris: " + ((Ownable) fields[i]).getPrice() + " kr.");
                    break;
                case "FreeParking":
                    guiFields[i] = new GUI_Tax();
                    guiFields[i].setBackGroundColor(Color.WHITE);
                    guiFields[i].setSubText("");
                    break;
                case "Tax":
                    guiFields[i] = new GUI_Tax();
                    guiFields[i].setBackGroundColor(Color.GRAY);
                    guiFields[i].setSubText("");
                    break;
                case "Brewery":
                    guiFields[i] = new GUI_Brewery();
                    guiFields[i].setSubText("Pris: " + ((Ownable) fields[i]).getPrice() + " kr.");
                    break;
            }
            guiFields[i].setTitle(fields[i].getName());
            guiFields[i].setDescription(fields[i].getName());
        }
        guiFields[10].setSubText("Besøg");
        guiFields[30].setSubText("Gå i fængsel");
        return guiFields;
    }

    public void createPlayers(int STARTBALANCE) {
        int qty = Integer.parseInt(gui.getUserSelection("How many players?", "3", "4", "5", "6"));

        guiPlayers = new GUI_Player[qty];

        for (int i = 0; i < qty; i++) {
            gui.showMessage("Indtast spiller navn: ");

            GUI_Car playerCar = new GUI_Car(colors[i], colors[i], GUI_Car.Type.UFO, GUI_Car.Pattern.FILL);

            guiPlayers[i] = new GUI_Player(gui.getUserString(""), STARTBALANCE, playerCar);

            gui.addPlayer(guiPlayers[i]);

            GUI_Field field = gui.getFields()[0];
            field.setCar(guiPlayers[i], true);
        }
    }

    public void removePlayer(Player player, int placement, Player[] players) {
        /*
        - Remove one GUI_Player from GUI_Player array
        - Make new GUI_Player array with new players who are still playing
        */

        GUI_Player[] tempguiPlayers = getGuiPlayers();
        GUI_Player[] guiPlayers = new GUI_Player[players.length];
        int j = 0;
        for (int i = 0; i < guiPlayers.length; i++) {
            if(!players[i].getBankruptStatus()) {
                guiPlayers[j] = tempguiPlayers[i];
                j++;
            }
        }
        if(player.getBankruptStatus()) {
            GUI_Field field = gui.getFields()[placement];
            field.setCar(getGuiPlayer(player), false);
        }
    }

    public void movePlayer(Player player, int placement, int preplacement) {

        GUI_Player playerToMove = new GUI_Player("");
        GUI_Field to = gui.getFields()[placement];


        // Get the GUI Player
        for (int i = 0; i < guiPlayers.length; i++) {

            if (guiPlayers[i].getName().equals(player.getName())) {

                playerToMove = guiPlayers[i];

                //Remove from previus position
                GUI_Field from = gui.getFields()[preplacement];

                from.setCar(playerToMove, false);

            }

        }
        to.setCar(playerToMove, true);
    }

    public void showDice(int fv1, int fv2) {
        //gui.setDie(sum);
        gui.setDice(fv1, fv2);
    }

    public void button(String msg, String buttonText) {
        gui.getUserButtonPressed(msg, buttonText);
    }

    public boolean getUserBool(String msg, String left, String right) {
        boolean answer = gui.getUserLeftButtonPressed(msg, left, right);
        if (answer) {
            return answer;
        } else {
            return answer;
        }
    }

    public String getUserSelection(String msg, String[] options){
        return gui.getUserSelection(msg, options);
    }

    public void message(String message){
        gui.showMessage(message);
    }

    public String dropdown(String msg, String[] option) {
        return gui.getUserSelection(msg, option);
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
    public GUI_Player[] getGuiPlayers() {
        return guiPlayers;
    }
    public int getPlayers() {
        return guiPlayers.length;
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

    public void setOwner(Player player, Ownable field) {

        int placement = field.getPlacement();

        switch (field.getClass().getSimpleName()) {
            case "Street":
                GUI_Street street = (GUI_Street) getGuiField(placement);
                street.setOwnerName(player.getName());
                street.setBorder(getPlayerColor(player));
                street.setRent((field.getCurrentRent() + " kr."));
                break;
            case "Ferry":
                GUI_Shipping ferry = (GUI_Shipping) getGuiField(placement);
                ferry.setOwnerName(player.getName());
                ferry.setBorder(getPlayerColor(player));
                ferry.setRent((field.getCurrentRent() + " kr."));
                break;
            case "Brewery":
                GUI_Brewery brewery = (GUI_Brewery) getGuiField(placement);
                brewery.setOwnerName(player.getName());
                brewery.setBorder(getPlayerColor(player));
                brewery.setRent((field.getCurrentRent() + " kr."));
                break;
            default:
                //((GUI_Ownable) getGuiField(field.getPlacement())).setRent(((Ownable) field).getCurrentRent() + "kr.");
                System.out.println("Not a ownable");
        }
    }

    private static Color convertColor(String color) {
        Color result = Color.white;

        switch (color.toLowerCase()) {
            case "red" :
                result = Color.red;
                break;
            case "green" :
                result = Color.green;
                break;
            case "blue" :
                result = new Color(45, 137, 239);
                break;
            case "yellow" :
                result = Color.yellow;
                break;
            case "purple" :
                result = new Color(255, 90, 255);
                break;
            case "turquoise" :
                result = new Color(0,255,239);
                break;
            case "magenta" :
                result = new Color(255,0,151);
                break;
            case "orange" :
                result = new Color(235,97,35);
                break;
        }

        return result;
    }
}
