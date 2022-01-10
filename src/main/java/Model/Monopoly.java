package Model;

import java.awt.*;

public class Monopoly {

    private Color color;
    private boolean monopolyTrue = false;
    private int monopolySize = 3;
    private Board board;
    //private Field[] streets;

    Monopoly (Color color, Board board){
        this.color = color;

        // Set Size Exception
        if (color == Color.MAGENTA || color == Color.BLUE){
            monopolySize = 2;
        }

        //Field streets = (Street) this.streets;

        // Create Street array
        if (color == Color.blue)
        {
            Street[] streets = {
                    (Street) board.getField(1),
                    (Street) board.getField(3)
            };
            for (int p=0; p<streets.length; p++)
            {
                System.out.println(streets[p].getName());
            }
        }


    }


    private void updateMonopoly(){

    }
}