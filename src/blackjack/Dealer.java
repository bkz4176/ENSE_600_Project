package blackjack;
/**
 *
 * @author daniel
 */
import java.util.List;

public class Dealer extends Player
{
    public Dealer()
    {
        super("Dealer");
    }
    String space = " " ;
    String width = space.repeat(11)+"* ";
    int totalWidth = 50;
    int nameLength = name.length();
    int paddingBefore = (totalWidth - 12 - nameLength) / 2;
    int paddingAfter = totalWidth - 12 - nameLength - paddingBefore;

    @Override
    public void play(Deck deck) // Method for the dealer to play. 
    {
        while (getHandValue() < 17)
        {
            addCardToHand(deck.drawCard());
        }
    }

    public void dealerInitialCard() // method to display the face up card of the Dealer.
    {
        System.out.print("\n"+width);
        System.out.println(space.repeat(paddingBefore)+"Dealers' Face up Card!"+space.repeat(paddingAfter));
        System.out.println(width+getHand() + " (value: " + getHandValue() + ")");
        System.out.println(width);
    }

    public void checkAndPlay(List<ActualPlayer> players, Deck deck) // If all playes have busted  then the dealer wins
    //and will not draw another card. else if at least one player has not busted then the play method will be called.
    {
        boolean notBusted = false;
        // Dealer's turn if player hasn't busted
        for(ActualPlayer p : players)
        {
            if(p.getHandValue()<=21)
            {
                notBusted = true;
            }
        }

        if (notBusted) {
            play(deck);
            System.out.print(width+"\n");
            System.out.println(width+space.repeat(paddingBefore)+"Dealers' Hand!"+space.repeat(paddingAfter));
            System.out.println(width+getHand() + " (value: " + getHandValue() + ")");
            if(getHandValue()>21)
            {
                System.out.println(width+name + " Busts!");
            }
            System.out.println(width);
        }
    }
        public void clearHand() // method to clear the dealer hand
        {
            hand.clear();  
        }
}

