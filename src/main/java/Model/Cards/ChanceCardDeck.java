package Model.Cards;

import java.util.Random;

/**
 * Provides methods for chance card deck and holds chance cards.
 */
public class ChanceCardDeck {
    private final ChanceCard[] deck;
    public ChanceCardDeck() {
        // Initialize Model.ChanceCard Array
        deck = new ChanceCard[35];
        //All PayCards
        deck[0] = new PayCard("Betal 200 kroner for levering af 2 kasser øl.", "pay", 200);
        deck[1] = new PayCard("De har kørt frem for 'fuldt stop', " +
                "betal 1000 kroner i bøde.", "pay", 1000);
        deck[2] = new PayCard("Betal for vognvask og smøring 300 kroner.", "pay", 300);
        deck[3] = new PayCard("Betal 3000 kroner for reparation af deres vogn.", "pay", 3000);
        deck[4] = new PayCard("Betal 3000 kroner for reparation af deres vogn.", "pay", 3000);
        deck[5] = new PayCard("De har købt 4 nye dæk til Deres vogn, betal 1000 kroner.", "pay", 1000);
        deck[6] = new PayCard("De har fået en parkeringsbøde, betal 200 kroner i bøde.", "pay", 200);
        deck[7] = new PayCard("Betal deres bilforsikring, 1000 kroner.", "pay", 1000);
        deck[8] = new PayCard("De har været udenlands og købt for mange smøger, betal 200 kroner i told.", "pay", 200);
        deck[9] = new PayCard("Tandlægeregning, betal 2000 kroner.", "pay", 2000);

        //All ReceiveCards
        deck[10] = new ReceiveCard("De har vundet i klasselotteriet. Modtag 500 kroner.",  "receive", 500);
        deck[11] = new ReceiveCard("De har vundet i klasselotteriet. Modtag 500 kroner.",  "receive", 500);
        deck[12] = new ReceiveCard("De modtager Deres aktieudbytte. Modtag 1000 kroner af banken.",  "receive", 1000);
        deck[13] = new ReceiveCard("De modtager Deres aktieudbytte. Modtag 1000 kroner af banken.",  "receive", 1000);
        deck[14] = new ReceiveCard("De modtager Deres aktieudbytte. Modtag 1000 kroner af banken.",  "receive", 1000);
        deck[15] = new ReceiveCard("Kommunen har eftergivet et kvartals skat. Hæv i banken 3000 kroner.",  "receive", 3000);
        deck[16] = new ReceiveCard("De have en række med elleve rigtige i tipning, modtag 1000 kroner.",  "receive", 1000);
        deck[17] = new ReceiveCard("Grundet dyrtiden har De fået gageforhøjelse, modtag 1000 kroner.",  "receive", 1000);
        deck[18] = new ReceiveCard("Deres præmieobligation er udtrykket. De modtager 1000 kroner af banken.",  "receive", 1000);
        deck[19] = new ReceiveCard("Deres præmieobligation er udtrykket. De modtager 1000 kroner af banken.",  "receive", 1000);
        deck[20] = new ReceiveCard("De har solgt nogle gamle møbler på auktion. Modtag 1000 kroner af banken.",  "receive", 1000);
        deck[21] = new ReceiveCard("Værdien af egen avl fra nyttehaven udgør 200 kroner" +
                " som de modtager af banken",  "receive", 200);

        //Receive legation
        deck[22] = new ReceiveCard("De modtager “Matador-legatet” på kr 40.000, men kun hvis værdier ikke overstiger " +
                "15.000 kroner.",  "legation", 40000);

        //Receive money from other players
        deck[23] = new ReceiveCard("Det er deres fødselsdag. Modtag af hver medspiller 200 kr.",  "receiveplayers", 200);

        //All MoveCards
        deck[24] = new MoveCard("Ryk frem til START.", "set", 0);
        deck[25] = new MoveCard("Ryk frem til START.", "set", 0);

        deck[26] = new MoveCard("Ryk tre felter frem.", "move", 3);
        deck[27] = new MoveCard("Ryk tre felter tilbage.", "move", -3);
        deck[28] = new MoveCard("Ryk tre felter tilbage.", "move", -3);

        deck[29] = new MoveCard("Ryk frem til Frederiksberg Allé.", "set", 11);

        //All move and receive cards
        deck[30] = new MoveCard("Ryk frem til Frederiksberg Allé. Hvis De passere START, indkasser da 4000 kroner.", "set", 11);

        deck[31] = new MoveCard("Tag med Mols-Linien, flyt brikken frem og hvis De passerer START indkassér da " +
                "4000 kroner.", "set", 15);

        deck[32] = new MoveCard("Ryk frem til Grønningen, hvis De passerer start indkasser da 4000 kroner.", "set", 24);
        deck[33] = new MoveCard("Ryk frem til Vimmelskaftet, hvis de passerer start indkasser da 4000 kroner", "set", 32);

        //All JailFreeCards
        deck[34] = new JailFreeCard(" anledning af kongens fødselsdag benådes De herved for fængsel. Dette kort kan \n" +
                "opbevares indtil De får brug for det.", "jail", 30);
    }

    /**
     * Shuffles card deck randomly
     */
    public void shuffleCard() {
        Random r = new Random();

        for (int i = 0; i < deck.length; i++) {
            // produces a uniformly distrubted int
            int indexSwap = r.nextInt(deck.length);

            ChanceCard temp = deck[indexSwap];
            deck[indexSwap] = deck[i];
            deck[i] = temp;
        }
    }

    /**
     * Draw card from top of deck, and place it at the bottom.
     * @return a Chancecard from top of deck
     */
    public ChanceCard drawCard() {
        // Method that draws and returns the top card in the deck and afterwards places the card in the bottom of the deck
        ChanceCard card;
        // Take the card on top
        card = deck[0];

        for(int i = 0; i < deck.length; i++) {
            // Shift the index of cards one up.
            if(i > 0) {
                deck[i - 1] = deck[i];
            }
            // For index = 0 - Put the card on top
            deck[i] = card;
        }
        return card;
    }
}
