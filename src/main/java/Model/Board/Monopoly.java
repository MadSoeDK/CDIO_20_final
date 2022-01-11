package Model.Board;

import Model.Board.Board;
import Model.Board.Street;
import Model.Player;

import java.awt.*;

public class Monopoly {

    String name;
    private Color color;
    private boolean monopolyTrue = false;
    private Player monopolyOwner;
    private int monopolySize = 3;
    private Board board;
    private Street[] streets;

    public Monopoly(String name, Color color, Board board, int firstHouse, int secHouse, int thirdHouse)
    {
        this.name = name;
        this.color = color;
        this.board = board;

        // Set Size Exception
        if (color == Color.MAGENTA || color == Color.BLUE){
            this.monopolySize = 2;
        }
        //Street[] streets = new Street[monopolySize-1];
        streets = new Street[monopolySize];
        // Create Street array
        if (color == Color.blue || color == Color.magenta)
        {
            streets[0] = (Street) board.getField(firstHouse);
            streets[1] = (Street) board.getField(secHouse);

            for (int p=0; p<streets.length; p++)
            {
                //System.out.println(color.toString()+streets[p].getName());
            }
            System.out.println();
        }
        else
        {
            streets[0] = (Street) board.getField(firstHouse);
            streets[1] = (Street) board.getField(secHouse);
            streets[2] = (Street) board.getField(thirdHouse);

            for (int p=0; p<streets.length; p++)
            {
                //System.out.println(color.toString()+streets[p].getName());
            }
            System.out.println();
        }

    }

    public Street[] getStreetArray(){

        return streets;
    }

    /*public Street[] getStreet(){

        return streets;
    }*/

    public Player getOwner(){
        return monopolyOwner;
    }

    public void updateMonopoly()
    {
        if (streets.length == 2) // If the monopoly is Blue or Magenta
        {
            if (streets[0].getOwner() == streets[1].getOwner() && streets[0].getOwner() != null)
            {
                monopolyTrue=true;
                monopolyOwner=streets[0].getOwner();
            }
            else{
                monopolyTrue=false;
                monopolyOwner=null;
            }
        }
        else{

            if (streets[0].getOwner() == streets[1].getOwner() && streets[1].getOwner() == streets[2].getOwner() && streets[0].getOwner() != null)
            {
                monopolyTrue=true;
                monopolyOwner=streets[0].getOwner();
            }
            else{
                monopolyTrue=false;
                monopolyOwner=null;
            }
        }
    }

    public String getName(){
        return name;
    }

    public String[] getStringArray(){
        String[] streetsString = new String[monopolySize];

        for (int i=0; i < streetsString.length; i++)
        {
            streetsString[i] = streets[i].getName();
        }

        return streetsString;
    }
}
