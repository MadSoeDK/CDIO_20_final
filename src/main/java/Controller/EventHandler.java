package Controller;

import Model.*;
import Model.Board.*;

public class EventHandler {

    private GUIController gui;

    public EventHandler(GUIController gui) {
        this.gui = gui;
    }

    public void playerOptionsTrade(Player player, Player [] players, Board board) {
        int playerIndex = java.util.Arrays.asList(players).indexOf(player);
        boolean answer = gui.getUserBool("Vil du handle?", "Ja", "Nej, gå tilbage");

        if (answer) {
                trade(playerIndex, players, board);
        }
        // Roll dice
    }

    public void playerOptionsBuyMortgaged(Player player, Board board) {
        boolean answer = gui.getUserBool("Vil du købe pantsatte ejendomme tilbage", "Ja", "Nej, gå tilbage");
        if (answer) {
            buyMortgage(player, board);
        }
        // Roll dice
    }

    public void fieldEffect(Player player, Ownable ownable, Player[] players) {
        if (ownable.getOwner() == null) {
            buyField(player, ownable, players);
        } else {
            Player fieldOwner = ownable.getOwner();

            if (false /*board.hasMonopoly(placement)*/) {
                // 1. Subtract rent from current player 2. add to field owner
                player.setPlayerBalance(-ownable.getCurrentRent() * 2);
                fieldOwner.setPlayerBalance(ownable.getCurrentRent() * 2);
            } else {
                player.setPlayerBalance(-ownable.getCurrentRent());
                fieldOwner.setPlayerBalance(ownable.getCurrentRent());
            }
        }
    }
    public void fieldEffect(Player player, Ownable ownable, Player[] players, int sum) {
        if (ownable.getOwner() == null) {
            buyField(player, ownable, players);
        } else {
            Player fieldOwner = ownable.getOwner();

            if (ownable.getOwner() == ownable.getOwner()) {
                player.setPlayerBalance(-ownable.getCurrentRent() * sum);
                fieldOwner.setPlayerBalance(ownable.getCurrentRent() * sum);
            } else {
                player.setPlayerBalance(-ownable.getCurrentRent() * sum);
                fieldOwner.setPlayerBalance(ownable.getCurrentRent() * sum);
            }
        }
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

    public void fieldEffect(Player player, Ferry ferry, Board board, Player[] players) {
        // Check Ferry Rent
        int ferry_cost = ferry.getRent(ferry,board);//ferry.getCurrentRent();

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

    public void fieldEffect(Player player, Brewery brewery, Player[] players, Board board, int sum) {
        // Typecast other company
        Ownable otherBrewery;

        // No one owns Company
        if (brewery.getOwner() == null) {
            buyField(player, brewery, players);
        } else { // Other player Owns Company

            // Get field owner
            Player fieldOwner = brewery.getOwner();

            if (brewery.getPlacement() == 12) {
                otherBrewery = (Ownable) board.getField(27);
            } else {
                otherBrewery = (Ownable) board.getField(12);
            }

            // 1. Subtract rent from current player 2. add to field owner
            if (brewery.getOwner() == otherBrewery.getOwner()) {
                player.setPlayerBalance(-brewery.getRent()[1] * sum);
                fieldOwner.setPlayerBalance(brewery.getRent()[1] * sum);
            } else {
                player.setPlayerBalance(-brewery.getRent()[0] * sum);
                fieldOwner.setPlayerBalance(brewery.getRent()[0] * sum);
            }
        }
    }

    public void fieldEffect(Player player, Jail jail) {

    }

    public void fieldEffect(Player player, Tax tax, int netWorth) {
        if (tax.getPlacement() == 4) { // first tax field
            boolean answer = gui.getUserBool("Betal 10% eller 4000 kr?", "4000 kr.", "10%");
            if (answer) {
                player.setPlayerBalance(-4000);
            } else {
                player.setPlayerBalance(-(int)(netWorth*(10.0f/100.0f)));
            }
        } else { // last tax field
            player.setPlayerBalance(-2000);
        }
    }

    public void buyField(Player player, Ownable field, Player[] players) {
        //boolean answer = gui.getUserBool("Buy this field?", "Yes", "No");
        boolean answer = gui.getUserBool("Køb dette felt?", "Køb", "Auktioner");

        // Buy if yes
        if (answer) {
            player.setPlayerBalance(-field.getPrice());
            field.setOwner(player);
            gui.setOwner(player, field);
        } else {
            auction(players, field);
        }
    }

    public void auction(Player[] players, Ownable field) {
        // Add players to auction array
        Player[] aucPlayers = new Player[players.length];
        for (int q = 0; q < players.length; q++) {
            aucPlayers[q] = players[q];
        }

        // Initialize Auction start variables
        int auctionWinner = 0;
        int curAucIndex = 0;
        Player curAucPlayer = aucPlayers[curAucIndex];
        int auctionSum = 0;
        int auctionPlayersLeft = aucPlayers.length;

        // Bid/Pass
        while (auctionPlayersLeft != 1) {
            String[] options = {"Pass", "100", "200", "500", "1000", "2000"};
            switch (gui.getUserSelection("Current Bid Amount: " + (auctionSum) + " " + curAucPlayer.getName() + ": Select Bid Option", options)) {
                case "Pass":
                    auctionPlayersLeft -= 1;
                    aucPlayers[curAucIndex] = null;
                    break;
                case "100":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum+100) {
                        auctionSum += 100;
                    }
                    else{
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
                case "200":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum+200) {
                        auctionSum += 200;
                    }
                    else{
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
                case "500":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum+500) {
                        auctionSum += 500;
                    }
                    else{
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
                case "1000":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum+1000) {
                        auctionSum += 1000;
                    }
                    else{
                        auctionPlayersLeft -= 1;
                        aucPlayers[curAucIndex] = null;
                    }
                    break;
                case "2000":
                    if (curAucPlayer.getPlayerBalance() >= auctionSum+2000) {
                        auctionSum += 2000;
                    }
                    else{
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

    public void trade(int curPlayer, Player[] players, Board board) {

        Player currentPlayer = players[curPlayer];

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
            a++;
        }

        // Create String array of possible trade partners
        String[] tradePlayersNames = new String[tradePlayers.length];
        for (int i = 0; i<players.length-1; i++) {
            tradePlayersNames[i] = tradePlayers[i].getName();
        }

        // Create a dropdown based on tradeable player amount
        String selectedTradePartner = gui.getUserSelection(currentPlayer.getName() + " Vælg spiller at handle med", tradePlayersNames);

        for (int i = 0; i < tradePlayersNames.length; i++) {
            if (tradePlayersNames[i] == selectedTradePartner) {
                tradePartnerId = i;
            }
        }

        // Display Chosen players property
        Ownable[] tradePartProperties = board.getPlayerProperties(tradePlayers[tradePartnerId]);
        String[] fieldNames = new String[tradePartProperties.length];
        String[] options = fieldNames;
        String fieldName = "";
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
        boolean correctPartnerPayAmount=false;
        String[] optionsMoney = {"Accepter mængde", "+50", "+100", "+200", "+500", "+1000", "+5000", "+10000"};
        while (!correctPartnerPayAmount) {

            switch (gui.getUserSelection(currentPlayer.getName() + " Vælg hvor meget " + tradePlayersNames[tradePartnerId] + " skal betale: " + tradePartnerPayed, optionsMoney)) {
                case "Accepter mængde":
                    correctPartnerPayAmount=true;
                    break;
                case "+50":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed+50) {
                        tradePartnerPayed += 50;
                    }
                    break;
                case "+100":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed+100) {
                        tradePartnerPayed += 100;
                    }
                    break;
                case "+200":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed+200) {
                        tradePartnerPayed += 200;
                    }
                    break;
                case "+500":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed+500) {
                        tradePartnerPayed += 500;
                    }
                    break;
                case "+1000":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed+1000) {
                        tradePartnerPayed += 1000;
                    }
                    break;
                case "+5000":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed+5000) {
                        tradePartnerPayed += 5000;
                    }
                    break;
                case "+10000":
                    if (tradePlayers[tradePartnerId].getPlayerBalance() >= tradePartnerPayed+10000){
                        tradePartnerPayed+=10000;
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
                String[] playerOptions = fieldNames;
                chosenSoldProp = null;
                fieldName = gui.getUserSelection(currentPlayer.getName() + " Vælg hvilke ejendomme du vil sælge", playerOptions);
                for (int i = 0; i < fieldNames.length; i++) {
                    if (fieldNames[i].equals(fieldName)) {
                        chosenSoldProp = traderProperties[i];
                    }
                }
            }
        }

        // Display menu for own players money
        boolean correctPlayerPayAmount=false;
        while (!correctPlayerPayAmount) {
            switch (gui.getUserSelection(currentPlayer.getName() + " Vælg hvor meget du skal betale: " + traderPayed, optionsMoney)) {
                case "Accepter mængde":
                    correctPlayerPayAmount=true;
                    break;
                case "+50":
                    if (currentPlayer.getPlayerBalance() >= traderPayed+50) {
                        traderPayed += 50;
                    }
                    break;
                case "+100":
                    if (currentPlayer.getPlayerBalance() >= traderPayed+100) {
                        traderPayed += 100;
                    }
                    break;
                case "+200":
                    if (currentPlayer.getPlayerBalance() >= traderPayed+200) {
                        traderPayed += 200;
                    }
                    break;
                case "+500":
                    if (currentPlayer.getPlayerBalance() >= traderPayed+500) {
                        traderPayed += 500;
                    }
                    break;
                case "+1000":
                    if (currentPlayer.getPlayerBalance() >= traderPayed+1000) {
                        traderPayed += 1000;
                    }
                    break;
                case "+5000":
                    if (currentPlayer.getPlayerBalance() >= traderPayed+5000) {
                        traderPayed += 5000;
                    }
                    break;
                case "+10000":
                    if (currentPlayer.getPlayerBalance() >= traderPayed+10000) {
                        traderPayed += 10000;
                    }
                    break;
            }
        }

        // Display Yes/No option to recipient
        String soldPropString = " Og ingen ejendomme: ";
        String propString = " For ingen ejendomme: ";
        boolean tradeAccepted=false;
        String[] optionsAccept = {};
        if (chosenProp != null) {propString = " For denne egendom: "+chosenProp.getName();}
        if (chosenSoldProp != null) {soldPropString = " og denne egendom "+chosenSoldProp.getName();}
        String msg = tradePlayersNames[tradePartnerId] + " Acceptere du denne handel? Du modtager " + (traderPayed-tradePartnerPayed) + soldPropString + propString;
        boolean answer =gui.getUserBool(msg, "Accepter handel","Accepter IKKE handel");

        if(answer)
        {
            tradeAccepted=true;
        }
        else
        {
            tradeAccepted=false;
        }

        // Transfer Ownership

        // Pay for trade
        if (tradeAccepted) {
            tradePlayers[tradePartnerId].setPlayerBalance(traderPayed - tradePartnerPayed);
            currentPlayer.setPlayerBalance(tradePartnerPayed - traderPayed);
            if (chosenProp != null){
                chosenProp.setOwner(currentPlayer);
                //gui.setOwner(currentPlayer, chosenProp);
                gui.setOwner(currentPlayer, chosenProp);
            }
            if (chosenSoldProp != null){
                chosenSoldProp.setOwner(tradePlayers[tradePartnerId]);
                //gui.setOwner(tradePlayers[tradePartnerId], chosenProp);
                gui.setOwner(tradePlayers[tradePartnerId], chosenSoldProp);
            }

            // Opdate GUI
            //board.getPlayer(curPlayer).getPlayer().setBalance(board.getPlayer(curPlayer).getPlayerBalance());
            //tradePlayers[tradePartnerId].getPlayer().setBalance(tradePlayers[tradePartnerId].getPlayerBalance());
            // Update Ownership

            gui.setguiPlayerBalance(currentPlayer,currentPlayer.getPlayerBalance());
            gui.setguiPlayerBalance(tradePlayers[tradePartnerId],tradePlayers[tradePartnerId].getPlayerBalance());
        }


    }

    public void buyMortgage(Player player, Board board) {
        boolean value = true;
        int numberOfMortgagedProperties;
        numberOfMortgagedProperties = board.countNumberOfMortgagedPropertiesForPlayer(player);
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
                gui.button("Du har ikke nogen pantsatte ejendomme","ok");
                value = false;
            }
            else {
                String guiSelection = gui.dropdown("Vælg en pantsat ejendom du skal købe:", mortgagedPropertyNames);
                for (int i = 0; i < mortgagedPropertyNames.length; i++) {
                    //Verifying that the current field is of the type Ownable
                    Ownable property = playerMortgagedProperties[i];
                    if (property.getName().equals(guiSelection)) {
                        property.setMortgage(false);
                        //set GUI mortgage
                        player.setPlayerBalance(-((property.getPrice()) / 2)+(((property.getPrice()) / 2) * 10/100));
                        String[] newMortgagedPropertyNames = new String[mortgagedPropertyNames.length - 1];
                        int j = 0;
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

