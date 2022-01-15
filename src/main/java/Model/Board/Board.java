package Model.Board;

import Model.Player;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Objects;

public class Board {

    private final Field[] fields;
    private final Monopoly[] monopolies;

    /**
     * Load and initilize fields
     */
    public Board() {
        BufferedReader CSV;
        String row;
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

        createBoard(data);

        String[] streetColors = {"blue", "orange", "green", "grey", "red", "teal", "yellow", "purple"};

        monopolies = new Monopoly[streetColors.length];

        for (int i = 0; i < streetColors.length; i++) {
            switch (streetColors[i]) {
                case "blue":
                    Street[] bluestreets = getStreetsByColor(streetColors[i]);
                    monopolies[i] = new Monopoly("Blå", Color.blue, bluestreets);
                    break;
                case "orange":
                    Street[] orangestreets = getStreetsByColor(streetColors[i]);
                    monopolies[i] = new Monopoly("Orange", Color.orange, orangestreets);
                    break;
                case "green":
                    Street[] greenstreets = getStreetsByColor(streetColors[i]);
                    monopolies[i] = new Monopoly("Lysegrøn", Color.green, greenstreets);
                    break;
                case "grey":
                    Street[] dgreenstreets = getStreetsByColor(streetColors[i]);
                    monopolies[i] = new Monopoly("Mørkegrøn", Color.gray, dgreenstreets);
                    break;
                case "red":
                    Street[] redstreets = getStreetsByColor(streetColors[i]);
                    monopolies[i] = new Monopoly("Rød", Color.red, redstreets);
                    break;
                case "teal":
                    Street[] lightbluestreets = getStreetsByColor(streetColors[i]);
                    monopolies[i] = new Monopoly("Lyseblå", Color.white, lightbluestreets);
                    break;
                case "yellow":
                    Street[] yellowstreets = getStreetsByColor(streetColors[i]);
                    monopolies[i] = new Monopoly("Gul", Color.yellow, yellowstreets);
                    break;
                case "purple":
                    Street[] purplestreets = getStreetsByColor(streetColors[i]);
                    monopolies[i] = new Monopoly("Lilla", Color.magenta, purplestreets);
                    break;
            }
        }
    }

    public void createBoard(String[][] data) {
        int name = 0, position = 1, type = 2, color = 3, price = 4, housePrice = 5;

        for (int i = 0; i < 40; i++) {
            String[] field = data[i];
            switch (field[type]) {
                case "street":
                    fields[Integer.parseInt(field[position])] = new Street(
                            field[name],
                            Integer.parseInt(field[position]),
                            field[color],
                            stringToIntarr(field, field[type]),
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
                            stringToIntarr(field, field[type]),
                            Integer.parseInt(field[price])
                    );
                    break;

                case "brewery":
                    fields[Integer.parseInt(field[position])] = new Brewery(
                            field[name],
                            Integer.parseInt(field[position]),
                            field[color],
                            stringToIntarr(field, field[type]),
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
                    fields[Integer.parseInt(field[position])] = new ChanceField(field[name],
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

    public Street[] getStreetsByColor(String color) {
        Street[] streets = new Street[getFields().length];
        int j = 0;
        for (int i = 0; i < getFields().length; i++) {
            if ((getFields()[i]) instanceof Street) {
                Street street = (Street) getFields()[i];
                if (street.getColor().equals(color)) {
                    streets[j] = (Street) getFields()[i];
                    j++;
                }
            }
        }
        Street[] result = new Street[j];
        System.arraycopy(streets, 0, result, 0, j);
        return result;
    }

    public Field getField(int placement) {
        return fields[placement];
    }

    public Field[] getFields() {
        return fields;
    }

    /**
     * Returns rent as a integer array
     *
     * @param array
     * @param type
     * @return int rent array
     */
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
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(array[col + i]);
        }
        return result;
    }

    public int PropertiesForPlayer(Player player) {
        int numberOfProperties = 0;
        for (int i = 0; i < getFields().length; i++) {

            //Type casting field to Ownable
            if (getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) getField(i);
                if (player == property.getOwner() && !property.getMortgage()) {
                    numberOfProperties++;
                }
            }
        }
        return numberOfProperties;
    }

    public int MortgagedPropertiesForPlayer(Player player) {
        int numberOfMortgagedProperties = 0;
        for (int i = 0; i < getFields().length; i++) {
            //Type casting field to Ownable
            if (getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) getField(i);
                if (player == property.getOwner()) {
                    //if property is mortgaged count++
                    if (property.getMortgage()) {
                        numberOfMortgagedProperties++;
                    }
                }
            }
        }
        return numberOfMortgagedProperties;
    }

    public int countNumbersOfPropertiesWithHouseForPlayer(Player player) {
        int numberOfPropertiesWithHouse = 0;
        for (int i = 0; i < getFields().length; i++) {

            //Type casting field to Ownable
            if (getField(i) instanceof Street) {
                //Verifying that the current field is of the type Ownable
                Street property = (Street) getField(i);
                if (player == property.getOwner()) {
                    if (property.getHouseAmount() > 0) {
                        numberOfPropertiesWithHouse++;
                    }
                }
            }
        }
        return numberOfPropertiesWithHouse;
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

    public Ownable[] getPlayerProperties(Player player) {
        int numberOfProperties;
        numberOfProperties = PropertiesForPlayer(player) + MortgagedPropertiesForPlayer(player);

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

    public boolean hasMonopoly(int placement) {
        Street property = (Street) getField(placement);
        boolean monopoly = false;
        // Check if both fields of same color is owned by the same player
        if (placement - 2 < 0) {
            for (int i = 0; i < (placement + 3); i++) {
                // Check 2 field in either direction
                if (getField(i) instanceof Street) {

                    // Typecast to Property
                    Street property_check = (Street) getField(i);

                    // Check if owner/color is the same
                    if (Objects.equals(property_check.getColor(), property.getColor()) && property_check.getOwner() == property.getOwner()) {
                        monopoly = true;
                    }
                }
            }
        } else {
            for (int i = placement - 2; i < placement + 3; i++) {
                // Check 2 field in either direction
                if (i < 24) {
                    if (getField(i) instanceof Street) {
                        // Typecast to Property
                        Street property_check = (Street) getField(i);
                        // Check if owner/color is the same
                        if (Objects.equals(property_check.getColor(), property.getColor()) && property_check.getOwner() == property.getOwner()) {
                            monopoly = true;
                        }
                    }
                }
            }
        }
        return monopoly;
    }

    public String getStreetColor(Street street) {
        return street.getColor();
    }

    public Monopoly[] getMonopolies() {
        return monopolies;
    }

    public boolean getPlayerOwnsMonopoly(Player player) {
        boolean playerOwnsMonopoly = false;

        for (Monopoly monopoly : monopolies) {
            monopoly.updateMonopoly();
            if (monopoly.getOwner() == player) {
                playerOwnsMonopoly = true;
            }
        }
        return playerOwnsMonopoly;
    }

    public Monopoly[] getOwnedPlayerMonopolyList(Player player) {
        int playerMonopolyAmount = 0;

        // Check amount of Monopolies owned
        for (Monopoly monopoly : monopolies) {
            monopoly.updateMonopoly();
            if (monopoly.getOwner() == player) {
                playerMonopolyAmount++;
            }
        }

        // Create array with owned arrays
        Monopoly[] playerOwnedMonopolyList = new Monopoly[playerMonopolyAmount];
        int playerOwnedIndex = 0;
        for (Monopoly monopoly : monopolies) {
            monopoly.updateMonopoly();
            if (monopoly.getOwner() == player) {
                playerOwnedMonopolyList[playerOwnedIndex] = monopoly;
                System.out.println(playerOwnedMonopolyList[playerOwnedIndex].getName());
                playerOwnedIndex++;
            }
        }

        return playerOwnedMonopolyList;
    }
}