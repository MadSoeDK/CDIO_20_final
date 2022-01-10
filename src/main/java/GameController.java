import Model.*;
import gui_fields.GUI_Ownable;

import java.util.Objects;

public class GameController {

    private Board board;
    private Cup cup;
    private GUIController gui;
    private EventHandler event;
    private Player[] players;
    private Player currentPlayer;
    private int playerindex=0;

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
        event.playerOptions(currentPlayer, players);

        gui.button(" ", "Rul terning");

        // Roll die, get value, show die
        cup.roll();
        sum = cup.getSum();
        gui.showDice(cup.getFacevalues()[0], cup.getFacevalues()[1]);

        // Move player placement - automatically updates GUI
        moveplayer(currentPlayer, sum);

        int placement = currentPlayer.getPlacement();

        Field field = board.getField(placement);

        gui.message(currentPlayer.getName() + " landede på " + field.getName());

        checkFieldType(field, placement);

        //event.auction(2000, players);

        //Update GUI players balance
        for (Player p : players) {
            gui.setguiPlayerBalance(p, p.getPlayerBalance());
        }

        //checkBankrupt();
        //netWorth(currentPlayer);
        //bankrupt(currentPlayer, placement);

    }
    public void setupPlayers(String[] playerNames) {
        players = new Player[playerNames.length];

        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i],STARTBALANCE);
        }

        currentPlayer = players[0];
    }
    public void nextTurn() {
        // Chance Player Turn/Reset to first player
        playerindex = java.util.Arrays.asList(players).indexOf(currentPlayer);

        if (playerindex == players.length-1) {
            currentPlayer = players[0];
        } else {
            currentPlayer = players[playerindex + 1];
        }
    }
    public void checkFieldType(Field field, int placement) {

        String fieldType = board.getField(placement).getClass().getSimpleName();

        switch (fieldType) {
            case "Street":
                Ownable street = (Ownable) field;
                event.fieldEffect(currentPlayer, street, players);
                break;
            case "Jail":
                Jail jail = (Jail) field;
                event.fieldEffect(currentPlayer, jail);
                // Move to Jail field
                setPlayerPlacement(currentPlayer, 10, false);
                break;
            case "FreeParking":
                // Give money to player
                currentPlayer.setPlayerBalance(FreeParking.getBalance());
                // Reset Free Parking
                FreeParking.resetBalance();
                break;
            case "Tax":
                Tax tax = (Tax) field;
                event.fieldEffect(currentPlayer, tax);
                break;
            case "Ferry":
                Ferry ferry = (Ferry) field;
                event.fieldEffect(currentPlayer, ferry);
                break;
            case "Brewery":
                Brewery brewery = (Brewery) field;
                event.fieldEffect(currentPlayer, brewery, sum);
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
        if(endPlacement >= 40) {
            newPlacement = endPlacement - 40;

            //Pay lap bonus
            player.setPlayerBalance(2);

        } else {
            newPlacement = endPlacement;
        }
        player.setPlacement(newPlacement);

        //Update GUI
        gui.movePlayer(player,newPlacement, prePlacement);
    }
    public void setPlayerPlacement(Player player, int endplacement, boolean passStart) {
        int preplacement = player.getPlacement();

        //Check if Starts is passed
        if (passStart && endplacement < preplacement) {
            player.setPlayerBalance(5);
        }
        player.setPlacement(endplacement);
        gui.movePlayer(player,endplacement,preplacement);
    }

    /*private int getFerryRent(Ferry property){

        // Check amounts of other ferries owned
        int same_ferry_owner=0;
        int ferry_cost=500;

        for (var i=5; i<35; i+=10) {
            // Typecast
            Ferry property_check = (Ferry) board.getField(i);

            // Check if owner is the same
            if (property_check.getOwner() == property.getOwner()) {
                same_ferry_owner++;
                if (same_ferry_owner>1)
                {
                    ferry_cost=ferry_cost*2;
                }
            }
        }

        return ferry_cost;
    }

    private void updateFerryGUI(Ferry property, int ferry_cost){
        // Update Cost
        for (var i=5; i<35; i+=10) {
            // Typecast
            Ferry property_check = (Ferry) board.getField(i);

            // Check if owner is the same
            if (property_check.getOwner() == property.getOwner()) {
                property_check.getGUIField().setSubText("$"+(ferry_cost));
            }
        }
    }


    /*public int netWorth(Player player) {
        int netWorth = player.getPlayerBalance();

        for (int i = 0; i < board.getFieldsTotal(); i++) {
    public int netWorth(Player player) {
        for (int i = 0; i < board.getFields().length; i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                if (player == property.getOwner() && board.getField(i) instanceof Street) {
                    System.out.println(player.getName() + " landte på "
                            + board.getField(i).getName() + ". Der tilføjes: " + ((Street) board.getField(i)).getPrice());
                    player.setNetWorth(((Ownable) board.getField(i)).getPrice());
                }
            }
        }
        System.out.println(player.getName() + "'s net worth: " + player.getNetWorth());
        return player.getNetWorth();
    }
    //Copies the player array except the bankrupt player
    public Player[] eliminatePlayer() {
        Player[] newPlayers = new Player[gui.getPlayers() - 1];
        int j = 0;
        for (int i = 0; i < gui.getPlayers(); i++) {
            Player currentPlayer = players[i];
            if (currentPlayer.getBankruptStatus() == false) {
                newPlayers[j] = players[i];
                j++;
            }
        }
        return newPlayers;
    }

    /*public boolean bankrupt(Player player, int placement) {
        //Type casting field to Ownable
        boolean bankrupt = false;
        if ((board.getField(placement) instanceof Ownable) && !(board.getField(placement) instanceof Ferry) && !(board.getField(placement) instanceof Tax)) {
            //Verifying that the current field is of the type Ownable
            Ownable property = (Ownable) board.getField(placement);
            if (player != property.getOwner()) {
                if (player.getPlayerBalance() < ((Street) board.getField(placement)).getCurrentRent()) {
                    //Checks if player is bankrupt
                    if (netWorth(player) < ((Street) board.getField(placement)).getCurrentRent()) {
                        player.setBankruptStatus(true);
                        for (int i = 0; i < board.getFields().length; i++) {
                            //Type casting field to Ownable
                            if (board.getField(i) instanceof Ownable) {
                                //Verifying that the current field is of the type Ownable
                                Ownable playerProperty = (Ownable) board.getField(i);
                                //Gives players properties to debt collector
                                if (player == playerProperty.getOwner()) {
                                    playerProperty.setOwner(((Street) board.getField(placement)).getOwner());
                                    player.setNetWorth(-((Ownable) board.getField(i)).getPrice());
                                }
                            }
                        }
                        //Removes player from the player array, Note: does not work if more players bankrupt same turn
                        eliminatePlayer();
                    } else if (netWorth(player) > ((Street) board.getField(placement)).getCurrentRent()) {

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


                    }
                }
            }
        }
        return bankrupt;
    }

/*
    - Make an array with the player's properties.
    - Create drop-down menu (GUI) to select different owned properties for player to sell.
    - Loop the drop-down menu option until the balance is higher than the rent.
     */
    //Method to mortgage properties when bankrupt
   /* public void mortage(Player player) {
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
            switch(gui.dropdown("Gå bankerot eller pantsæt ejendomme?", optionsBankruptOrMortage)) {
                case "Bankerot":
                    player.setPlayerBalance(1);
                    eliminatePlayer();
                    break;
                case "Pantsæt":
                    while(player.getPlayerBalance() < ((Street) board.getField(player.getPlacement())).getCurrentRent()) {


                    - Make options with properties in an array to choose to mortgage.
                    - OR make the program automatically sell a players properties

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
                                if (board.getField(i) instanceof Street) {


                                }
                                else if (board.getField(i) instanceof Ferry) {

                                }
                                else if (board.getField(i) instanceof Brewery) {

                                }
                            }
                        }

                    }
                    break;
            }
        }
    }

    */
}