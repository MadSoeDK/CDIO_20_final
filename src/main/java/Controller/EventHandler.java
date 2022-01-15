package Controller;

import Model.*;
import Model.Board.*;
import Utility.Language;

import java.util.Objects;

public class EventHandler {

    private final GUIController gui;

    public EventHandler(GUIController gui) {
        this.gui = gui;
    }

    /**
     * Asks player if they wish to trade with another player, then runs methods accordingly.
     *
     * @param player
     * @param players
     * @param board
     */
    public void playerOptionsTrade(Player player, Player[] players, Board board) {
        int playerIndex = board.getField(player.getPlacement()).getPlacement();
        boolean answer = gui.getUserBool(Language.getText("playeroptionstrade1"), Language.getText("yes"), Language.getText("no"));

        if (answer) {
            trade(playerIndex, players, board);
        }
        // Roll dice
    }

    /**
     * Asks player if they wish to buyback mortgaged properties, then runs methods accordingly.
     *
     * @param player
     * @param board
     */
    public void playerOptionsBuyMortgaged(Player player, Board board) {
        boolean answer = gui.getUserBool(Language.getText("playeroptionsbuymortgage1"), Language.getText("yes"), Language.getText("no"));
        if (answer) {
            buyMortgage(player, board);
        }
        // Roll dice
    }

    public void fieldEffect(Player player, Street street, Board board, Player[] players) {
        if (street.getOwner() == null) { // No owner - ask to buy it
            buyField(player, street, players);
        } else { //Pay rent to owner
            Player fieldOwner = street.getOwner();

            // Check if Owner has Monopoly
            if (board.hasMonopoly(player.getPlacement()) && street.getHouseAmount() == 0) {
                // 1. Subtract rent from current player 2. add to field owner
                player.setPlayerBalance(-street.getCurrentRent() * 2);
                fieldOwner.setPlayerBalance(street.getCurrentRent() * 2);
            } else {
                player.setPlayerBalance(-street.getCurrentRent());
                fieldOwner.setPlayerBalance(street.getCurrentRent());
            }
        }
    }

    /**
     * Ferry Effect: Player pays rent. Rent is based on amount of ferries owned by same player.
     *
     * @param player
     * @param ferry
     * @param board
     * @param players
     */
    public void fieldEffect(Player player, Ferry ferry, Board board, Player[] players) {
        // Check Ferry Rent
        ferry.updateNumberOfFerriesOwned(player, board);
        int ferry_cost = ferry.getCurrentRent();//ferry.getCurrentRent();

        if (ferry.getOwner() == null) { // Noone owns Ferry
            buyField(player, ferry, players);
        } else {
            // Get field owner
            Player fieldOwner = ferry.getOwner();

            // 1. Subtract rent from current player 2. add to field owner
            player.setPlayerBalance(-ferry_cost);
            fieldOwner.setPlayerBalance(ferry_cost);
        }
    }

    /**
     * Brewery Effect: Player pays rent. Rent is based on sum of dice and ownership of other brewery.
     *
     * @param player
     * @param brewery
     * @param players
     * @param board
     * @param sum
     */
    public void fieldEffect(Player player, Brewery brewery, Player[] players, Board board, int sum) {
        Player fieldOwner = brewery.getOwner();
        brewery.updateNumberOfBreweryOwned(player, board);
        int breweryCost = brewery.getCurrentRent();
        if (fieldOwner == null) {
            buyField(player, brewery, players);
        } else {
            player.setPlayerBalance(-breweryCost * sum);
            fieldOwner.setPlayerBalance(breweryCost * sum);
        }
    }

    /**
     * Player pays either 4000 or 10% of netWorth.
     *
     * @param player
     * @param tax
     * @param netWorth
     */
    public void fieldEffect(Player player, Tax tax, int netWorth) {
        if (tax.getPlacement() == 4) { // first tax field
            boolean answer = gui.getUserBool(Language.getText("tax1"), "4000" + Language.getText("valuta"), "10%");
            if (answer) {
                player.setPlayerBalance(-4000);
            } else {
                player.setPlayerBalance(-(int) (netWorth * (10.0f / 100.0f)));
            }
        } else { // last tax field
            player.setPlayerBalance(-2000);
        }
    }

    /**
     * Asks the player if they wish to buy or auction the property, then runs the code accordingly
     *
     * @param player
     * @param field
     * @param players
     */
    public void buyField(Player player, Ownable field, Player[] players) {
        boolean answer = gui.getUserBool(Language.getText("buyfield1"), Language.getText("buyfield2"), Language.getText("buyfield3"));

        // Buy if yes
        if (answer) {
            player.setPlayerBalance(-field.getPrice());
            field.setOwner(player);
            gui.setOwner(player, field);
        } else {
            auction(players, field);
        }
    }

    /**
     * The players take turns bidding on property if the first players doesn't buy it first
     *
     * @param players
     * @param field
     */
    public void auction(Player[] players, Ownable field) {
        // Add players to auction array
        Player[] aucPlayers = new Player[players.length];
        System.arraycopy(players, 0, aucPlayers, 0, players.length);

        // Initialize Auction start variables
        int auctionWinner = 0;
        int curAucIndex = 0;
        Player curAucPlayer = aucPlayers[curAucIndex];
        int auctionSum = 0;
        int auctionPlayersLeft = aucPlayers.length;

        // Bid/Pass
        while (auctionPlayersLeft != 1) {
            String[] options = {"Afvis", "100", "200", "500", "1000", "2000"};
            switch (gui.getUserSelection("Nuværende totale bud: " + (auctionSum) + " " + curAucPlayer.getName() + ": Vælg budvalgmulighed", options)) {
                case "Afvis":
                    auctionPlayersLeft -= 1;
                    aucPlayers[curAucIndex] = null;
                    break;
                case "100":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum + 100) {
                        auctionSum += 100;
                    } else {
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
                case "200":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum + 200) {
                        auctionSum += 200;
                    } else {
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
                case "500":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum + 500) {
                        auctionSum += 500;
                    } else {
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
                case "1000":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum + 1000) {
                        auctionSum += 1000;
                    } else {
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
                case "2000":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum + 2000) {
                        auctionSum += 2000;
                    } else {
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
            }
            // Next Player
            if (curAucIndex + 1 != aucPlayers.length) // If this player is not the last
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
                } else {// Else Proceed
                    curAucIndex++;
                }
            } else {
                // If the player is the last player
                if (aucPlayers[0] == null) {// And the first player should be skipped
                    curAucIndex = 0; // Check next player
                    while (aucPlayers[curAucIndex] == null) { //Skip them
                        curAucIndex++;
                        if (curAucIndex == aucPlayers.length) {
                            curAucIndex = 0;
                        }
                    }
                } else {
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
        for (int i = 0; i < aucPlayers.length; i++) {
            if (aucPlayers[i] != null) {
                auctionWinner = i;
            }
        }

        // Pay for auction
        players[auctionWinner].setPlayerBalance(-auctionSum);
        field.setOwner(players[auctionWinner]);
        gui.setOwner(players[auctionWinner], field);
        /*
        if (auctionSum > propertyWorth) {
            // After the auction you will be charged for the property, here we account for that
            players[auctionWinner].setPlayerBalance(-auctionSum + propertyWorth);
        } else {
            players[auctionWinner].setPlayerBalance(+propertyWorth);
        }
        return auctionWinner;

         */
    }

    /**
     * The player choses tradepartner, Chosen property to buy, Chosen amount of money from player, Chosen Property to sell, Chosen money to spend.
     * The target player then choses to accept or decline the trade. If accepted the ownership of the properties switches.
     */
    public void trade(int curPlayer, Player[] players, Board board) {

        Player currentPlayer = players[curPlayer];

        // Initialize Trade variables
        int tradePartnerId = 0;
        int tradePartnerPayed = 0;
        int traderPayed = 0;

        // Add possible player to trade options array
        Player[] tradePlayers = new Player[players.length - 1];
        int a = 0;
        for (int i = 0; i < players.length - 1; i++) {
            if (i == curPlayer) {
                a++;
            }
            tradePlayers[i] = players[a];
            a++;
        }

        // Create String array of possible trade partners
        String[] tradePlayersNames = new String[tradePlayers.length];
        for (int i = 0; i < players.length - 1; i++) {
            tradePlayersNames[i] = tradePlayers[i].getName();
        }

        // Create a dropdown based on tradeable player amount
        String selectedTradePartner = gui.getUserSelection(currentPlayer.getName() + " Vælg spiller at handle med", tradePlayersNames);

        for (int i = 0; i < tradePlayersNames.length; i++) {
            if (Objects.equals(tradePlayersNames[i], selectedTradePartner)) {
                tradePartnerId = i;
            }
        }

        // Display Chosen players property
        Ownable[] tradePartProperties = board.getPlayerProperties(tradePlayers[tradePartnerId]);
        String[] fieldNames = new String[tradePartProperties.length];
        String[] options;
        String fieldName;
        Ownable chosenProp = null;
        if (gui.getUserBool("Vil du købe en af spillerens ejendomme?", "Ja, Køb ejendom", "Køb ikke ejendom")) {
            if (tradePartProperties.length > 0) {
                for (int i = 0; i < tradePartProperties.length; i++) {
                    fieldNames[i] = tradePartProperties[i].getName();
                }
                options = fieldNames;
                chosenProp = tradePartProperties[0];
                fieldName = gui.getUserSelection(currentPlayer.getName() + " Vælg hvilke ejendomme du vil købe", options);
                for (int i = 0; i < fieldNames.length; i++) {
                    if (fieldNames[i].equals(fieldName)) {
                        chosenProp = tradePartProperties[i];
                    }
                }
            }
        }

        // Display Menu for money
        boolean correctPartnerPayAmount = false;
        String[] optionsMoney = {"Accepter mængde", "+50", "+100", "+200", "+500", "+1000", "+5000", "+10000"};
        while (!correctPartnerPayAmount) {

            switch (gui.getUserSelection(currentPlayer.getName() + " Vælg hvor meget " + tradePlayersNames[tradePartnerId] + " skal betale: " + tradePartnerPayed, optionsMoney)) {
                case "Accepter mængde":
                    correctPartnerPayAmount = true;
                    break;
                case "+50":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed + 50) {
                        tradePartnerPayed += 50;
                    }
                    break;
                case "+100":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed + 100) {
                        tradePartnerPayed += 100;
                    }
                    break;
                case "+200":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed + 200) {
                        tradePartnerPayed += 200;
                    }
                    break;
                case "+500":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed + 500) {
                        tradePartnerPayed += 500;
                    }
                    break;
                case "+1000":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed + 1000) {
                        tradePartnerPayed += 1000;
                    }
                    break;
                case "+5000":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed + 5000) {
                        tradePartnerPayed += 5000;
                    }
                    break;
                case "+10000":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed + 10000) {
                        tradePartnerPayed += 10000;
                    }
                    break;
            }
        }

        // Display own players Property
        Ownable[] traderProperties = board.getPlayerProperties(currentPlayer);
        fieldNames = new String[traderProperties.length];
        Ownable chosenSoldProp = null;
        if (gui.getUserBool("Vil du sælge en af dine ejendomme?", "Ja, Sælg en ejendom", "Sælg ikke ejendom")) {
            if (traderProperties.length > 0) {
                for (int i = 0; i < traderProperties.length; i++) {
                    fieldNames[i] = traderProperties[i].getName();
                }
                fieldName = gui.getUserSelection(currentPlayer.getName() + " Vælg hvilke ejendomme du vil sælge", fieldNames);
                for (int i = 0; i < fieldNames.length; i++) {
                    if (fieldNames[i].equals(fieldName)) {
                        chosenSoldProp = traderProperties[i];
                    }
                }
            }
        }

        // Display menu for own players money
        boolean correctPlayerPayAmount = false;
        while (!correctPlayerPayAmount) {
            switch (gui.getUserSelection(currentPlayer.getName() + " Vælg hvor meget du skal betale: " + traderPayed, optionsMoney)) {
                case "Accepter mængde":
                    correctPlayerPayAmount = true;
                    break;
                case "+50":
                    if (currentPlayer.getPlayerBalance() >= traderPayed + 50) {
                        traderPayed += 50;
                    }
                    break;
                case "+100":
                    if (currentPlayer.getPlayerBalance() >= traderPayed + 100) {
                        traderPayed += 100;
                    }
                    break;
                case "+200":
                    if (currentPlayer.getPlayerBalance() >= traderPayed + 200) {
                        traderPayed += 200;
                    }
                    break;
                case "+500":
                    if (currentPlayer.getPlayerBalance() >= traderPayed + 500) {
                        traderPayed += 500;
                    }
                    break;
                case "+1000":
                    if (currentPlayer.getPlayerBalance() >= traderPayed + 1000) {
                        traderPayed += 1000;
                    }
                    break;
                case "+5000":
                    if (currentPlayer.getPlayerBalance() >= traderPayed + 5000) {
                        traderPayed += 5000;
                    }
                    break;
                case "+10000":
                    if (currentPlayer.getPlayerBalance() >= traderPayed + 10000) {
                        traderPayed += 10000;
                    }
                    break;
            }
        }

        // Display Yes/No option to recipient
        String soldPropString = " Og ingen ejendomme: ";
        String propString = " For ingen ejendomme: ";
        boolean tradeAccepted;
        if (chosenProp != null) {
            propString = " For denne egendom: " + chosenProp.getName();
        }
        if (chosenSoldProp != null) {
            soldPropString = " og denne egendom " + chosenSoldProp.getName();
        }
        String msg = tradePlayersNames[tradePartnerId] + " Acceptere du denne handel? Du modtager " + (traderPayed - tradePartnerPayed) + soldPropString + propString;
        boolean answer = gui.getUserBool(msg, "Accepter handel", "Accepter IKKE handel");

        tradeAccepted = answer;

        // Pay for trade
        if (tradeAccepted) {
            tradePlayers[tradePartnerId].setPlayerBalance(traderPayed - tradePartnerPayed);
            currentPlayer.setPlayerBalance(tradePartnerPayed - traderPayed);
            if (chosenProp != null) {
                chosenProp.setOwner(currentPlayer);
                //gui.setOwner(currentPlayer, chosenProp);
                gui.setOwner(currentPlayer, chosenProp);
            }
            if (chosenSoldProp != null) {
                chosenSoldProp.setOwner(tradePlayers[tradePartnerId]);
                //gui.setOwner(tradePlayers[tradePartnerId], chosenProp);
                gui.setOwner(tradePlayers[tradePartnerId], chosenSoldProp);
            }

            // Update Ownership
            gui.setguiPlayerBalance(currentPlayer, currentPlayer.getPlayerBalance());
            gui.setguiPlayerBalance(tradePlayers[tradePartnerId], tradePlayers[tradePartnerId].getPlayerBalance());
        }


    }

    /**
     * Allows players to buy back mortgaged properties.
     *
     * @param player
     * @param board
     */
    public void buyMortgage(Player player, Board board) {
        boolean value = true;
        int numberOfMortgagedProperties;
        numberOfMortgagedProperties = board.MortgagedPropertiesForPlayer(player);
        Ownable[] playerMortgagedProperties = new Ownable[numberOfMortgagedProperties];
        String[] mortgagedPropertyNames = new String[numberOfMortgagedProperties];
        int currentMortgagedProperty = 0;
        for (int i = 0; i < board.getFields().length; i++) {
            //Type casting field to Ownable
            if (board.getField(i) instanceof Ownable) {
                //Verifying that the current field is of the type Ownable
                Ownable property = (Ownable) board.getField(i);
                if (player == property.getOwner()) {
                    if (property.getMortgage()) {
                        playerMortgagedProperties[currentMortgagedProperty] = property;
                        mortgagedPropertyNames[currentMortgagedProperty] = property.getName();
                        currentMortgagedProperty++;
                    }
                }
            }
        }
        while (value) {
            //stops while loop if no mortgaged properties
            if (numberOfMortgagedProperties == 0) {
                gui.button("Du har ikke nogen pantsatte ejendomme", "OK");
                value = false;
            } else {
                String guiSelection = gui.dropdown("Vælg en pantsat ejendom du skal købe:", mortgagedPropertyNames);
                for (int i = 0; i < mortgagedPropertyNames.length; i++) {
                    //Verifying that the current field is of the type Ownable
                    Ownable property = playerMortgagedProperties[i];
                    if (property.getName().equals(guiSelection)) {
                        property.setMortgage(false);
                        playerMortgagedProperties[i].setOwner(player);
                        //set GUI mortgage
                        player.setPlayerBalance(-((property.getPrice()) / 2) + (((property.getPrice()) / 2) * 10 / 100));
                        String[] newMortgagedPropertyNames = new String[mortgagedPropertyNames.length - 1];
                        int j = 0;
                        if (newMortgagedPropertyNames.length == 0) {
                            gui.button("Du har ikke nogen pantsatte ejendomme", "ok");
                            value = false;
                        }
                        if (property.getOwner() == player && (property.getName().equals(guiSelection))) {
                            mortgagedPropertyNames[i] = newMortgagedPropertyNames[j];
                            j++;
                        }
                    }
                }
            }
        }
    }

}

