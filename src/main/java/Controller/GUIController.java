package Controller;

import Model.Board.Field;
import Model.Board.Ownable;
import Model.Player;
import Model.Board.Street;
import Utility.Language;
import gui_fields.*;
import gui_main.GUI;
import Model.Cards.*;

import java.awt.*;
import java.util.Random;

public class GUIController {

    private final GUI gui;
    private GUI_Player[] guiPlayers;
    private String[] playernames;
    private GUI_Field[] guiFields;
    private final Color[] colors = {new Color(180, 0, 0), new Color(0, 90, 0), new Color(0, 0, 180), new Color(210, 9, 214), new Color(0, 209, 206), new Color(255, 106, 0)};

    public GUIController(Field[] fields) {
        GUI_Field[] guiBoard = createBoard(fields);
        gui = new GUI(guiBoard, new Color(112, 171, 79));
        chooseLanguage();
    }

    public void chooseLanguage() {
        String[] options = {"Danish", "English"};
        String language = getUserSelection("Select language - (English version not finished)", options);
        Language.setLanguage(language);
        Language.read();
    }

    public GUI_Field[] createBoard(Field[] fields) {
        guiFields = new GUI_Field[fields.length];

        for (int i = 0; i < fields.length; i++) {
            switch (fields[i].getClass().getSimpleName()) {
                case "Start":
                    guiFields[i] = new GUI_Start();
                    guiFields[i].setBackGroundColor(Color.WHITE);
                    guiFields[i].setSubText("");
                    break;
                case "Street":
                    guiFields[i] = new GUI_Street();
                    guiFields[i].setBackGroundColor(convertColor(((Street) fields[i]).getColor()));
                    guiFields[i].setSubText("Pris: " + ((Ownable) fields[i]).getPrice() + " kr.");
                    break;
                case "ChanceField":
                    guiFields[i] = new GUI_Chance();
                    break;
                case "Jail":
                    guiFields[i] = new GUI_Jail();
                    guiFields[i].setBackGroundColor(Color.LIGHT_GRAY);
                    break;
                case "Ferry":
                    guiFields[i] = new GUI_Shipping();
                    guiFields[i].setBackGroundColor(Color.WHITE);
                    guiFields[i].setSubText("Pris: " + ((Ownable) fields[i]).getPrice() + " kr.");
                    break;
                case "FreeParking":
                    guiFields[i] = new GUI_Refuge();
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
            if (!(guiFields[i] instanceof GUI_Chance)) {
                guiFields[i].setTitle(fields[i].getName());
            }
            guiFields[i].setDescription(fields[i].getName());
        }
        guiFields[10].setSubText("Besøg");
        guiFields[30].setSubText("Gå i fængsel");
        return guiFields;
    }

    public void createPlayers(int STARTBALANCE) {
        int qty = Integer.parseInt(gui.getUserSelection(Language.getText("createPlayers1"), "3", "4", "5", "6"));

        guiPlayers = new GUI_Player[qty];

        for (int i = 0; i < qty; i++) {
            gui.showMessage(Language.getText("createPlayers2"));

            // Get random car type
            Random rand = new Random();
            int carOptions = rand.nextInt(4);
            GUI_Car.Type carOptionChosen = GUI_Car.Type.TRACTOR;
            switch (carOptions) {
                case 0:
                    carOptionChosen = GUI_Car.Type.CAR;
                    break;
                case 1:
                    carOptionChosen = GUI_Car.Type.RACECAR;
                    break;
                case 2:
                    carOptionChosen = GUI_Car.Type.TRACTOR;
                    break;
                case 3:
                    carOptionChosen = GUI_Car.Type.UFO;
                    break;
            }

            GUI_Car playerCar = new GUI_Car(colors[i], colors[i], carOptionChosen, GUI_Car.Pattern.FILL);

            guiPlayers[i] = new GUI_Player(gui.getUserString(""), STARTBALANCE, playerCar);

            gui.addPlayer(guiPlayers[i]);

            GUI_Field field = gui.getFields()[0];
            field.setCar(guiPlayers[i], true);
        }
    }

    public void removePlayer(Player player, int placement) {
        GUI_Field field = gui.getFields()[placement];
        field.setCar(getGuiPlayer(player), false);
    }

    public void movePlayer(Player player, int placement, int preplacement) {

        GUI_Player playerToMove = new GUI_Player("");
        GUI_Field to = gui.getFields()[placement];


        // Get the GUI Player
        for (GUI_Player guiPlayer : guiPlayers) {

            if (guiPlayer.getName().equals(player.getName())) {

                playerToMove = guiPlayer;

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

    public String getUserSelection(String msg, String[] options) {
        return gui.getUserSelection(msg, options);
    }

    public void message(String message) {
        gui.showMessage(message);
    }

    public String dropdown(String msg, String[] option) {
        return gui.getUserSelection(msg, option);
    }

    public GUI_Field getGuiField(int placement) {
        return guiFields[placement];
    }

    public void setGuiHouseAmount(int placement, int houseAmount) {

        // Typecast to Street
        GUI_Field field = gui.getFields()[placement];
        GUI_Street street = (GUI_Street) field;

        if (houseAmount == 5) {
            street.setHouses(0);
            street.setHotel(true);
        } else {
            street.setHouses(houseAmount);
            //street.setHotel(false);
        }

    }

    public String[] getPlayernames() {
        playernames = new String[guiPlayers.length];
        for (int i = 0; i < guiPlayers.length; i++) {
            playernames[i] = guiPlayers[i].getName();
        }
        return playernames;
    }

    public GUI_Field[] getGuiFields() {
        return guiFields;
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
            if (player.getName().equals(playernames[i])) {
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
                System.out.println("Not a ownable");
        }
    }

    private static Color convertColor(String color) {
        Color result = Color.white;

        switch (color.toLowerCase()) {
            case "red":
                result = new Color(255, 80, 80);
                break;
            case "green":
                result = new Color(94, 255, 110);
                break;
            case "blue":
                result = new Color(45, 137, 239);
                break;
            case "yellow":
                result = new Color(242, 236, 51);
                break;
            case "purple":
                result = new Color(185, 105, 200);
                break;
            case "turquoise":
                result = new Color(0, 255, 239);
                break;
            case "magenta":
                result = new Color(255, 0, 151);
                break;
            case "orange":
                result = new Color(235, 97, 35);
                break;
            case "grey":
                result = new Color(13, 138, 17);
                break;
            case "teal":
                result = new Color(8, 230, 216);
                break;
        }

        return result;
    }

    public void updateFieldRent(int placement, int rent) {

        // Typecast to Street
        GUI_Field field = gui.getFields()[placement];
        GUI_Street street = (GUI_Street) field;

        street.setRent(" " + rent);
        //street.setSubText("Leje: " + rent);
    }

    public void setChanceCard(ChanceCard card) {
        gui.displayChanceCard(card.getDescription());
    }

    public void setSubText(String subText, Field field) {
        GUI_Field gui_field = getGuiField(field.getPlacement());
        gui_field.setSubText(subText);
    }
}
