/*import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChanceCardTest {
    ChanceCard[] deck = {
    new ChanceCard("Du har spist for meget slik. Betal $2 til banken", "Chancekort 1", 0),
    new ChanceCard("Ryk frem til START. Modtag $2 fra banken.", "Chancekort 2", 1),
    new ChanceCard("Du har lavet alle dine lektier. Modtag $2 fra banken", "Chancekort 3", 2),
    new ChanceCard("Ryk frem til Strandpromenaden", "Chancekort 4", 3),
    new ChanceCard("Du har fødselsdag, Modtag $1 fra hver spiller", "Chancekort 5", 4),
    };

    @Test
    void drawCard() {

        ChanceCard firstCard = deck[0];

        for(int i = 0; i < deck.length; i++) {
            if(i > 0) {
                deck[i - 1] = deck[i];
            }
            deck[i] = firstCard;
        }
        assertTrue(deck[4] == firstCard);
    }
}
 */