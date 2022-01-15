package Model.Board;

import Model.Player;

import java.awt.*;

public class Monopoly {

    private final String name;
    private Color color;
    private boolean monopolyTrue = false;
    private Player monopolyOwner;
    private final Street[] streets;
    private final int monopolySize;

    public Monopoly(String name, Color color, Street[] streets) {
        this.name = name;
        this.color = color;
        this.streets = streets;
        monopolySize = streets.length;
    }

    public Street[] getStreetArray() {
        return streets;
    }

    public Player getOwner() {
        return monopolyOwner;
    }

    public void updateMonopoly() {
        monopolyTrue = true;
        monopolyOwner = streets[0].getOwner();
        for (int i = 0; i < streets.length; i++) {
            if (monopolyOwner != streets[i].getOwner()) {
                monopolyTrue = false;
                monopolyOwner = null;
                break;
            } else {
                monopolyTrue = true;
            }
        }

        if (monopolyTrue && streets[0].getOwner() != null) {
            monopolyOwner = streets[0].getOwner();
        }
    }

    public String getName() {
        return name;
    }

    public String[] getStringArray() {
        String[] streetsString = new String[monopolySize];

        for (int i = 0; i < streetsString.length; i++) {
            streetsString[i] = streets[i].getName();
        }
        return streetsString;
    }
}
