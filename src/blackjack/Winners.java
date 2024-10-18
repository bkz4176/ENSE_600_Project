package blackjack;

/**
 *
 * @author daniel
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Winners {

    public static Map<ActualPlayer, String> determineWinner(Dealer dealer, List<ActualPlayer> players)// Method do determine the winners from the from the current game
    //and to add the winnings to the players bank account.
    // this method also stores the results "Winner, Loser, Draw in a Map with the player.
    {
        int dealerValue = dealer.getHandValue();
        Map<ActualPlayer, String> outcomes = new HashMap<>();

        for(int i = 0; i < players.size(); i++)
        {
            ActualPlayer p = players.get(i);
            int bet = p.getBetAmount();        
            
            if (dealerValue > 21 && p.getHandValue()<= 21)
            {
                p.addWinnings(bet * 2);
                outcomes.put(p, "Winner");
                p.incrementGamesWon();
                p.setTotalWinnings(bet);
                
            }
            else if(p.getHandValue() > 21)
            {
                outcomes.put(p, "Loser");
                p.incrementGamesLost();
                p.setTotalLoss(bet);
            }
            else if(dealerValue <= 21 && p.getHandValue()<=21 && p.getHandValue()>dealerValue)
            {
                p.addWinnings(bet * 2);
                outcomes.put(p, "Winner");
                p.incrementGamesWon();
                p.setTotalWinnings(bet);
                
            }
            else if(p.getHandValue() == dealerValue && p.getHandValue() <= 21) // draw
            {
                p.addWinnings(bet);
                outcomes.put(p, "Draw");
                p.incrementGamesDrawn();
            }
            else
            {
                
                outcomes.put(p, "Loser");
                p.incrementGamesLost();
                p.setTotalLoss(bet);
                
            }
            System.out.println(" || New bank total = $" + p.getBalance());
            p.incrementGamesPlayed();
            p.setTotalEarnings();           
        }
            return outcomes;
    }
}

