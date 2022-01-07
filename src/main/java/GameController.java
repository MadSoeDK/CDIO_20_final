import Model.*;
import gui_fields.GUI_Ownable;

public class GameController {
    Board board;
    Cup cup;

    // Initializing Variables
    int sum = 0;
    int currentPlayer = 0;
    int same_color_owner = 0;
    int rent_mutiplier = 1;

    public GameController() {
        board = new Board();
        cup = new Cup();
        board.newGame();
    }

    public void playGame() {
        do {
            takeTurn();
        } while (!board.checkWinner());
    }

    public void takeTurn() {
        // Roll Model.Cup
        board.button(currentPlayer);
        cup.roll();
        sum = cup.getSum();

        // Get Player pre-turn information
        Player player = board.getPlayer(currentPlayer);
        int placement = player.getPlacement();

        moveplayer(placement, player);

        Field field = board.getField(placement);

        checkFieldType(field, placement, player);
        nextTurn();
        netWorth(player);
        bankrupt(player, placement);
        checkWinner();

    }

    public void nextTurn() {
        // Chance Model.Player Turn/Reset to first player
        currentPlayer++;
        if (currentPlayer == board.amountofPlayers()) {
            currentPlayer = 0;
        }
        board.updateCurrentPlayer(currentPlayer);
    }

    public void checkFieldType(Field field, int placement, Player player) {
        // Check field type
        if (field instanceof Property) {

            // Typecast to Model.Property
            Property property = (Property) field;

            // Model.Field is owned by another player and it's not the current player
            if (property.getOwner() != player && property.getOwner() != null) {

                // Get field owner
                Player fieldOwner = property.getOwner();

                // Check if both fields of same color is owned by the same player
                if (placement - 2 < 0) {
                    for (int i = 0; i < (placement + 3); i++) {
                        // Check 2 field in either direction
                        if (board.getField(i) instanceof Property) {
                            // Typecast to Model.Property
                            Property property_check = (Property) board.getField(i);

                            // Check if owner/color is the same
                            if (property_check.getColor() == property.getColor() && property_check.getOwner() == property.getOwner()) {
                                same_color_owner++;
                            }
                        }
                    }
                } else {
                    for (int i = placement - 2; i < placement + 3; i++) {
                        // Check 2 field in either direction
                        if (i < 24) {
                            if (board.getField(i) instanceof Property) {
                                // Typecast to Model.Property
                                Property property_check = (Property) board.getField(i);
                                // Check if owner/color is the same
                                if (property_check.getColor() == property.getColor() && property_check.getOwner() == property.getOwner()) {
                                    same_color_owner++;
                                }
                            }
                        }
                    }
                }

                // Increase rent if owner owns entire color
                if (same_color_owner == 2) {
                    rent_mutiplier = 2;
                } else {
                    rent_mutiplier = 1;
                }

                // 1. Subtract rent from current player 2. add to field owner
                player.setPlayerBalance(-property.getRent() * rent_mutiplier);
                fieldOwner.setPlayerBalance(property.getRent() * rent_mutiplier);

                //Update GUI-object and display the current player balance
                board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());

                // Reset
                same_color_owner = 0;
            }

            // No one owns the Model.Property
            if (property.getOwner() == null) {

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
        }

        if (field instanceof Jail) {

            // Typecast to Model.Property
            Jail jail = (Jail) field;

            // Subtract player balance from Model.Property rent. 2. Update GUI
            player.setPlayerBalance(-jail.getRent());
            player.getPlayer().setBalance(player.getPlayerBalance());

            // Add money to Free Parking if landed on "Go To Model.Jail"
            if (placement == 18) {
                FreeParking.setBalance(3);
            }

            // Set GUI Balance
            board.getField(12).getGUIField().setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));

            // Move to Model.Jail field
            player.gotoPlacement(6);
            placement = player.getPlacement();

            // Update GUI with new placement
            board.movePlayer(currentPlayer, placement);
            board.removePlayer(currentPlayer, 18);
        }

        if (field instanceof FreeParking) {

            // Give money to player
            player.setPlayerBalance(FreeParking.getBalance());
            player.getPlayer().setBalance(player.getPlayerBalance());

            // Reset Free Parking
            FreeParking.resetBalance();

            // Set GUI Balance
            field.getGUIField().setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));

        }
    }

    public void moveplayer(int placement, Player player) {
        // Remove player from board
        board.removePlayer(currentPlayer, placement);

        // Display dice roll on GUI
        board.setDice(sum);

        // Check for a complete lap around on board. Then recalibrate player placement
        if (placement + sum >= 40) {
            player.setPlacement(sum - 40);
            board.removePlayer(currentPlayer, placement);
            sum = 0;

            // Pass Model.Start field and gain $2
            player.setPlayerBalance(2);
        }

        // Set new placement incremented by dice. Then get the new placement.
        player.setPlacement(sum);
        placement = player.getPlacement();

        // Update GUI with new placement
        board.movePlayer(currentPlayer, placement);
    }

    public void checkWinner() {
        //board.guiMessage(board.getPlayer(board.getWinner()).getName()+" HAS WON THE GAME!");
        if (board.getCurrentPlayer().getPlayerBalance() < 11) {
            board.guiMessage(board.getPlayer(board.getWinner()).getName() + "WON THE GAME!");
        }
    }

    public int netWorth(Player player) {
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

    public boolean bankrupt(Player player, int placement) {
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
                                //Gives players porperties to debt collecter
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
                         */
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
    public void mortage(Player player) {
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
        /*
        While loop that checks if balance is higher than rent
         */
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
                     */
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
    }
}