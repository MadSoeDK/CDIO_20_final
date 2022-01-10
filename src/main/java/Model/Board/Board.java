package Model.Board;

import Model.ChanceCardDeck;
import Model.Player;

import java.io.BufferedReader;
import java.io.FileReader;

public class Board {

    private final Field[] fields;

    public Board() {
        BufferedReader CSV;
        String row = "";
        String[][] data = new String[40][12];
        String path = "src/main/resources/da_board.csv";
        try {
            CSV = new BufferedReader(new FileReader(path));

            int i = 0;
            boolean firstline = true;
            while ((row = CSV.readLine()) != null) {

                if (firstline) {
                    firstline = false;
                    continue;
                }

                data[i] = row.split(",");

                i++;
            }
            CSV.close();

        } catch (Exception e) {
            System.out.println("Error");
        }

        fields = new Field[40];

        int name = 0, position = 1, type = 2, color = 3, price = 4, housePrice = 5;

        for (int i = 0; i < 40; i++) {
            String[] field = data[i];
            switch (field[type]) {
                case "street":
                    fields[Integer.parseInt(field[position])] = new Street (
                            field[name],
                            Integer.parseInt(field[position]),
                            field[color],
                            stringToIntarr(field,field[type]),
                            Integer.parseInt(field[price]),
                            Integer.parseInt(field[housePrice])
                    );
                    break;

                case "tax":
                    fields[Integer.parseInt(field[position])] = new Tax(
                            field[name],
                            Integer.parseInt(field[position]),
                            Integer.parseInt(field[price])
                    );
                    break;

                case "ferry":
                    fields[Integer.parseInt(field[position])] = new Ferry(
                            field[name],
                            Integer.parseInt(field[position]),
                            field[color],
                            stringToIntarr(field,field[type]),
                            Integer.parseInt(field[price])
                    );
                    break;

                case "brewery":
                    fields[Integer.parseInt(field[position])] = new Brewery(
                            field[name],
                            Integer.parseInt(field[position]),
                            field[color],
                            stringToIntarr(field,field[type]),
                            Integer.parseInt(field[price])
                    );
                    break;

                case "jail":
                    fields[Integer.parseInt(field[position])] = new Jail(
                            field[name],
                            Integer.parseInt(field[position])
                    );
                    break;

                case "chance":
                    fields[Integer.parseInt(field[position])] = new ChanceField(
                            field[name],
                            Integer.parseInt(field[position])
                    );
                    break;

                case "start":
                    fields[Integer.parseInt(field[position])] = new Start(
                            field[name],
                            Integer.parseInt(field[position])
                    );
                    break;

                case "FreeParking":
                    fields[Integer.parseInt(field[position])] = new FreeParking(
                            field[name],
                            Integer.parseInt(field[position])
                    );
                    break;
                default:
                    System.out.println("No field type match");
            }
        }
    }

    public Field getField(int placement) {
            return fields[placement];
    }

    public Field[] getFields() {
        return fields;
    }

    public int[] stringToIntarr(String[] array, String type) {
        int col = 6;
        int[] result;

        if (type.equals("street")) {
            result = new int[6];
        } else if (type.equals("ferry")) {
            result = new int[4];
        } else {
            result = new int[2];
        }
        for (int i = 0; i < result.length; i++){
            result[i] = Integer.parseInt(array[col + i]);
        }
        return result;
    }
    public int countNumbersOfPropertiesForPlayer(Player player) {
        int numberOfProperties = 0;
        for (int i = 0; i < getFields().length; i++) {

            //Type casting field to Ownable
            if (getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) getField(i);
                if (player == property.getOwner()) {
                    numberOfProperties++;
                }
            }
        }
        return numberOfProperties;
    }

    public int getFieldsByColor(int placement) {
        Ownable ownable = (Ownable) fields[placement];
        String color = ownable.getColor();
        int result = 0;
        for (Field field : fields) {
            Ownable property = (Ownable) field;
            if (property.getColor().equals(color)) {
                result++;
            }
        }
        return result;
    }

    public Brewery[] getBreweryFields() {
        Brewery[] breweryFields = new Brewery[2];

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof Brewery) {
                breweryFields[i] = (Brewery) fields[i];
            }
        }
        return breweryFields;
    }

    public Ferry[] getFerryFields() {
        Ferry[] ferryFields = new Ferry[4];

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] instanceof Brewery) {
                ferryFields[i] = (Ferry) fields[i];
            }
        }
        return ferryFields;
    }

    /*public Brewery[] addBreweryToField() {

    }*/

    public Ownable[] getPlayerProperties(Player player){
        int numberOfProperties;
        numberOfProperties = countNumbersOfPropertiesForPlayer(player);

        Ownable[] playerProperties = new Ownable[numberOfProperties];
        int currentProperty = 0;
        for (int i = 0; i < getFields().length; i++) {
            //Type casting field to Ownable
            if (getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) getField(i);
                if (player == property.getOwner()) {
                    playerProperties[currentProperty] = property;
                    currentProperty++;
                }
            }
        }

        return playerProperties;
    }
    /*public boolean hasMonopoly(int placement) {


        Property property = (Property) getField(placement);

        boolean monopoly = false;

        // Check if both fields of same color is owned by the same player
        if (placement - 2 < 0) {
            for (int i = 0; i < (placement + 3); i++) {
                // Check 2 field in either direction
                if (getField(i) instanceof Property) {

                    // Typecast to Property
                    Property property_check = (Property) getField(i);

                    // Check if owner/color is the same
                    if (property_check.getColor() == property.getColor() && property_check.getOwner() == property.getOwner()) {
                        monopoly = true;
                    }
                }
            }
        } else {
            for (int i = placement - 2; i < placement + 3; i++) {
                // Check 2 field in either direction
                if (i < 24) {
                    if (getField(i) instanceof Property) {
                        // Typecast to Property
                        Property property_check = (Property) getField(i);
                        // Check if owner/color is the same
                        if (property_check.getColor() == property.getColor() && property_check.getOwner() == property.getOwner()) {
                            monopoly = true;
                        }
                    }
                }
            }
        }
        return monopoly;
    }*/
}