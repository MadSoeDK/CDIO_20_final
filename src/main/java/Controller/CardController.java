package Controller;

import Model.Cards.ChanceCard;
import Model.Cards.ChanceCardDeck;
import Model.Player;
import gui_main.GUI;

public class CardController {

    private ChanceCardDeck deck;
    private GUIController gui;

    public CardController(GUIController gui) {
        this.gui = gui;
        deck = new ChanceCardDeck();
        //deck.shuffleCard();
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
                recieve(card, player, players);
                break;
            case "JailFreeCard":
                player.setHasJailFreeCard(true);
                break;

        }
        gui.setChanceCard(card);
        gui.message(player.getName() + " trak prøv-lykken kortet: " + card.getDescription());
    }

    public void moveplayer(Player player, int amount) {
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
        //setPlayerPlacement(player, to, true);
        String type = card.getType();
        if (type.equals("set")) {
            setPlayerPlacement(player, card.getValue(), true);
        } else if (type.equals("move")) {
            moveplayer(player, card.getValue());
        } else if (type.equals("jail")) {
            setPlayerPlacement(player, card.getValue(), false);
        }
    }
    private void pay(ChanceCard card, Player player) {
        int amount = card.getValue();
        player.setPlayerBalance(-amount);
    }
    private void recieve(ChanceCard card, Player player, Player[] players) {
        int amount = card.getValue();

        if (card.getType().equals("legation")) {
            if (player.getNetWorth() < 15000) {
                player.setPlayerBalance(40000);
            }
        } else if (card.getType().equals("receiveplayers")) {
            for(int i = 0; i < players.length; i++) {
                if(players[i] != player) {
                    players[i].setPlayerBalance(-amount);
                }
            }
            int newAmount = amount * (players.length - 1);
            player.setPlayerBalance(newAmount);
        } else {
            player.setPlayerBalance(amount);
        }
    }
}
