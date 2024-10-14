package blackjack;
/**
 *
 * @author daniel
 */
import java.util.*;

public class Hand
{
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) // add card to the list of cards
    {
        cards.add(card);
    }

    public int getValue() // get the value of each indivudual card.
    {
        int value = 0;
        int aceCount = 0;

        for (Card card : cards) {
            value += card.getValue();
            if (card.getRank().equals("A")) {
                aceCount++;
            }
        }
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }
        return value;
    }
    public void clear()//clear the cards from the list
    {
        cards.clear();
    }

    public Card getCard(int index)//get cards at an index.
    {
        return cards.get(index);
    }

    @Override
    public String toString() {
        return cards.toString();
    }
    
     public List<Card> getCards() { // return the list of cards
        return new ArrayList<>(cards); // Return a copy of the list to avoid external modifications
    }
}
