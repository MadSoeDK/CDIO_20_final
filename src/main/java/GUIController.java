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
    public void showDie(int sum) {
        gui.setDie(sum);
    }

    public void Button(String msg, String buttonText) {
        gui.getUserButtonPressed(msg, buttonText);
    }

}
