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
    {
        String space = " ";
        String star = " * ";
        int dealerValue = dealer.getHandValue();
        System.out.println("Dealer's hand value: " + dealerValue);
        String width = space.repeat(11)+"* ";
        //String[] messages = new String[players.size()];
        Map<ActualPlayer, String> outcomes = new HashMap<>();

        for(int i = 0; i < players.size(); i++)
        {
            ActualPlayer p = players.get(i);
            int bet = p.getBetAmount();
            String message;            
            
            if (dealerValue > 21 && p.getHandValue()<= 21)
            {
                p.addWinnings(bet * 2);
                System.out.print(width+p.getName() + " wins with: " + p.getHandValue());
                outcomes.put(p, "Winner");
                p.incrementGamesWon();
                p.setTotalWinnings(bet);
                
            }
            else if(p.getHandValue() > 21)
            {
                System.out.print(width+dealer.getName() + " wins against " + p.getName());
                message = dealer.getName() + " wins against " + p.getName();
                outcomes.put(p, "Loser");
                p.incrementGamesLost();
                p.setTotalLoss(bet);
            }
            else if(dealerValue <= 21 && p.getHandValue()<=21 && p.getHandValue()>dealerValue)
            {
                p.addWinnings(bet * 2);
                System.out.print(width+p.getName() + " wins with: " + p.getHandValue());
                outcomes.put(p, "Winner");
                p.incrementGamesWon();
                p.setTotalWinnings(bet);
                
            }
            else if(p.getHandValue() == dealerValue && p.getHandValue() <= 21) // draw
            {
                p.addWinnings(bet);
                System.out.print(width+"Push (tie) with " + p.getName());
                message = p.getName() + " draws: Returning bet amount";
                outcomes.put(p, "Draw");
                p.incrementGamesDrawn();
            }
            else
            {
                
                System.out.print(width+dealer.getName() + " wins against " + p.getName());
                message = dealer.getName() + " wins against " + p.getName();
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

