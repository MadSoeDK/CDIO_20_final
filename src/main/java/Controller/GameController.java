package Controller;

import Model.*;
import Model.Board.*;

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
        } while (players.length > 1); //Check winner
        gui.message(currentPlayer.getName() + " HAR VUNDET SPILLET!");
    }

    public void takeTurn() {
        boolean playerHasRolledDice=false;
        while (!playerHasRolledDice) {
            String[] playerStartOptions = {"Slå med terningerne", "Handle med Spiller", "Betal Pantsætning", "Sælg Hus"};
            String playerStartChoice = gui.getUserSelection("Hvilken handling vil du tage?", playerStartOptions);
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
                    gui.message("Du ejer ikke nogle huse");
                }
            }
        }

        // Check jail status
        if (currentPlayer.getInJailStatus() == false)
        {
            // Roll die, get value, show die
            cup.roll();
            sum = 5;//cup.getSum();
            gui.showDice(cup.getFacevalues()[0], cup.getFacevalues()[1]);
            moveplayer(currentPlayer, sum);
        }
        else {
            escapeJail();
        }



        // Roll die, get value, show die
        /*
        cup.roll();
        sum = 30; //cup.getSum();
        gui.showDice(cup.getFacevalues()[0], cup.getFacevalues()[1]);

         */

        // Move player placement - automatically updates GUI
        int placement = currentPlayer.getPlacement();

        Field field = board.getField(placement);

        gui.message(currentPlayer.getName() + " landede på " + field.getName());

        checkFieldType(field, placement);

        // Ask about building houses?
        build();

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
        }
        currentPlayer = players[0];
    }

    public void nextTurn() {
        // Chance Player Turn/Reset to first player
        playerindex = java.util.Arrays.asList(players).indexOf(currentPlayer);

        if (cup.getFacevalues()[0] != cup.getFacevalues()[1])
        {
            // If there a pair wasn't rolled
            if (playerindex == players.length - 1) {
                currentPlayer = players[0];
            } else {
                currentPlayer = players[playerindex + 1];
            }
        }
        else
        {
            if (currentPlayer.getTurnsInRow() != 2)
            {
                gui.message(currentPlayer.getName() + " tager en ekstra tur fordi der blev slået par!");
                currentPlayer.incrementTurnsInRow();
            }
            else
            {
                // Go to jail
                currentPlayer.setTurnsInRow(0);
                currentPlayer.setInJailStatus(true);
                gui.message(currentPlayer.getName() + " rykker til fængsel.");
                setPlayerPlacement(currentPlayer, 10, false);
            }
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
                event.fieldEffect(currentPlayer, ferry, board, players);
                break;
            case "Brewery":
                Brewery brewery = (Brewery) field;
                event.fieldEffect(currentPlayer, brewery, players, board,  sum);
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
                } else if (player == property.getOwner() && property instanceof Brewery) {
                    netWorth += property.getPrice();
                } else if (player == property.getOwner() && property instanceof Street) {
                    netWorth += property.getPrice();
                    if(((Street) property).getHouseAmount() > 0) {
                        for (int j = 0; j < ((Street) property).getHouseAmount(); j++) {
                            netWorth += ((Street) property).getHousePrice();
                            if(((Street) property).getHouseAmount() > 4) {
                                //netWorth += property.getHotelPrice();
                            }
                        }
                    }
                }
            }
        }
        System.out.println(player.getName() + "'s net worth er " + netWorth);
        player.setNetWorth(netWorth);
        return netWorth;
    }

    //Copies the player array except the bankrupt player
    public void eliminatePlayer(Player player, int placement) {
        Player[] newPlayers = new Player[gui.getPlayers() - 1];
        int j = 0;
        for (int i = 0; i < gui.getPlayers(); i++) {
            if(players[i].getBankruptStatus()) {
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
            // Check if the propert is a street, so that houses are calculated
            if (property instanceof Street){property = (Street) board.getField(placement);}
            if (player.getPlayerBalance() < property.getCurrentRent()) {
                //If netWorth is lower than the rent, then you are bankrupt
                if (player.getNetWorth() < property.getCurrentRent()) {
                    player.setBankruptStatus(true);
                    // Change ownership to creditor
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
                    //System.out.println(player.getName() + " IS BANKRUPT");
                } else { // When you are able to mortgage to survive
                    //While loop that checks if balance is higher than rent
                    boolean answer = gui.getUserBool("Gå bankerot eller pantsæt ejendomme?", "Bankerot", "Pantsæt");

                    if (answer) {
                        player.setBankruptStatus(true);
                        eliminatePlayer(player, placement);
                        //Change ownership
                    } else {
                        mortgage(player);
                    }
                }
            }
        }
        return player.getBankruptStatus();
    }

    public void build(){
        if (board.getPlayerOwnsMonopoly(currentPlayer))
        {
            boolean stopBuilding = false;
            while (!stopBuilding) {
                if (gui.getUserBool("Vil du bygge på din grund", "Jeg vil bygge", "Jeg vil IKKE bygge")) {

                    // Give player option over monopolies
                    Monopoly[] playerMonopolyOptions = board.getOwnedPlayerMonopolyList(currentPlayer);
                    String[] PlayerMonopolyOptionsString = new String[playerMonopolyOptions.length];
                    for (int i = 0; i < playerMonopolyOptions.length; i++) {
                        PlayerMonopolyOptionsString[i] = playerMonopolyOptions[i].getName();
                    }
                    String selectedMonopolyName = gui.dropdown("Hvilken farve vil du bygge på?", PlayerMonopolyOptionsString);
                    Monopoly selectedMonopoly = null;// = new Monopoly();

                    // Match the string array to the object referance array
                    for (int i = 0; i < playerMonopolyOptions.length; i++) {
                        if (playerMonopolyOptions[i].getName() == selectedMonopolyName) {
                            selectedMonopoly = playerMonopolyOptions[i];
                        }
                    }

                    // Chose specific street
                    int lowestHouseAmount=5;
                    Street selectedStreet = null;
                    String[] selectedMonopolyStreetStringArray = selectedMonopoly.getStringArray();
                    String selectedStreetString = gui.dropdown("Hvilken Bygning vil du bygge på? Et hus koster: "+selectedMonopoly.getStreetArray()[0].getHousePrice(), selectedMonopolyStreetStringArray);
                    for (int i = 0; i < selectedMonopoly.getStreetArray().length; i++) {
                        // Check highest house amount
                        if (selectedMonopoly.getStreetArray()[i].getHouseAmount() < lowestHouseAmount){lowestHouseAmount = selectedMonopoly.getStreetArray()[i].getHouseAmount();}
                        // Match player selection with string array number
                        if (selectedStreetString == selectedMonopolyStreetStringArray[i]) {
                            selectedStreet = selectedMonopoly.getStreetArray()[i];
                        }
                    }
                    // Check for uneven house amounts
                    if (selectedStreet.getHouseAmount() == lowestHouseAmount)
                    {
                        // Add house to selected Street, pay for it & add GUI element
                        selectedStreet.incrementHouseAmount();
                        // Build house/hotel
                        gui.setGuiHouseAmount(selectedStreet.getPlacement(), selectedStreet.getHouseAmount());
                        gui.updateFieldRent(selectedStreet.getPlacement(), selectedStreet.getCurrentRent());
                        currentPlayer.setPlayerBalance(-selectedStreet.getHousePrice());
                    }
                    else{
                        gui.message("Du skal bygge jævnt, forskelle i husmængder på farven må maksimalt være 1");
                    }
                }
                else{
                    stopBuilding = true;
                }
            }
        }
    }
    public void sellHouse(){

        boolean stopBuilding = false;
        while (!stopBuilding)
        {
            if (gui.getUserBool("Vil sælge et hus?", "Jeg vil sælge huse", "Jeg vil IKKE sælge huse")) {

                // Sell Houses
                    // Give player option over monopolies
                    Monopoly[] playerMonopolyOptions = board.getOwnedPlayerMonopolyList(currentPlayer);
                    String[] PlayerMonopolyOptionsString = new String[playerMonopolyOptions.length];
                    for (int i = 0; i < playerMonopolyOptions.length; i++) {
                        PlayerMonopolyOptionsString[i] = playerMonopolyOptions[i].getName();
                    }
                    String selectedMonopolyName = gui.dropdown("Hvilken farve vil du sælge Fra?", PlayerMonopolyOptionsString);
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
                    String selectedStreetString = gui.dropdown("Hvilken Bygning vil sælge for: "+selectedMonopoly.getStreetArray()[0].getHousePrice()/2, selectedMonopolyStreetStringArray);
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
                        gui.message("Du skal sælge jævnt, forskelle i husmængder på farven må maksimalt være 1");
                    }
                } else {
                    gui.message("Her er ikke nogen huse at sælge");
                }
            }
            else
            {
                // Don't sell Houses
                stopBuilding = true;
            }
        }
    }



    /*
    - Make an array with the player's properties.
    - Create drop-down menu (GUI) to select different owned properties for player to sell.
    - Loop the drop-down menu option until the balance is higher than the rent.
     */
    //Method to mortgage properties when bankrupt
    public void mortgage(Player player) {
        //Property[] playerProperties = new Property[4];
        int numberOfProperties;
        numberOfProperties = board.countNumbersOfPropertiesForPlayer(player);

        Ownable[] playerProperties = new Ownable[numberOfProperties];
        String[] propertyNames = new String[numberOfProperties];
        int currentProperty = 0;
        for (int i = 0; i < board.getFields().length; i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                if (player == property.getOwner()) {
                    playerProperties[currentProperty] = property;
                    propertyNames[currentProperty] = property.getName();
                    currentProperty++;
                }

            }
        }
        // Mortgage until you have enough money to pay rent
        while (player.getPlayerBalance() < ((Ownable) board.getField(player.getPlacement())).getCurrentRent()) {

            String guiSelection = gui.dropdown("Vælg en ejendom du skal sælge:", propertyNames);

            for (int i = 0; i < propertyNames.length; i++) {
                //Verifying that the current field is of the type Ownable
                Ownable property = playerProperties[i];
                if (property.getName().equals(guiSelection)) {
                    property.setMortgage(true);
                    //set GUI mortgage
                    player.setPlayerBalance((property.getPrice()) / 2);

                    if (numberOfProperties == 1) {
                        String msg = "Ikke flere ejendomme";
                        propertyNames[0] = msg;
                    } else {
                        String[] newPropertyNames = new String[propertyNames.length - 1];
                        //String[] propertyNames = new String[newPropertyNames.length];
                        int j = 0;
                        if (property.getOwner() == player && (property.getName().equals(guiSelection))) {
                            propertyNames[i] = newPropertyNames[j];
                            j++;
                        }
                    }
                }
            }
        }
    }

    public void escapeJail(){
        // Make string array based on jailcard
        String[] escapeOption = {"Slå et par", "Betal 1000"};
        String[] allEscapeOption = {"Slå et par", "Betal 1000", "Brug Chance Kort"};
        String[] oneEscapeOption = {"Betal 1000"};
        // If you have jailcard
        if (false)
        {
            escapeOption = allEscapeOption;
        }
        if (currentPlayer.getEscapeAttempts() == 3)
        {
            escapeOption = oneEscapeOption;
        }
        switch(gui.dropdown("Hvordan vil du undslippe fængsel",escapeOption)){

            case "Slå et par":
                cup.roll();
                sum = cup.getSum();
                gui.showDice(cup.getFacevalues()[0],cup.getFacevalues()[1]);
                // If pair
                if (cup.getFacevalues()[0] == cup.getFacevalues()[1])
                {
                    currentPlayer.setInJailStatus(false);
                    moveplayer(currentPlayer,sum);
                    currentPlayer.setEscapeAttempts(0);
                }
                else
                {
                    currentPlayer.incrementEscapeAttempts();
                }
                break;

            case "Betal 1000":
                currentPlayer.setPlayerBalance(-1000);
                hasEscapedJail();
                break;

            case "Brug Chance Kort":
                hasEscapedJail();
                break;

        }
    }

    private void hasEscapedJail(){
        cup.roll();
        sum = cup.getSum();
        currentPlayer.setInJailStatus(false);
        currentPlayer.setEscapeAttempts(0);
        moveplayer(currentPlayer,sum);
        gui.showDice(cup.getFacevalues()[0],cup.getFacevalues()[1]);
    }
}



