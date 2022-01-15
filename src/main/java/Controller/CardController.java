package Controller;

import Model.Cards.ChanceCard;
import Model.Cards.ChanceCardDeck;
import Model.Player;

public class CardController {

    private final ChanceCardDeck deck;
    private final GUIController gui;

    public CardController(GUIController gui) {
        this.gui = gui;
        deck = new ChanceCardDeck();
        deck.shuffleCard();
    }

    public void doCardAction(Player player, Player[] players) {
        ChanceCard card = deck.drawCard();

        switch (card.getClass().getSimpleName()) {
            case "MoveCard":
                move(card, player);
                break;
            case "PayCard":
                pay(card, player);
                break;
            case "ReceiveCard":
                receive(card, player, players);
                break;
            case "JailFreeCard":
                player.setHasJailFreeCard(true);
                break;

        }
        gui.setChanceCard(card);
        gui.message(player.getName() + " trak prøv-lykken kortet: " + card.getDescription());
    }

    public void movePlayer(Player player, int amount) {
        int prePlacement = player.getPlacement();
        int endPlacement = player.getPlacement() + amount;
        int newPlacement;

        // Check for a complete lap around on board. Then recalibrate player placement
        if (endPlacement >= gui.getGuiFields().length) {
            newPlacement = endPlacement - gui.getGuiFields().length;

            //Pay lap bonus
            player.setPlayerBalance(4000);

        } else if (endPlacement < 0) {
            newPlacement = endPlacement + 40;
        } else {
            newPlacement = endPlacement;
        }
        player.setPlacement(newPlacement);

        //Update GUI
        gui.movePlayer(player, newPlacement, prePlacement);
    }

    public void setPlayerPlacement(Player player, int endPlacement, boolean passStart) {
        int preplacement = player.getPlacement();
        int newPlacement;

        //Check if Starts is passed
        if (passStart && endPlacement < preplacement) {
            player.setPlayerBalance(4000);
        }

        // Check for a complete lap around on board. Then recalibrate player placement
        if (endPlacement >= gui.getGuiFields().length) {
            newPlacement = endPlacement - gui.getGuiFields().length;

            //Pay lap bonus
            player.setPlayerBalance(4000);

        } else {
            newPlacement = endPlacement;
        }
        player.setPlacement(newPlacement);
        gui.movePlayer(player, newPlacement, preplacement);
    }

    private void move(ChanceCard card, Player player) {
        int move = card.getValue();
        int from = player.getPlacement();
        int to = from + move;
        if (to < 0) {
            to = from + 40 + move;
        }
        String type = card.getType();
        switch (type) {
            case "set":
                setPlayerPlacement(player, card.getValue(), true);
                break;
            case "move":
                movePlayer(player, card.getValue());
                break;
            case "jail":
                setPlayerPlacement(player, card.getValue(), false);
                break;
        }
    }
    private void pay(ChanceCard card, Player player) {
        int amount = card.getValue();
        player.setPlayerBalance(-amount);
    }
    private void receive(ChanceCard card, Player player, Player[] players) {
        int amount = card.getValue();

        if (card.getType().equals("legation")) {
            if (player.getNetWorth() < 15000) {
                player.setPlayerBalance(40000);
            }
        } else if (card.getType().equals("receiveplayers")) {
            for (Player value : players) {
                if (value != player) {
                    value.setPlayerBalance(-amount);
                }
            }
            int newAmount = amount * (players.length - 1);
            player.setPlayerBalance(newAmount);
        } else {
            player.setPlayerBalance(amount);
        }
    }
}
