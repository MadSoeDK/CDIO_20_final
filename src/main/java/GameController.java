import Model.*;
import Model.Board.*;

import java.util.Objects;

public class GameController {

    private Board board;
    private Cup cup;
    private GUIController gui;
    private EventHandler event;
    private Player[] players;
    private Player currentPlayer;
    private int playerindex = 0;

    // Game Constants
    final int STARTBALANCE = 30000;

    int sum;

    public GameController() {
        board = new Board();
        cup = new Cup();
        gui = new GUIController(board.getFields());
        event = new EventHandler(gui);
        gui.createPlayers(STARTBALANCE);
        setupPlayers(gui.getPlayernames());
        playGame();
    }

    public void playGame() {
        do {
            gui.message("Nu er det " + currentPlayer.getName() + "'s tur");
            takeTurn();
            nextTurn();
        } while (true); //Check winner
    }

    public void takeTurn() {
        // Ask if currentplayer wish to trade?
        event.playerOptions(currentPlayer, players, board);

        // Roll die, get value, show die
        cup.roll();
        sum = 1; //cup.getSum();
        gui.showDice(cup.getFacevalues()[0], cup.getFacevalues()[1]);

        // Move player placement - automatically updates GUI
        moveplayer(currentPlayer, sum);

        int placement = currentPlayer.getPlacement();

        Field field = board.getField(placement);

        gui.message(currentPlayer.getName() + " landede på " + field.getName());

        checkFieldType(field, placement);

        // Ask about building houses?
        if (board.getPlayerOwnsMonopoly(currentPlayer))
        {
            if (gui.getUserBool("Vil du bygge på din grund","Jeg vil bygge","Jeg vil IKKE bygge")){
                // Give player option over monopolies
                board.getOwnedPlayerMonopolyList(currentPlayer);
                //gui.dropdown("Hvilke grunde vil du bygge på?","Blå");
            }
        }

        //event.auction(2000, players);

        //Update GUI players balance
        for (Player p : players) {
            gui.setguiPlayerBalance(p, p.getPlayerBalance());
        }

        netWorth(currentPlayer);
        bankrupt(currentPlayer, placement);

    }

    public void setupPlayers(String[] playerNames) {
        players = new Player[playerNames.length];

        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i], STARTBALANCE);
            if(i==2) {
                players[i] = new Player(playerNames[i], 0);
            }
        }

        currentPlayer = players[0];
    }

    public void nextTurn() {
        // Chance Player Turn/Reset to first player
        playerindex = java.util.Arrays.asList(players).indexOf(currentPlayer);

        if (playerindex == players.length - 1) {
            currentPlayer = players[0];
        } else {
            currentPlayer = players[playerindex + 1];
        }
    }

    public void checkFieldType(Field field, int placement) {

        String fieldType = board.getField(placement).getClass().getSimpleName();

        switch (fieldType) {
            case "Street":
                Street street = (Street) field;
                event.fieldEffect(currentPlayer, street, players);
                break;
            case "Jail":
                Jail jail = (Jail) field;
                if (jail.getPlacement() == 30) {
                    gui.message(currentPlayer.getName() + " rykker til fængsel.");

                    // Move to Jail field
                    setPlayerPlacement(currentPlayer, 10, false);
                }
                break;
            case "FreeParking":
                // Give money to player
                currentPlayer.setPlayerBalance(FreeParking.getBalance());
                // Reset Free Parking
                FreeParking.resetBalance();
                break;
            case "Tax":
                Tax tax = (Tax) field;
                event.fieldEffect(currentPlayer, tax, netWorth(currentPlayer));
                break;
            case "Ferry":
                Ferry ferry = (Ferry) field;
                event.fieldEffect(currentPlayer, ferry, players);
                break;
            case "Brewery":
                Ownable brewery = (Ownable) field;
                event.fieldEffect(currentPlayer, brewery, players, sum);
                break;
            case "ChanceField":
                //do something
                break;
            case "Start":
                //do something
                break;
            default:
                System.out.println("Fieldtype unknown" + fieldType);
        }
    }

    public void moveplayer(Player player, int amount) {
        int prePlacement = player.getPlacement();
        int endPlacement = player.getPlacement() + amount;
        int newPlacement;

        // Check for a complete lap around on board. Then recalibrate player placement
        if (endPlacement >= 40) {
            newPlacement = endPlacement - 40;

            //Pay lap bonus
            player.setPlayerBalance(2);

        } else {
            newPlacement = endPlacement;
        }
        player.setPlacement(newPlacement);

        //Update GUI
        gui.movePlayer(player, newPlacement, prePlacement);
    }

    public void setPlayerPlacement(Player player, int endplacement, boolean passStart) {
        int preplacement = player.getPlacement();

        //Check if Starts is passed
        if (passStart && endplacement < preplacement) {
            player.setPlayerBalance(5);
        }
        player.setPlacement(endplacement);
        gui.movePlayer(player, endplacement, preplacement);
    }

    public int netWorth(Player player) {
        int netWorth = player.getPlayerBalance();
        for (int i = 0; i < board.getFields().length; i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                //Checking if field is of type Ferry, Brewery or Street.
                if (player == property.getOwner() && property instanceof Ferry) {
                    netWorth += property.getPrice();
                }
                if (player == property.getOwner() && property instanceof Brewery) {
                    netWorth += property.getPrice();
                } else if (player == property.getOwner() && property instanceof Street) {
                    netWorth += property.getPrice();
                }
            }
        }
        player.setNetWorth(netWorth);
        System.out.println(player.getName() + "'s net worth: " + player.getNetWorth());
        return netWorth;
    }

    //Copies the player array except the bankrupt player
    public void eliminatePlayer(Player player, int placement) {
        Player[] newPlayers = new Player[gui.getPlayers() - 1];
            int j = 0;
            for (int i = 0; i < gui.getPlayers(); i++) {
                if (players[i].getBankruptStatus()) {
                    gui.removePlayer(player, placement);
                    gui.getGuiPlayer(players[i]).setName(players[i].getName() + "\n[BANKEROT]");
                    gui.getGuiPlayer(players[i]).setBalance(0);
                } else {
                    newPlayers[j] = players[i];
                    j++;
                }
            }
            players = newPlayers;
    }

    public boolean bankrupt(Player player, int placement) {
        //Type casting field to Ownable
        if ((board.getField(placement) instanceof Ownable)) {
            //Verifying that the current field is of the type Ownable
            Ownable property = (Ownable) board.getField(placement);
            //if (player != property.getOwner()) {
                if (player.getPlayerBalance() < property.getCurrentRent()) {
                    //Checks if player is bankrupt
                    if (player.getNetWorth() < property.getCurrentRent()) {
                        player.setBankruptStatus(true);
                        for (int i = 0; i < board.getFields().length; i++) {
                            //Type casting field to Ownable
                            if (board.getField(i) instanceof Ownable) {
                                //Verifying that the current field is of the type Ownable
                                Ownable playerProperty = (Ownable) board.getField(i);
                                //Gives players properties to debt collector
                                if (player == playerProperty.getOwner()) {
                                    playerProperty.changeOwner(player);
                                    playerProperty.setOwner(((Ownable) board.getField(placement)).getOwner());
                                    player.setNetWorth(-((Ownable) board.getField(i)).getPrice());
                                }
                            }
                        }
                        //Removes player from the player array, Note: does not work if more players bankrupt same turn
                        eliminatePlayer(player, placement);
                        gui.message(player.getName() + " IS BANKRUPT! \nThank you for playing " + player.getName() + ".");
                        System.out.println(player.getName() + " IS BANKRUPT");
                    } else if (player.getNetWorth() > ((Ownable) board.getField(placement)).getCurrentRent()) {

                        /*
                        Player gets the option to either (drop-down menu):
                            1) Forfeit all his properties and end the game.
                            2) To mortgage all the necessary properties to survive and stay in the game.
                                - If 2) is chosen then the mortgage method will be activated and
                                  loop through the player's properties and look for houses and hotels
                                  that can be sold. *If there is no houses or hotels then run through
                                  a while loop which generates an array with properties, that gets shown
                                  through a drop-down menu that loops until the players balance is
                                  higher than the rent.
                                  */

                    }
                }
            }
        return player.getBankruptStatus();
    }

    /*
    - Make an array with the player's properties.
    - Create drop-down menu (GUI) to select different owned properties for player to sell.
    - Loop the drop-down menu option until the balance is higher than the rent.
     */
    //Method to mortgage properties when bankrupt
    public void mortage(Player player) {
        //Property[] playerProperties = new Property[4];
        int numberOfProperties;
        numberOfProperties = board.countNumbersOfPropertiesForPlayer(player);

        Street[] playerProperties = new Street[numberOfProperties];
        int currentProperty = 0;
        for (int i = 0; i < board.getFields().length; i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                if (player == property.getOwner()) {
                    playerProperties[currentProperty] = (Street) property;
                    currentProperty++;
                }
            }
        }

        //While loop that checks if balance is higher than rent

        String[] optionsBankruptOrMortage = new String[2];
        optionsBankruptOrMortage[0] = "Bankerot";
        optionsBankruptOrMortage[1] = "Pantsæt";

            gui.dropdown("Gå bankerot eller pantsæt ejendomme?", optionsBankruptOrMortage);
            switch (gui.dropdown("Gå bankerot eller pantsæt ejendomme?", optionsBankruptOrMortage)) {
                case "Bankerot":
                    player.setPlayerBalance(1);
                    eliminatePlayer(player, player.getPlacement());
                    break;
                case "Pantsæt":
                    while (player.getPlayerBalance() < ((Street) board.getField(player.getPlacement())).getCurrentRent()) {

                        /*
                        - Make options with properties in an array to choose to mortgage.
                        - OR make the program automatically sell a players properties
                         */
                        numberOfProperties = board.countNumbersOfPropertiesForPlayer(player);
                        String[] propertyNames = new String[numberOfProperties];
                        currentProperty = 0;

                        for (int i = 0; i < board.getFields().length; i++) {
                            //Type casting field to Ownable
                            if (board.getField(i) instanceof Ownable) {
                                //Verifying that the current field is of the type Ownable
                                Ownable property = (Ownable) board.getField(i);
                                if (player == property.getOwner()) {
                                    String propertyName = ((Street) property).getName();
                                    propertyNames[currentProperty] = propertyName;

                                    currentProperty++;
                                }
                            }
                        }

                        String guiSelection = gui.dropdown("Vælg en ejendom du skal sælge:", propertyNames);
                        for (int i = 0; i < board.getFields().length; i++) {
                            if (board.getField(i) instanceof Ownable) {
                                //Verifying that the current field is of the type Ownable
                                Ownable property = (Ownable) board.getField(i);
                                if (Objects.equals(guiSelection, property.getName())) {
                                    /*
                                        - Rent should be equals 0 if mortgage is true
                                        - Remove name from string array of properties
                                        - Update player balance
                                         */
                                    property.setMortage();
                                    player.setPlayerBalance((property.getPrice())/2);
                                    String[] newPropertyNames = new String[propertyNames.length - 1];
                                    int j = 0;
                                    for (int m = 0; i < propertyNames.length; m++) {
                                        if (property.getOwner() == player && !(Objects.equals(property.getName(), guiSelection))) {
                                            newPropertyNames[j] = propertyNames[m];
                                            j++;
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }



