package Model.Cards;

import java.util.Random;

/**
 * Initializes ChanceCards, Controls Model.ChanceCard Effects.
 */
public class ChanceCardDeck {
    private ChanceCard[] deck;
    public ChanceCardDeck() {
        // Initialize Model.ChanceCard Array
        deck = new ChanceCard[35];
        //All PayCards
        deck[0] = new PayCard("Betal 200 kroner for levering af 2 kasser øl.", "paycard 1", "pay", 200);
        deck[1] = new PayCard("De har kørt frem for 'fuldt stop', " +
                "betal 1000 kroner i bøde.", "paycard 2", "pay", 1000);
        deck[2] = new PayCard("Betal for vognvask og smøring 300 kroner.", "paycard 3", "pay", 300);
        deck[3] = new PayCard("Betal 3000 kroner for reparation af deres vogn.", "paycard 4", "pay", 3000);
        deck[4] = new PayCard("Betal 3000 kroner for reparation af deres vogn.", "paycard 5", "pay", 3000);
        deck[5] = new PayCard("De har købt 4 nye dæk til Deres vogn, betal 1000 kroner.", "paycard 6", "pay", 1000);
        deck[6] = new PayCard("De har fået en parkeringsbøde, betal 200 kroner i bøde.", "paycard 7", "pay", 200);
        deck[7] = new PayCard("Betal deres bilforsikring, 1000 kroner.", "paycard 8", "pay", 1000);
        deck[8] = new PayCard("De har været udenlands og købt for mange smøger, betal 200 kroner i told.", "paycard 9", "pay", 200);
        deck[9] = new PayCard("Tandlægeregning, betal 2000 kroner.", "paycard 10", "pay", 2000);

        //All ReceiveCards
        deck[10] = new ReceiveCard("De har vundet i klasselotteriet. Modtag 500 kroner.", "receivecard 2",  "receive", 500);
        deck[11] = new ReceiveCard("De har vundet i klasselotteriet. Modtag 500 kroner.", "receivecard 3",  "receive", 500);
        deck[12] = new ReceiveCard("De modtager Deres aktieudbytte. Modtag 1000 kroner af banken.", "receivecard 4",  "receive", 1000);
        deck[13] = new ReceiveCard("De modtager Deres aktieudbytte. Modtag 1000 kroner af banken.", "receivecard 5",  "receive", 1000);
        deck[14] = new ReceiveCard("De modtager Deres aktieudbytte. Modtag 1000 kroner af banken.", "receivecard 6",  "receive", 1000);
        deck[15] = new ReceiveCard("Kommunen har eftergivet et kvartals skat. Hæv i banken 3000 kroner.", "receivecard 7",  "receive", 3000);
        deck[16] = new ReceiveCard("De have en række med elleve rigtige i tipning, modtag 1000 kroner.", "receivecard 8",  "receive", 1000);
        deck[17] = new ReceiveCard("Grundet dyrtiden har De fået gageforhøjelse, modtag 1000 kroner.", "receivecard 9",  "receive", 1000);
        deck[18] = new ReceiveCard("Deres præmieobligation er udtrykket. De modtager 1000 kroner af banken.", "receivecard 10",  "receive", 1000);
        deck[19] = new ReceiveCard("Deres præmieobligation er udtrykket. De modtager 1000 kroner af banken.", "receivecard 11",  "receive", 1000);
        deck[20] = new ReceiveCard("De har solgt nogle gamle møbler på auktion. Modtag 1000 kroner af banken.", "receivecard 12",  "receive", 1000);
        deck[21] = new ReceiveCard("Værdien af egen avl fra nyttehaven udgør 200 kroner" +
                " som de modtager af banken", "receivecard 13",  "receive", 200);

        //Receive legation
        deck[22] = new ReceiveCard("De modtager “Matador-legatet” på kr 40.000, men kun hvis værdier ikke overstiger " +
                "15.000 kroner.", "receivecard 14",  "legation", 40000);

        //Receive money from other players
        deck[23] = new ReceiveCard("Det er deres fødselsdag. Modtag af hver medspiller 200 kr.", "receiveplayers",  "receiveplayers", 200);

        //All MoveCards
        deck[24] = new MoveCard("Ryk frem til START.", "movecard 1", "set", 0);
        deck[25] = new MoveCard("Ryk frem til START.", "movecard 2", "set", 0);

        deck[26] = new MoveCard("Ryk tre felter frem.", "movecard 3", "move", 3);
        deck[27] = new MoveCard("Ryk tre felter tilbage.", "movecard 4", "move", -3);
        deck[28] = new MoveCard("Ryk tre felter tilbage.", "movecard 5", "move", -3);

        deck[29] = new MoveCard("Ryk frem til Frederiksberg Allé.", "movecard 6", "set", 11);

        //All move and receive cards
        deck[30] = new MoveCard("Ryk frem til Frederiksberg Allé. Hvis De passere START, indkasser da 4000 kroner.",
                "movecard 7", "set", 11);

        deck[31] = new MoveCard("Tag med Mols-Linien, flyt brikken frem og hvis De passerer START indkassér da " +
                "4000 kroner.", "movecard 8", "set", 15);

        deck[32] = new MoveCard("Ryk frem til Grønningen, hvis De passerer start indkasser da 4000 kroner.", "movecard 9", "set", 24);
        deck[33] = new MoveCard("Ryk frem til Vimmelskaftet, hvis de passerer start indkasser da 4000 kroner", "movecard 10", "set", 32);

        //All JailFreeCards
        deck[34] = new JailFreeCard(" anledning af kongens fødselsdag benådes De herved for fængsel. Dette kort kan \n" +
                "opbevares indtil De får brug for det.", "Jailfreecard 1", "jail", 30);
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
        card = deck[23];

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
