import Model.*;
import gui_fields.GUI_Ownable;

public class GameController {

    private Board board;
    private Cup cup;
    private GUIController gui;
    private Player[] players;
    private Player currentPlayer;

    // Game Constants
    final int STARTBALANCE = 30000;

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
            takeTurn();
            nextTurn();
        } while (true); //Check winner
    }

    public void takeTurn() {
        int sum = 0;

        gui.message("Nu er det " + currentPlayer.getName() + "'s tur");

        // Ask if they wish trade?
        playerOptions(currentPlayer);

        gui.button(" ", "Rul terning");

        // Roll die, get value, show die
        cup.roll();
        sum = 10;//cup.getSum();
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
        int playerindex = java.util.Arrays.asList(players).indexOf(currentPlayer);

        if (playerindex == players.length-1) {
            currentPlayer = players[0];
        } else {
            currentPlayer = players[playerindex + 1];
        }
    }
    public void checkFieldType(Field field, int placement) {

        String fieldType = board.getField(placement).getClass().getSimpleName();

        switch (fieldType) {
            case "Property":

                // Typecast to Property
                Property property = (Property) field;

                // Check if Field is owned by another player and it's not the current player
                if (property.getOwner() != currentPlayer && property.getOwner() != null) { //There is a Field owner and it's not owned by the current Player

                    // Get field owner
                    Player fieldOwner = property.getOwner();

                    // Check if Owner owns all colors
                    if (board.hasMonopoly(placement)) {
                        // 1. Subtract rent from current player 2. add to field owner
                        currentPlayer.setPlayerBalance(-property.getRent() * 2);
                        fieldOwner.setPlayerBalance(property.getRent() * 2);
                    }

                } else { // No one owns the Property, buy it.

                    // Subtract player balance from Property rent
                    currentPlayer.setPlayerBalance(-property.getRent());

                    // Set Property owner
                    property.setOwner(currentPlayer);

                    // Set GUI Field
                    gui.showOwner(currentPlayer, placement);
                }
                break;

            case "Jail":

                // Typecast to Jail
                Jail jail = (Jail) field;

                // Add money to Free Parking if landed on "Go To Jail"
                if (placement == 30) {
                    gui.message(currentPlayer.getName() + " rykker til fængsel og betaler $3");

                    // Subtract player balance from Property rent
                    currentPlayer.setPlayerBalance(-jail.getRent());

                    FreeParking.setBalance(3);

                    // Move to Jail field
                    //currentPlayer.setPlacement(6);
                    setPlayerPlacement(currentPlayer, 10, false);

                }
                // Set GUI Balance
                gui.getGuiField(placement).setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));
                break;

            case "FreeParking":
                // Give money to player
                currentPlayer.setPlayerBalance(FreeParking.getBalance());

                // Reset Free Parking
                FreeParking.resetBalance();

                // Set GUI Balance
                gui.getGuiField(placement).setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));
                break;

            case "Tax":
                //do something
                break;

            case "Ferry":
                //do something
                break;

            case "ChanceField":
                //do something
                break;
            case "Start":
                //do something
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
                //trade(gui, curPlayer);
                break;
            case "Roll Die":
                break;
        }

    }

    /*public Player checkWinner() {
        gui.guiMessage(board.getPlayer(board.getWinner()).getName()+" HAS WON THE GAME!");
        board.getCurrentPlayer().getPlayerBalance();
        return board.getCurrentPlayer();
    }*/

}
