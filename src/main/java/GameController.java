import Model.*;
import gui_fields.GUI_Ownable;

public class GameController {

    private Board board;
    private Cup cup;
    private GUIController gui;
    private Player[] players;
    private Player currentPlayer;


    // Initializing Variables
    int sum = 0;
    //int currentPlayer = 0;
    int same_color_owner = 0;
    int rent_mutiplier = 1;

    public GameController() {
        board = new Board();
        cup = new Cup();
        gui = new GUIController(board.getFields());
        gui.createPlayers();
        setupPlayers(gui.getPlayernames());
        playGame();
    }

    public void playGame() {
        do {
            takeTurn();
        } while (true);
    }

    public void takeTurn() {

        // Get Player pre-turn information
        //Player player = board.getPlayer(currentPlayer);
        int from = currentPlayer.getPlacement();

        // Roll die, get value, show die
        gui.Button("Nu er det" + currentPlayer + "'s tur, rul terningen!", "Rul terning");
        cup.roll();
        sum = cup.getSum();
        gui.showDie(sum);

        // Move player position
        moveplayer(from, currentPlayer);

        int placement = currentPlayer.getPlacement();

        // Get new position and Update GUI
        gui.movePlayer(currentPlayer, from, placement);

        Field field = board.getField(placement);

        checkFieldType(field, placement);

        nextTurn();

        //checkWinner();

    }

    public void nextTurn() {
        // Chance Player Turn/Reset to first player
        int playerindex = java.util.Arrays.asList(players).indexOf(currentPlayer);

        if (playerindex == players.length-1) {
            currentPlayer = players[0];
        } else {
            currentPlayer = players[playerindex + 1];
        }
        //board.updateCurrentPlayer(currentPlayer);
    }

    public void checkFieldType(Field field, int placement) {
        // Check field type
        if (field instanceof Property) {

            // Typecast to Model.Property
            Property property = (Property) field;

            // Model.Field is owned by another player and it's not the current player
            if (property.getOwner() != currentPlayer && property.getOwner() != null) {

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
                currentPlayer.setPlayerBalance(-property.getRent() * rent_mutiplier);
                fieldOwner.setPlayerBalance(property.getRent() * rent_mutiplier);

                //Update GUI balance from currentplayer and field owner
                gui.setguiPlayerBalance(currentPlayer, currentPlayer.getPlayerBalance());
                gui.setguiPlayerBalance(fieldOwner, fieldOwner.getPlayerBalance());

                //gui.getGuiPlayer(currentPlayer).setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                //board.getPlayer(currentPlayer).getPlayer().setBalance(board.getPlayer(currentPlayer).getPlayerBalance());
                //fieldOwner.getPlayer().setBalance(fieldOwner.getPlayerBalance());

                // Reset
                same_color_owner = 0;
            }

            // No one owns the Model.Property
            if (property.getOwner() == null) {

                // Subtract player balance from Model.Property rent
                currentPlayer.setPlayerBalance(-property.getRent());

                // Update GUI balance
                gui.setguiPlayerBalance(currentPlayer, currentPlayer.getPlayerBalance());
                //gui.getGuiPlayer(currentPlayer).setBalance(currentPlayer.getPlayerBalance());

                // Set Property owner
                property.setOwner(currentPlayer);

                // Set GUI Field
                GUI_Ownable ownable = (GUI_Ownable) gui.getGuiField(placement);//board.getField(placement).getGUIField();
                ownable.setOwnerName(currentPlayer.getName());
                ownable.setBorder(gui.getPlayerColor(currentPlayer));
            }
        }

        if (field instanceof Jail) {

            // Typecast to Model.Property
            Jail jail = (Jail) field;

            // Subtract player balance from Model.Property rent. 2. Update GUI
            currentPlayer.setPlayerBalance(-jail.getRent());
            gui.getGuiPlayer(currentPlayer).setBalance(currentPlayer.getPlayerBalance());

            // Add money to Free Parking if landed on "Go To Model.Jail"
            if (placement == 18) {
                FreeParking.setBalance(3);
            }

            // Set GUI Balance
            gui.getGuiField(placement).setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));

            // Move to Model.Jail field
            currentPlayer.gotoPlacement(6);
            placement = currentPlayer.getPlacement();

            // Update GUI with new placement
            gui.movePlayer(currentPlayer, 18, placement);
            //board.movePlayer(currentPlayer, placement);
            //board.removePlayer(currentPlayer, 18);
        }

        if (field instanceof FreeParking) {

            // Give money to player
            currentPlayer.setPlayerBalance(FreeParking.getBalance());
            //gui.getGuiPlayer(currentPlayer).setBalance(player.getPlayerBalance());
            // Update GUI
            gui.setguiPlayerBalance(currentPlayer, currentPlayer.getPlayerBalance());

            // Reset Free Parking
            FreeParking.resetBalance();

            // Set GUI Balance
            gui.getGuiField(placement).setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));

        }
    }

    public void setupPlayers(String[] playerNames) {
        final int STARTBALANCE = 30000;
        players = new Player[playerNames.length];

        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i],STARTBALANCE);
        }

        currentPlayer = players[0];
    }
    public void moveplayer(int from, Player player) {
        // Remove player from board
        //board.removePlayer(currentPlayer, placement);

        // Check for a complete lap around on board. Then recalibrate player placement
        if(from + sum >= 40) {
            player.setPlacement(sum - 40);
            //board.removePlayer(currentPlayer, from);
            gui.movePlayer(player, from, player.getPlacement());
            sum = 0;

            // Pass Model.Start field and gain $2
            player.setPlayerBalance(2);
        }

        // Set new placement incremented by dice. Then get the new placement.
        player.setPlacement(sum);
        //placement = player.getPlacement();

        // Update GUI with new placement
        //board.movePlayer(currentPlayer, placement);
    }

    /*public Player checkWinner() {
        gui.guiMessage(board.getPlayer(board.getWinner()).getName()+" HAS WON THE GAME!");
        board.getCurrentPlayer().getPlayerBalance();
        return board.getCurrentPlayer();
    }*/

}
