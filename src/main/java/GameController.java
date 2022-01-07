import Model.*;
import gui_fields.GUI_Ownable;

public class GameController {

    private Board board;
    private Cup cup;
    private GUIController gui;
    private Player[] players;
    private Player currentPlayer;


    // Initializing Variables
    int same_color_owner = 0;
    int rent_mutiplier = 1;
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

        gui.Button("Nu er det" + currentPlayer.getName() + "'s tur, rul terningen!", "Rul terning");

        // Roll die, get value, show die
        cup.roll();
        sum = cup.getSum();
        gui.showDie(sum);

        // Move player placement - automatically updates GUI
        moveplayer(currentPlayer, sum);

        int placement = currentPlayer.getPlacement();

        Field field = board.getField(placement);

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
        // Check field type
        if (field instanceof Property) {

            // Typecast to Property
            Property property = (Property) field;

            // Field is owned by another player and it's not the current player
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

                // Reset
                same_color_owner = 0;
            }

            // No one owns the Model.Property
            if (property.getOwner() == null) {

                // Subtract player balance from Model.Property rent
                currentPlayer.setPlayerBalance(-property.getRent());

                // Set Property owner
                property.setOwner(currentPlayer);

                // Set GUI Field
                GUI_Ownable ownable = (GUI_Ownable) gui.getGuiField(placement);
                ownable.setOwnerName(currentPlayer.getName());
                ownable.setBorder(gui.getPlayerColor(currentPlayer));
            }
        }

        if (field instanceof Jail) {

            // Typecast to Model.Property
            Jail jail = (Jail) field;

            // Subtract player balance from Property rent
            currentPlayer.setPlayerBalance(-jail.getRent());

            // Add money to Free Parking if landed on "Go To Model.Jail"
            if (placement == 18) {
                FreeParking.setBalance(3);
            }

            // Set GUI Balance
            gui.getGuiField(placement).setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));

            // Move to Model.Jail field
            currentPlayer.gotoPlacement(6);
            placement = currentPlayer.getPlacement();

            moveplayer(currentPlayer, placement);

        }

        if (field instanceof FreeParking) {

            // Give money to player
            currentPlayer.setPlayerBalance(FreeParking.getBalance());

            // Reset Free Parking
            FreeParking.resetBalance();

            // Set GUI Balance
            gui.getGuiField(placement).setSubText("Modtag: " + String.valueOf(FreeParking.getBalance()));

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

    /*public Player checkWinner() {
        gui.guiMessage(board.getPlayer(board.getWinner()).getName()+" HAS WON THE GAME!");
        board.getCurrentPlayer().getPlayerBalance();
        return board.getCurrentPlayer();
    }*/

}
