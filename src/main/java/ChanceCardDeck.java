import gui_main.GUI;
import java.lang.Math;
import java.util.Random;

public class ChanceCardDeck {
    private ChanceCard[] deck;
    //private final int MAX_VALUE = 3;
    //private int position = 0;
    //protected Player player;
    private Board board;

    public ChanceCardDeck(Board owner) {
        deck = new ChanceCard[3];
        deck[0] = new ChanceCard("Du har spist for meget slik. Betal $2 til banken", "Chancekort 1", 0);
        deck[1] = new ChanceCard("Ryk frem til START. Modtag $2 fra banken.", "Chancekort 2", 1);
        deck[2] = new ChanceCard("Du har lavet alle dine lektier. Modtag $2 fra banken", "Chancekort 3", 2);
        this.board = owner;
        //board.getCurrentPlayer().setPlayerBalance(-200);
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
    public void useChanceCard() {
        ChanceCard card = drawCard();
        switch(card.getNumber()) {
            case 0:
                //Du har spist for meget slik. Betal $2 til banken.
                board.getCurrentPlayer().setPlayerBalance(-2);
                System.out.println("Du har spist for meget Slik");
                break;
            case 1:
                //Ryk frem til START. Modtag $2 fra banken
                board.removePlayer(board.getCurrentPlayerVar(), board.getCurrentPlayer().getPlacement());
                board.getCurrentPlayer().setPlacement(-board.getCurrentPlayer().getPlacement());
                board.movePlayer(board.getCurrentPlayerVar(), board.getCurrentPlayer().getPlacement());
                System.out.println("Ryk til Start");
                break;
            case 2:
                //Du har lavet alle dine lektier. Modtag $2 fra banken
                board.getCurrentPlayer().setPlayerBalance(2);
                System.out.println("Du har lavet alle dine lektier +2");
                break;
        }
    }

}
