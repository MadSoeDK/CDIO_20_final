package Model;

import java.awt.*;

public class Monopoly {

    private Color color;
    private boolean monopolyTrue = false;
    private int monopolySize = 3;
    private Board board;
    private Street[] streets = new Street[2];

    Monopoly (Color color, Board board)
    {
        this.color = color;
        this.board = board;

        // Set Size Exception
        if (color == Color.MAGENTA || color == Color.BLUE){
            monopolySize = 2;
        }
        //Street[] streets = new Street[monopolySize-1];
        // Create Street array
        if (color == Color.blue)
        {
            //streets = new Street[2];
            streets[0] = (Street) board.getField(1);
            streets[1] = (Street) board.getField(3);

            for (int p=0; p<streets.length; p++)
            {
                //System.out.println(streets[1].getName());
            }
        }
    }


    private void updateMonopoly(){

    }
}