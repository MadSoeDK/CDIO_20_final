package Controller;

import Model.*;
import Model.Board.*;
import Utility.Language;

public class GameController {

    private Board board;
    private Cup cup;
    private GUIController gui;
    private CardController cardController;
    private EventHandler event;
    private Player[] players;
    private Player currentPlayer;
    private int playerindex = 0;

    // Game Constants
    final int STARTBALANCE = 7500;

    int sum;

    public GameController() {
        board = new Board();
        cup = new Cup();
        gui = new GUIController(board.getFields());
        event = new EventHandler(gui);
        cardController = new CardController(gui);
        gui.createPlayers(STARTBALANCE);
        cardController = new CardController(gui);
        setupPlayers(gui.getPlayernames());
        playGame();
    }

    public void playGame() {
        if(!checkWinner()) {
            do {
                gui.message(Language.getText("playgame1") +" "+ currentPlayer.getName() +" "+ Language.getText("playgame2"));
                takeTurn();
                nextTurn();
            } while (!checkWinner());
            gui.message(players[0].getName() + Language.getText("playgame3"));
        }
    }

    public void takeTurn() {
        boolean playerHasRolledDice=false;
        while (!playerHasRolledDice) {

            String[] playerStartOptions = {Language.getText("taketurn2"), Language.getText("taketurn3"), Language.getText("taketurn4"), Language.getText("taketurn5")};
            String playerStartChoice = gui.getUserSelection(Language.getText("taketurn1"), playerStartOptions);

            // Run actions
            if (playerStartOptions[0] == playerStartChoice) { // Roll dice
                playerHasRolledDice=true;
            }
            if (playerStartOptions[1] == playerStartChoice) { // Trade
                // Ask if currentplayer wishes to trade?
                event.playerOptionsTrade(currentPlayer, players, board);
            }
            if (playerStartOptions[2] == playerStartChoice) { // Pay Mortgage
                //ask if currentplayer wants to buy mortgaged properties
                event.playerOptionsBuyMortgaged(currentPlayer, board);
            }
            if (playerStartOptions[3] == playerStartChoice) { // Sell houses
                //Sell houses
                if (board.getPlayerOwnsMonopoly(currentPlayer)) {
                    sellHouse();
                } else{
                    gui.message(Language.getText("taketurn6"));
                }
            }
        }

        // Check jail status
        if (!currentPlayer.getInJailStatus()) { //False - not in jail
            // Roll die, get value, show die, move player
            cup.roll();
            sum = cup.getSum();
            gui.showDice(cup.getFacevalues()[0], cup.getFacevalues()[1]);
            moveplayer(currentPlayer, sum);
        } else { // In jail
            escapeJail();
        }

        // get new placement and field
        int placement = currentPlayer.getPlacement();
        Field field = board.getField(placement);

        gui.message(currentPlayer.getName() + Language.getText("taketurn7") + field.getName());

        checkFieldType(field, placement);

        // Ask about building houses?
        build();

        //Update GUI players balance
        for (Player p : players) {
            gui.setguiPlayerBalance(p, p.getPlayerBalance());
        }

        // Updates networth and checks bankrupt status
        netWorth(currentPlayer);
        bankrupt(currentPlayer, placement);
    }

    public void setupPlayers(String[] playerNames) {
        players = new Player[playerNames.length];

        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i], STARTBALANCE);
        }
        currentPlayer = players[0];
    }

    /**
     * Change currentPlayer by incrementing players array index
     */
    public void nextTurn() {
        // Get index of player
        playerindex = java.util.Arrays.asList(players).indexOf(currentPlayer);

        if (!cup.getPair()) {
            // No pair, new player turn. If it's the last player turn, reset to first player
            if (playerindex == players.length - 1) {
                currentPlayer = players[0];
            } else {
                currentPlayer = players[playerindex + 1];
            }
        } else { // Rolled a pair, take extra turn.
            if (currentPlayer.getTurnsInRow() != 2 && currentPlayer.getInJailStatus() == false && currentPlayer.getBankruptStatus()==false) {
                gui.message(currentPlayer.getName() + Language.getText("nextturn1"));
                currentPlayer.incrementTurnsInRow();
            } else { // If 3 pairs is rolled in a row, Go to jail.
                currentPlayer.setTurnsInRow(0);
                currentPlayer.setInJailStatus(true);
                gui.message(currentPlayer.getName() + Language.getText("nextturn2"));
                setPlayerPlacement(currentPlayer, 10, false);
            }
        }
    }

    public boolean checkWinner() {
        if(players.length == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks field type and perform field effect.
     * @param field on board
     * @param placement on board
     */
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
                    gui.message(currentPlayer.getName() + Language.getText("nextturn2"));

                    // Move to Jail field
                    setPlayerPlacement(currentPlayer, 10, false);
                    currentPlayer.setInJailStatus(true);
                }
                break;
            case "FreeParking":
                // Give money to player
                //currentPlayer.setPlayerBalance(FreeParking.getBalance());
                // Reset Free Parking
                //FreeParking.resetBalance();
                break;
            case "Tax":
                Tax tax = (Tax) field;
                event.fieldEffect(currentPlayer, tax, netWorth(currentPlayer));
                break;
            case "Ferry":
                Ferry ferry = (Ferry) field;
                event.fieldEffect(currentPlayer, ferry, board, players);
                break;
            case "Brewery":
                Brewery brewery = (Brewery) field;
                event.fieldEffect(currentPlayer, brewery, players, board,  sum);
                break;
            case "ChanceField":
                cardController.doCardAction(currentPlayer, players);
                break;
            case "Start":
                //do nothing
                break;
            default:
                System.out.println("Fieldtype unknown" + fieldType);
        }
    }

    /**
     * Moves player on the board by amount
     * @param player
     * @param amount
     */
    public void moveplayer(Player player, int amount) {
        int prePlacement = player.getPlacement();
        int endPlacement = player.getPlacement() + amount;
        int newPlacement;

        // Check for a complete lap around on board. Then recalibrate player placement
        if (endPlacement >= board.getFields().length) {
            newPlacement = endPlacement - board.getFields().length;

            //Pay lap bonus
            player.setPlayerBalance(4000);

        } else {
            newPlacement = endPlacement;
        }
        player.setPlacement(newPlacement);

        //Update GUI
        gui.movePlayer(player, newPlacement, prePlacement);
    }

    /**
     * Set player placement on the board by giving an endplacement
     * @param player
     * @param endplacement - desired player placement
     * @param passStart - true if money by passing start should by paid to player
     */
    public void setPlayerPlacement(Player player, int endplacement, boolean passStart) {
        int preplacement = player.getPlacement();

        //Check if Starts is passed
        if (passStart && endplacement < preplacement) {
            player.setPlayerBalance(5);
        }
        player.setPlacement(endplacement);
        gui.movePlayer(player, endplacement, preplacement);
    }

    /**
     * Calculates networth of player. Calculation includes ownables, player balance and buildings.
     * @param player
     * @return integer amount of networth
     */
    public int netWorth(Player player) {
        int netWorth = player.getPlayerBalance();
        for (int i = 0; i < board.getFields().length; i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                //Checking if field is of type Ferry, Brewery or Street.
                if (player == property.getOwner() && property instanceof Ferry) {
                    netWorth += property.getPrice()/2;
                } else if (player == property.getOwner() && property instanceof Brewery) {
                    netWorth += property.getPrice()/2;
                } else if (player == property.getOwner() && property instanceof Street) {
                    netWorth += property.getPrice()/2;
                    if(((Street) property).getHouseAmount() > 0) {
                        netWorth += property.getPrice() + (((Street) property).getHouseAmount() * ((Street) property).getHousePrice());
                    }
                }
            }
        }
        player.setNetWorth(netWorth);
        return netWorth;
    }

    /**
     * Eliminates a selected player from the game by removing the player from the player array
     * @param player
     * @param placement - to remove from the GUI also
     */
    public void eliminatePlayer(Player player, int placement) {
        // Makes a new player array one size less and copies the old array one by one except for the bankrupt player
        Player[] newPlayers = new Player[players.length - 1];
        int j = 0;
        for (int i = 0; i < players.length; i++) {
            if(players[i].getBankruptStatus()) {
                gui.removePlayer(player, placement);
                gui.getGuiPlayer(players[i]).setName(players[i].getName() + Language.getText("eliminateplayer1"));
                gui.getGuiPlayer(players[i]).setBalance(0);
            } else {
                newPlayers[j] = players[i];
                j++;
            }
        }
        players = newPlayers;

    }

    /**
     * Sets bankrupt status if the player can't pay rent
     * @param player
     * @param placement
     * @return
     */
    public boolean bankrupt(Player player, int placement) {

        // If player landed on an ownable, proceed to more checks
        if ((board.getField(placement) instanceof Ownable)) {

            // Cast to ownable
            Ownable property = (Ownable) board.getField(placement);

            // Check if the propert is a street, so that houses are calculated correctly
            if (property instanceof Street) {
                property = (Street) board.getField(placement);
            }

            // If player can't pay the rent by current balance, proceed to more checks
            if (player.getPlayerBalance() < property.getCurrentRent()) {

                // If netWorth is lower than the rent, then player is bankrupt. Else pay debt by mortgage
                if (player.getNetWorth() < property.getCurrentRent()) {
                    player.setBankruptStatus(true);

                    // transfer ownership of the bankrupt player to creditor
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
                    gui.message(player.getName() + Language.getText("bankrupt1") + player.getName() + ".");
                    eliminatePlayer(player, placement);

                } else { // When you are able to mortgage properties to survive
                    //While loop that checks if balance is higher than rent
                    boolean answer = gui.getUserBool(Language.getText("bankrupt2"), "Bankerot", "Pantsæt");

                    // If player gives up - set bankrupt status true
                    if (answer) {
                        player.setBankruptStatus(true);
                        eliminatePlayer(player, placement);

                        //Change ownership
                    } else { //Pay by mortgage
                        mortgage(player);
                    }
                }
            }
        }
        return player.getBankruptStatus();
    }

    /**
     * Build house when player hos monopoly
     */
    public void build(){
        if (board.getPlayerOwnsMonopoly(currentPlayer)) {

            boolean stopBuilding = false;
            while (!stopBuilding) {
                if (gui.getUserBool(Language.getText("build1"), "Jeg vil bygge", "Jeg vil IKKE bygge")) {

                    // Get player monopolies and store as Strings
                    Monopoly[] playerMonopolyOptions = board.getOwnedPlayerMonopolyList(currentPlayer);
                    String[] PlayerMonopolyOptionsString = new String[playerMonopolyOptions.length];

                    for (int i = 0; i < playerMonopolyOptions.length; i++) {
                        PlayerMonopolyOptionsString[i] = playerMonopolyOptions[i].getName();
                    }

                    String selectedMonopolyName = gui.dropdown(Language.getText("build2"), PlayerMonopolyOptionsString);
                    Monopoly selectedMonopoly = null;
                    // Match the string array to the object reference array
                    for (int i = 0; i < playerMonopolyOptions.length; i++) {
                        if (playerMonopolyOptions[i].getName() == selectedMonopolyName) {
                            selectedMonopoly = playerMonopolyOptions[i];
                        }
                    }

                    // Chose specific street in monopoly
                    int lowestHouseAmount=5;
                    Street selectedStreet = null;
                    String[] selectedMonopolyStreetStringArray = selectedMonopoly.getStringArray();
                    String selectedStreetString = gui.dropdown(Language.getText("build3")+selectedMonopoly.getStreetArray()[0].getHousePrice(), selectedMonopolyStreetStringArray);
                    for (int i = 0; i < selectedMonopoly.getStreetArray().length; i++) {
                        // Check highest house amount
                        if (selectedMonopoly.getStreetArray()[i].getHouseAmount() < lowestHouseAmount){lowestHouseAmount = selectedMonopoly.getStreetArray()[i].getHouseAmount();}
                        // Match player selection with string array number
                        if (selectedStreetString == selectedMonopolyStreetStringArray[i]) {
                            selectedStreet = selectedMonopoly.getStreetArray()[i];
                        }
                    }

                    // Check for uneven house amounts
                    if (selectedStreet.getHouseAmount() == lowestHouseAmount) {
                        // Add house to selected Street, pay for it & add GUI element
                        selectedStreet.incrementHouseAmount();
                        // Build house/hotel
                        gui.setGuiHouseAmount(selectedStreet.getPlacement(), selectedStreet.getHouseAmount());
                        gui.updateFieldRent(selectedStreet.getPlacement(), selectedStreet.getCurrentRent());
                        currentPlayer.setPlayerBalance(-selectedStreet.getHousePrice());
                    } else {
                        gui.message(Language.getText("build4"));
                    }
                } else {
                    stopBuilding = true;
                }
            }
        }
    }
    public void sellHouse(){
        boolean stopBuilding = false;
        while (!stopBuilding) {
            if (gui.getUserBool(Language.getText("sell1"), "Jeg vil sælge huse", "Jeg vil IKKE sælge huse")) {

                // Sell Houses
                // Give player option over monopolies
                Monopoly[] playerMonopolyOptions = board.getOwnedPlayerMonopolyList(currentPlayer);
                String[] PlayerMonopolyOptionsString = new String[playerMonopolyOptions.length];
                for (int i = 0; i < playerMonopolyOptions.length; i++) {
                    PlayerMonopolyOptionsString[i] = playerMonopolyOptions[i].getName();
                }
                String selectedMonopolyName = gui.dropdown(Language.getText("sell2"), PlayerMonopolyOptionsString);
                Monopoly selectedMonopoly = null;// = new Monopoly();

                // Match the string array to the object referance array
                for (int i = 0; i < playerMonopolyOptions.length; i++) {
                    if (playerMonopolyOptions[i].getName() == selectedMonopolyName) {
                        selectedMonopoly = playerMonopolyOptions[i];
                    }
                }

                // Chose specific street to sell from
                int highestHouseAmount=0;
                Street selectedStreet = null;
                String[] selectedMonopolyStreetStringArray = selectedMonopoly.getStringArray();
                String selectedStreetString = gui.dropdown(Language.getText("sell3")+selectedMonopoly.getStreetArray()[0].getHousePrice()/2, selectedMonopolyStreetStringArray);
                for (int i = 0; i < selectedMonopoly.getStreetArray().length; i++) {
                    // Check highest house amount
                    if (selectedMonopoly.getStreetArray()[i].getHouseAmount() > highestHouseAmount){highestHouseAmount = selectedMonopoly.getStreetArray()[i].getHouseAmount();}
                    // Match player selection with string array number
                    if (selectedStreetString == selectedMonopolyStreetStringArray[i]) {
                        selectedStreet = selectedMonopoly.getStreetArray()[i];
                    }
                }
                // Make sure Street has house
                if (selectedStreet.getHouseAmount() > 0) {
                    // Check for uneven house amounts
                    if (selectedStreet.getHouseAmount() == highestHouseAmount) {
                        // Add house to selected Street, pay for it & add GUI element
                        selectedStreet.setHouseAmount(selectedStreet.getHouseAmount() - 1);
                        // Build house/hotel
                        gui.setGuiHouseAmount(selectedStreet.getPlacement(), selectedStreet.getHouseAmount());
                        gui.updateFieldRent(selectedStreet.getPlacement(), selectedStreet.getCurrentRent());
                        currentPlayer.setPlayerBalance(selectedStreet.getHousePrice() / 2);
                    } else {
                        gui.message(Language.getText("sell4"));
                    }
                } else {
                    gui.message(Language.getText("sell5"));
                }
            } else { // Don't sell Houses
                stopBuilding = true;
            }
        }
    }

    /**
     * Method to mortgage properties when bankrupt
     * @param player
     */
    public void mortgage(Player player) {
        //checks number of properties the player own
        int numberOfProperties;
        int numberOfPropertiesWithHouses;
        numberOfProperties = board.PropertiesForPlayer(player);
        numberOfPropertiesWithHouses = board.PropertiesWithHouseForPlayer(player);

        Ownable[] playerProperties = new Ownable[numberOfProperties];
        String[] propertyNames = new String[numberOfProperties];
        int currentProperty = 0;
        //fills property arrays
        for (int i = 0; i < board.getFields().length; i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                if (player == property.getOwner() && !property.getMortgage()) {
                    playerProperties[currentProperty] = property;
                    propertyNames[currentProperty] = property.getName();
                    currentProperty++;
                }
            }
        }
        int j = 0;
        // Mortgage until you have enough money to pay rent
        while (player.getPlayerBalance() < ((Ownable) board.getField(player.getPlacement())).getCurrentRent()) {
            //checks if the player own properties with houses
            if (numberOfPropertiesWithHouses == 0) {
                String guiSelection = gui.dropdown("mortgage1", propertyNames);
                //creates new arrays without mortgaged properties
                String[] newPropertyNames = new String[propertyNames.length - 1];
                Ownable[] newPlayerProperties = new Ownable[playerProperties.length - 1];
                for (int i = 0; i < propertyNames.length; i++) {
                    //Verifying that the current field is of the type Ownable
                    Ownable property = playerProperties[i];
                    if (property.getName().equals(guiSelection)) {
                        property.setMortgage(true);
                        //update gui field text
                        gui.setSubText("mortgage2",property);
                        player.setPlayerBalance((property.getPrice()) / 2);
                        //changes array if length is 0 to escape null pointer exception
                        if (newPropertyNames.length == 0) {
                            String msg = "mortgage3";
                            newPropertyNames = new String[1];
                            newPropertyNames[0] = msg;
                        }

                    } else {
                        newPropertyNames[j] = propertyNames[i];
                        newPlayerProperties[j] = playerProperties[i];
                        j++;
                    }
                }
                propertyNames = newPropertyNames;
                playerProperties = newPlayerProperties;
            } else {
                //makes player sell houses before mortgage properties
                sellHouse();
            }
            j=0;
            gui.setguiPlayerBalance(player, player.getPlayerBalance());
        }
    }

    public void escapeJail() {
        // Make string array based on jailcard
        String[] escapeOption = {"escapejail1", "escapejail2"};
        String[] allEscapeOption = {"escapejail1", "escapejail2", "escapejail3"};
        String[] oneEscapeOption = {"escapejail2"};

        // If you have jailcard
        if (currentPlayer.gethasJailFreecard()) {
            escapeOption = allEscapeOption;
        }

        if (currentPlayer.getEscapeAttempts() == 3) {
            escapeOption = oneEscapeOption;
        }

        switch(gui.dropdown("escapejail4", escapeOption)){

            case "escapejail1":
                cup.roll();
                sum = cup.getSum();
                gui.showDice(cup.getFacevalues()[0],cup.getFacevalues()[1]);
                // If pair
                if (cup.getPair()) {
                    currentPlayer.setInJailStatus(false);
                    moveplayer(currentPlayer,sum);
                    currentPlayer.setEscapeAttempts(0);
                } else {
                    currentPlayer.incrementEscapeAttempts();
                }
                break;

            case "escapejail2":
                currentPlayer.setPlayerBalance(-1000);
                hasEscapedJail();
                break;

            case "escapejail3":
                hasEscapedJail();
                break;

        }
    }

    private void hasEscapedJail(){
        cup.roll();
        sum = 1;//cup.getSum();
        currentPlayer.setInJailStatus(false);
        currentPlayer.setEscapeAttempts(0);
        moveplayer(currentPlayer,sum);
        gui.showDice(cup.getFacevalues()[0],cup.getFacevalues()[1]);
    }
}



