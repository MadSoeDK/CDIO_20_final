import Model.*;
import gui_fields.GUI_Ownable;
import gui_main.GUI;

public class GameController {

    private Board board;
    private Cup cup;
    private GUIController gui;
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
        playerOptions(currentPlayer);

        gui.button(" ", "Rul terning");

        // Roll die, get value, show die
        cup.roll();
        sum = 12;//cup.getSum();
        gui.showDice(cup.getFacevalues()[0], cup.getFacevalues()[1]);

        // Move player placement - automatically updates GUI
        moveplayer(currentPlayer, sum);

        int placement = currentPlayer.getPlacement();

        Field field = board.getField(placement);

        gui.message(currentPlayer.getName() + " landede på " + field.getName());

        checkFieldType(field, placement);

        //Update GUI players balance
        for (Player p : players) {
            gui.setguiPlayerBalance(p, p.getPlayerBalance());
        }

        //checkBankrupt();
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

                // Typecast to Property
                Street street = (Street) field;

                // Check if Field is owned by another player and it's not the current player
                if (street.getOwner() != currentPlayer && street.getOwner() != null) { //There is a Field owner and it's not owned by the current Player

                    // Get field owner
                    Player fieldOwner = street.getOwner();

                    // Check if Owner owns all colors
                    if (false /*board.hasMonopoly(placement)*/) {
                        // 1. Subtract rent from current player 2. add to field owner
                        currentPlayer.setPlayerBalance(-street.getCurrentRent() * 2);
                        fieldOwner.setPlayerBalance(street.getCurrentRent() * 2);
                    }

                } else { // No one owns the Property, buy it.

                    // Subtract player balance from Property rent
                    currentPlayer.setPlayerBalance(-street.getCurrentRent());

                    // Set Property owner
                    street.setOwner(currentPlayer);

                    // Set GUI Field
                    gui.setOwner(currentPlayer, field);
                }
                break;

            case "Jail":

                // Typecast to Jail
                Jail jail = (Jail) field;

                // Add money to Free Parking if landed on "Go To Jail"
                if (placement == 30) {
                    gui.message(currentPlayer.getName() + " rykker til fængsel og betaler $3");

                    // Subtract player balance from Property rent
                    //currentPlayer.setPlayerBalance(-jail.getRent());

                    FreeParking.setBalance(3);

                    // Move to Jail field
                    //currentPlayer.setPlacement(6);
                    setPlayerPlacement(currentPlayer, 10, false);

                }
                // Set GUI Balance
                //gui.getGuiField(placement).setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));
                break;

            case "FreeParking":
                // Give money to player
                currentPlayer.setPlayerBalance(FreeParking.getBalance());

                // Reset Free Parking
                FreeParking.resetBalance();

                // Set GUI Balance
                //gui.getGuiField(placement).setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));
                break;

            case "Tax":
                //do something

                break;

            case "Ferry":

                // Typecast to Model.Property
                Ferry ferry = (Ferry) field;

                // Check Ferry Rent
                int ferry_cost = ferry.getCurrentRent(); //getFerryRent()

                if (ferry.getOwner() == null) { // Noone owns Ferry
                    // Subtract player balance from Model.Property rent
                    currentPlayer.setPlayerBalance(-4000);

                    // Set Model.Property owner
                    ferry.setOwner(currentPlayer);

                    // Set GUI Field
                    gui.setOwner(currentPlayer, ferry);

                    //ferry_cost = getFerryRent(property);
                    //updateFerryGUI(property, ferry_cost);
                } else {
                    // Get field owner
                    Player fieldOwner = ferry.getOwner();

                    // 1. Subtract rent from current player 2. add to field owner
                    currentPlayer.setPlayerBalance(-ferry_cost);
                    fieldOwner.setPlayerBalance(ferry_cost);

                    //Update GUI-object and display the current player balance
                    //board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                    //fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());
                }

                /*if (field instanceof Ferry) {

                    // Typecast to Model.Property
                    Ferry property = (Ferry) field;

                    // Check Ferry Rent
                    int ferry_cost = getFerryRent(property);

                    // Noone owns Ferry
                    if (property.getOwner() == null)
                    {
                        // Subtract player balance from Model.Property rent
                        player.setPlayerBalance(-4000);

                        //Get the GUI-object and display the current player balance
                        player.getPlayer().setBalance(player.getPlayerBalance());

                        // Set Model.Property owner
                        property.setOwner(player);

                        // Set GUI Model.Field
                        GUI_Ownable ownable = (GUI_Ownable) board.getField(placement).getGUIField();
                        ownable.setOwnerName(player.getName());
                        ownable.setBorder(player.getPlayerColor());

                        ferry_cost = getFerryRent(property);
                        updateFerryGUI(property, ferry_cost);
                    }
                    else // Other player Owns Ferry
                    {
                        // Get field owner
                        Player fieldOwner = property.getOwner();

                        // 1. Subtract rent from current player 2. add to field owner
                        player.setPlayerBalance(-ferry_cost);
                        fieldOwner.setPlayerBalance(ferry_cost);

                        //Update GUI-object and display the current player balance
                        board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                        fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());
                    }
                }*/
                break;

            case "Brewery":

                // Typecast to Model.Property
                Brewery brewery = (Brewery) field;

                // Typecast other company
                Ownable otherBrewery;

                // No one owns Company
                if (brewery.getOwner() == null) {

                    // Subtract player balance from Model.Property rent
                    currentPlayer.setPlayerBalance(-brewery.getPrice());

                    // Set Brewery owner and display on GUI
                    brewery.setOwner(currentPlayer);
                    gui.setOwner(currentPlayer, field);

                }  else { // Other player Owns Company

                    // Get field owner
                    Player fieldOwner = brewery.getOwner();

                    if (currentPlayer.getPlacement() == 12) {
                        otherBrewery = (Ownable) board.getField(27);
                    } else {
                        otherBrewery = (Ownable) board.getField(12);
                    }

                    // 1. Subtract rent from current player 2. add to field owner
                    if (brewery.getOwner() == otherBrewery.getOwner()) {
                        currentPlayer.setPlayerBalance(-brewery.getRent()[1] * sum);
                        fieldOwner.setPlayerBalance(brewery.getRent()[1] * sum);
                    } else {
                        currentPlayer.setPlayerBalance(-brewery.getRent()[0] * sum);
                        fieldOwner.setPlayerBalance(brewery.getRent()[0] * sum);
                    }
                }

                /*if (field instanceof Company) {

                    // Typecast to Model.Property
                    Company property = (Company) field;
                    // Typecast other company
                    Company otherCompany = (Company) board.getField(12);
                    if (player.getPlacement() == 12)
                    {
                        otherCompany = (Company) board.getField(27);
                    }

                    // Check Company Rent
                    int company_cost = sum*100;
                    if (property.getOwner() == otherCompany.getOwner())
                    {
                        company_cost = sum*200;
                    }

                    // Noone owns Company
                    if (property.getOwner() == null)
                    {
                        // Subtract player balance from Model.Property rent
                        player.setPlayerBalance(-property.getRent());

                        //Get the GUI-object and display the current player balance
                        player.getPlayer().setBalance(player.getPlayerBalance());

                        // Set Model.Property owner
                        property.setOwner(player);

                        // Set GUI Model.Field
                        GUI_Ownable ownable = (GUI_Ownable) board.getField(placement).getGUIField();
                        ownable.setOwnerName(player.getName());
                        ownable.setBorder(player.getPlayerColor());
                    }
                    else // Other player Owns Company
                    {
                        // Get field owner
                        Player fieldOwner = property.getOwner();

                        // 1. Subtract rent from current player 2. add to field owner
                        player.setPlayerBalance(-company_cost);
                        fieldOwner.setPlayerBalance(company_cost);

                        //Update GUI-object and display the current player balance
                        board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                        fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());
                    }
                }*/
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
    public void playerOptions(Player player) {

        String[] options = {"Roll Die", "Begin Trade"};

        switch (gui.dropdown("What do you want to do?", options)) {
            case "Begin Trade":
                trade(gui, playerindex);
                break;
            case "Roll Die":
                break;
        }

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


    public void tradeOrNotOption(GUI gui, int curPlayer)
    {
        switch (gui.getUserSelection("Do you want to trade?",  "Roll Die","Begin Trade"))
        {
            case "Begin Trade":
                trade(gui, curPlayer);
                break;
            case "Roll Die":

                break;
        }
    }

    public int buyOrSellOption(GUI gui, int curPlayer, int propertyWorth)
    {
        int winnerId=0;
        switch (gui.getUserSelection("Buy or Auction Property?", "Buy", "Start Auction"))
        {
            case "Buy":
                winnerId=curPlayer;
                break;
            case "Start Auction":
                winnerId = auction(gui, propertyWorth);
            break;
        }
        return winnerId;
    }
*/
    private void trade(GUIController gui, int curPlayer)
    {
        // Initialize Trade variables
        int tradePartnerId=0;
        int tradePartnerPayed=0;
        int traderPayed=0;

        // Add possible player to trade options array
        Player[] tradePlayers = new Player[ players.length -1];
        int a=0;
        for (int i = 0; i< players.length-1; i++) {

            if (i == curPlayer){a++;}
                tradePlayers[i] = players[a];
                //System.out.println(tradePlayers[i].getName());
            a++;
        }

        // Create String array of possible trade partners
        String[] tradePlayersNames = new String[tradePlayers.length];
        for (int i = 0; i<players.length-1; i++)
        {
            tradePlayersNames[i] = tradePlayers[i].getName();
        }

        // Display array as Dropdown Menu
        for(int i = 0; i<tradePlayers.length; i++){
            System.out.println(tradePlayers[i].getName());
        }
        System.out.println();

        // Create a dropdown based on tradeable player amount
        /*String selectedTradePartner = gui.getUserSelection(board.getPlayer(curPlayer).getName() + " Vælg spiller at handle med", tradePlayersNames);
        if (tradePlayersNames[0] == selectedTradePartner)
        {
            tradePartnerId=0;
            System.out.println("Tradepartner: "+tradePlayersNames[0]);
        }
        if (tradePlayersNames[1] == selectedTradePartner)
        {
            tradePartnerId=1;
            System.out.println("Tradepartner: "+tradePlayersNames[1]);
        }
        if (tradePlayersNames.length > 2)
        {
            if (tradePlayersNames[2] == selectedTradePartner) {
                tradePartnerId = 2;
                System.out.println("Tradepartner: " + tradePlayersNames[2]);
            }
        }
        if (tradePlayersNames.length > 3)
        {
            if (tradePlayersNames[3] == selectedTradePartner) {
                tradePartnerId = 3;
                System.out.println("Tradepartner: " + tradePlayersNames[3]);
            }
        }
        if (tradePlayersNames.length > 4)
        {
            if (tradePlayersNames[4] == selectedTradePartner) {
                tradePartnerId = 4;
                System.out.println("Tradepartner: " + tradePlayersNames[4]);
            }
        }



        // Display Chosen players property


        // Display Menu for money
        boolean correctPartnerPayAmount=false;
        while (!correctPartnerPayAmount) {
            switch (gui.getUserSelection(board.getPlayer(curPlayer).getName() + " Vælg hvor meget " + tradePlayersNames[tradePartnerId] + " skal betale: " + tradePartnerPayed, "Accepter mængde", "+50", "+100", "+200", "+500", "+1000", "+5000", "+10000")) {
                case "Accepter mængde":
                    correctPartnerPayAmount=true;
                    break;
                case "+50":
                    tradePartnerPayed+=50;
                    break;
                case "+100":
                    tradePartnerPayed+=100;
                    break;
                case "+200":
                    tradePartnerPayed+=200;
                    break;
                case "+500":
                    tradePartnerPayed+=500;
                    break;
                case "+1000":
                    tradePartnerPayed+=1000;
                    break;
                case "+5000":
                    tradePartnerPayed+=5000;
                    break;
                case "+10000":
                    tradePartnerPayed+=10000;
                    break;
            }
        }

        // Display own players Property

        // Display menu for own players money
        boolean correctPlayerPayAmount=false;
        while (!correctPlayerPayAmount) {
            switch (gui.getUserSelection(board.getPlayer(curPlayer).getName() + " Vælg hvor meget du skal betale: " + traderPayed, "Accepter mængde", "+50", "+100", "+200", "+500", "+1000", "+5000", "+10000")) {
                case "Accepter mængde":
                    correctPlayerPayAmount=true;
                    break;
                case "+50":
                    traderPayed+=50;
                    break;
                case "+100":
                    traderPayed+=100;
                    break;
                case "+200":
                    traderPayed+=200;
                    break;
                case "+500":
                    traderPayed+=500;
                    break;
                case "+1000":
                    traderPayed+=1000;
                    break;
                case "+5000":
                    traderPayed+=5000;
                    break;
                case "+10000":
                    traderPayed+=10000;
                    break;
            }
        }

        // Display Yes/No option to recipient
        boolean tradeAccepted=false;
        switch (gui.getUserSelection(tradePlayersNames[tradePartnerId] + " Acceptere du denne handel? Du modtager " + (traderPayed-tradePartnerPayed) +" og disse ejendomme: ", "Accepter handel","Accepter IKKE handel")) {

            case "Accepter handel":
                tradeAccepted=true;
                break;
            case "Accepter IKKE handel":
                tradeAccepted=false;
                break;
        }

        // Transfer Ownership

        // Pay for trade
        if (tradeAccepted) {
            tradePlayers[tradePartnerId].setPlayerBalance(traderPayed - tradePartnerPayed);
            board.getPlayer(curPlayer).setPlayerBalance(tradePartnerPayed - traderPayed);

            // Opdate GUI
            board.getPlayer(curPlayer).getPlayer().setBalance(board.getPlayer(curPlayer).getPlayerBalance());
            tradePlayers[tradePartnerId].getPlayer().setBalance(tradePlayers[tradePartnerId].getPlayerBalance());

        }
    }
*/}
    /*public int auction(GUI gui, int propertyWorth) {

        // Add players to auction array
            Player[] aucPlayers = new Player[board.getPlayerArray().length];
            for (int q = 0; q<board.getPlayerArray().length; q++)
            {
                aucPlayers[q] = board.getPlayerArray()[q];
            }

        // Initialize Auction start variables
        int auctionWinner=0;
        int curAucIndex = 0;
            Player curAucPlayer = aucPlayers[curAucIndex];
            int auctionSum = 0;
            int auctionPlayersLeft = aucPlayers.length;

        // Bid/Pass
        while (auctionPlayersLeft != 1)
        {
            switch (gui.getUserSelection("Current Bid Amount: " + (auctionSum) + " " + curAucPlayer.getName() + ": Select Bid Option", "Pass", "100", "200", "500", "1000", "2000")) {
                case "Pass":
                    auctionPlayersLeft -= 1;
                    aucPlayers[curAucIndex]=null;
                    break;
                case "100":
                    auctionSum += 100;
                    break;
                case "200":
                    auctionSum += 200;
                    break;
                case "500":
                    auctionSum += 500;
                    break;
                case "1000":
                    auctionSum += 1000;
                    break;
                case "2000":
                    auctionSum += 2000;
                    break;
            }
            // Next Player
            if (curAucIndex+1 != aucPlayers.length) // If this player is not the last
            {
                if (aucPlayers[curAucIndex + 1] == null) // If the next player should be skipped
                {
                    curAucIndex++;
                    while (aucPlayers[curAucIndex] == null) // Skip them
                    {
                        curAucIndex++;
                        if (curAucIndex == aucPlayers.length) {
                            curAucIndex = 0;
                        }
                    }
                }
                else // Else Proceed
                {
                    curAucIndex++;
                }
            }
            else
            {
                // If the player is the last player
                if (aucPlayers[0] == null) // And the first player should be skipped
                {
                    curAucIndex=0; // Check next player
                    while (aucPlayers[curAucIndex] == null) // Skip them
                    {
                        curAucIndex++;
                        if (curAucIndex == aucPlayers.length) {
                            curAucIndex = 0;
                        }
                    }
                }
                else
                {
                    // If the first player shouldn't be skipped
                    curAucIndex = 0;
                }
            }
            // Loop around
            if (curAucIndex == aucPlayers.length) {
                curAucIndex = 0;
            }
            // Set Current Player
            curAucPlayer = aucPlayers[curAucIndex];
        }
        // Return winner of Auction
        for (int i=0; i<aucPlayers.length; i++)
        {
            if (aucPlayers[i] != null)
            {
                auctionWinner=i;
            }
        }

        // Pay for auction
        if (auctionSum > propertyWorth)
        {
            // After the auction you will be charged for the property, here we account for that
            board.getPlayer(auctionWinner).setPlayerBalance(-auctionSum + propertyWorth);
        }
        else
        {
            board.getPlayer(auctionWinner).setPlayerBalance(+propertyWorth);
        }
        return auctionWinner;
    }
}

    /*public int netWorth(Player player) {
        int netWorth = player.getPlayerBalance();

        for (int i = 0; i < board.getFieldsTotal(); i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                if (player == property.getOwner()) {
                    netWorth += ((Property) board.getField(i)).getRent();
                }
            }
        }
        System.out.println(player.getName() + "'s net worth: " + netWorth);
        return netWorth;
    }
    //Copies the player array except the bankrupt player
    public Player[] eliminatePlayer() {
        Player[] newPlayers = new Player[board.amountofPlayers() - 1];
        int j = 0;
        for (int i = 0; i < board.amountofPlayers(); i++) {
            Player currentPlayer = board.getPlayer(i);
            if (!(bankrupt(currentPlayer, currentPlayer.getPlacement()))) {
                newPlayers[j] = board.getPlayer(i);
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
                if (player.getPlayerBalance() < ((Property) board.getField(placement)).getRent()) {
                    //Checks if player is bankrupt
                    if (netWorth(player) < ((Property) board.getField(placement)).getRent()) {
                        bankrupt = true;
                        for (int i = 0; i < board.getFieldsTotal(); i++) {
                            //Type casting field to Ownable
                            if (board.getField(i) instanceof Ownable) {
                                //Verifying that the current field is of the type Ownable
                                Ownable playerProperty = (Ownable) board.getField(i);
                                //Gives players properties to debt collector
                                if (player == playerProperty.getOwner()) {
                                    playerProperty.setOwner(((Property) board.getField(placement)).getOwner());
                                }
                            }
                        }
                        //Removes player from the player array, Note: does not work if more players bankrupt same turn
                        eliminatePlayer();
                    } else if (netWorth(player) > ((Property) board.getField(placement)).getRent()) {
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


    - Make an array with the player's properties.
    - Create drop-down menu (GUI) to select different owned properties for player to sell.
    - Loop the drop-down menu option until the balance is higher than the rent.
     */
    //Method to mortgage properties when bankrupt
    /*public void mortage(Player player) {
        //Property[] playerProperties = new Property[4];
        int numberOfProperties;
        numberOfProperties = board.countNumbersOfPropertiesForPlayer(player);

        Property[] playerProperties = new Property[numberOfProperties];
        int currentProperty = 0;
        for (int i = 0; i < board.getFieldsTotal(); i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                if (player == property.getOwner()) {
                    playerProperties[currentProperty] = (Property) property;
                    currentProperty++;
                }
            }
        }

        While loop that checks if balance is higher than rent

     */
        /*
        while(player.getPlayerBalance() < ((Property) board.getField(player.getPlacement())).getRent()) {

            board.getGui().getUserSelection("Bankerot eller pantsæt ejendomme?", "Bankerot", "Pantsæt");
            switch(board.getGui().getUserSelection("Bankerot eller pantsæt ejendomme?", "Bankerot", "Pantsæt")) {
                case "Bankerot":
                    player.setPlayerBalance(1);
                    eliminatePlayer();
                    break;
                case "Pantsæt":
                    /*
                    - Make options with properties in an array to choose to mortgage.
                    - OR make the program automatically sell a players properties

                    numberOfProperties = board.countNumbersOfPropertiesForPlayer(player);
                    String[] propertyNames = new String[numberOfProperties];
                    currentProperty = 0;

                    for (int i = 0; i < board.getFieldsTotal(); i++) {
                        //Type casting field to Ownable
                        if (board.getField(i) instanceof Ownable) {
                            //Verifying that the current field is of the type Ownable
                            Ownable property = (Ownable) board.getField(i);
                            if (player == property.getOwner()) {
                                String propertyName = ((Property) property).getName();
                                propertyNames[currentProperty] = propertyName;

                                currentProperty++;
                            }
                        }
                    }
                    String guiSelection = board.getGui().getUserSelection("Vælg en ejendom du skal sælge:", propertyNames);
                    switch (guiSelection) {

                    }
                    break;
            }
        }
    }*/
}