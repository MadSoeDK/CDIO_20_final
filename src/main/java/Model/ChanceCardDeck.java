package Model;

import java.util.Random;

/**
 * Initializes ChanceCards, Controls Model.ChanceCard Effects.
 */
public class ChanceCardDeck {
    private ChanceCard[] deck;
    private Board board;

    public ChanceCardDeck(Board owner) {
        // Initialize Model.ChanceCard Array
        deck = new ChanceCard[4];
        deck[0] = new ChanceCard("Betal 200kr for levering af 2 kasser øl", "Chancekort 1", 0, "pay");
        deck[1] = new ChanceCard("Værdien af egen avl fra nyttehaven udgør 200 som de modtager af banken", "Chancekort 2", 1, "receive");
        deck[2] = new ChanceCard("Ryk frem til START", "Chancekort 3", 2,"move");
        deck[3] = new ChanceCard("I anledning af kongens fødselsdag benådes De herved for fængsel. Dette kort kan \n" +
                "opbevares indtil De får brug for det.", "Chancekort 4", 3,"jail");
        this.board = owner;
        shuffleCard();
    }
    public void shuffleCard() {
        //Method that shuffles cards.
        Random r = new Random();

        //Shuffles cards.
        for (int i = 0; i < deck.length; i++) {
            int indexSwap = r.nextInt(deck.length);

            ChanceCard temp = deck[indexSwap];
            deck[indexSwap] = deck[i];
            deck[i] = temp;
        }
    }
    public ChanceCard drawCard() {
        // Method that draws and returns the top card in the deck and afterwards places the card in the bottom of the deck
        ChanceCard card;
        card = deck[0];

        for(int i = 0; i < deck.length; i++) {
            if(i > 0) {
                deck[i - 1] = deck[i];
            }
            deck[i] = card;
        }
        return card;
    }
    /*public void useChanceCard() {
        ChanceCard card = drawCard();
        switch(card.getNumber()) {
            case 0:
                //Du har spist for meget slik. Betal $2 til banken.
                board.guiMessage("Du Har Spist for meget slik betal $2");
                board.getCurrentPlayer().setPlayerBalance(-2);
                break;
            case 1:
                //Ryk frem til START. Modtag $2 fra banken
                board.guiMessage("Ryk tilbage til start, modtag penge");
                board.removePlayer(board.getCurrentPlayerVar(), board.getCurrentPlayer().getPlacement());
                board.getCurrentPlayer().gotoPlacement(0);
                board.movePlayer(board.getCurrentPlayerVar(), board.getCurrentPlayer().getPlacement());
                break;
            case 2:
                //Du har lavet alle dine lektier. Modtag $2 fra banken
                board.guiMessage("Du Har lavet alle dine Lektier modtag $2");
                board.getCurrentPlayer().setPlayerBalance(2);
                break;
            case 3:
                //Ryk frem til strandpromenaden
                board.guiMessage("Ryk frem til strandpromenaden");
                board.removePlayer(board.getCurrentPlayerVar(), board.getCurrentPlayer().getPlacement());
                //board.getCurrentPlayer().setPlacement(-board.getCurrentPlayer().getPlacement());
                board.getCurrentPlayer().gotoPlacement(23);
                board.movePlayer(board.getCurrentPlayerVar(), board.getCurrentPlayer().getPlacement());
                break;
            case 4:
                //Du har Fødselsdag
                board.guiMessage("Det er din fødselsdag, Modtag $1 fra alle spiller");
                board.getCurrentPlayer().setPlayerBalance(board.amountofPlayers()+1);
                for (int i=0; i<board.amountofPlayers()-1; i++) {
                    board.getPlayer(i).setPlayerBalance(-1);
                    board.getPlayer(i).getPlayer().setBalance(board.getPlayer(i).getPlayerBalance());
                break;
                }
        }
    }*/

}
