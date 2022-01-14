package Model.Board;

import Model.Player;

import java.awt.*;

public class Monopoly {

    private String name;
    private Color color;
    private boolean monopolyTrue = false;
    private Player monopolyOwner;
    private final Street[] streets;
    private int monopolySize;

    public Monopoly(String name, Color color, Street[] streets/*Board board, int firstHouse, int secHouse, int thirdHouse*/) {
        this.name = name;
        this.color = color;
        this.streets = streets;
        monopolySize = streets.length;
    }

    public Street[] getStreetArray() {
        return streets;
    }

    public Player getOwner(){
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

        /*if (streets.length == 2) {// If the monopoly is Blue or Magenta
            if (streets[0].getOwner() == streets[1].getOwner() && streets[0].getOwner() != null) {
                monopolyTrue=true;
                monopolyOwner=streets[0].getOwner();
            } else {
                monopolyTrue=false;
                monopolyOwner=null;
            }
        }
        else {

            if (streets[0].getOwner() == streets[1].getOwner() && streets[1].getOwner() == streets[2].getOwner() && streets[0].getOwner() != null) {
                monopolyTrue=true;
                monopolyOwner=streets[0].getOwner();
            } else {
                monopolyTrue=false;
                monopolyOwner=null;
            }
        }*/
    }

    public String getName(){
        return name;
    }

    public String[] getStringArray() {
        String[] streetsString = new String[monopolySize];

        for (int i=0; i < streetsString.length; i++) {
            streetsString[i] = streets[i].getName();
        }
        return streetsString;
    }
}
