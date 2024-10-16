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
                //DataFile.log(p.getName() + " wins with: " + p.getHandValue()+" || New bank total = $" + p.getBalance());
                //message = p.getName() + " wins: Adding winnings to bank Balance";
                outcomes.put(p, "Winner");
                
            }
            else if(p.getHandValue() > 21)
            {
                System.out.print(width+dealer.getName() + " wins against " + p.getName());
                //DataFile.log(dealer.getName() + " wins against " + p.getName()+" || New bank total = $" + p.getBalance());
                message = dealer.getName() + " wins against " + p.getName();
                outcomes.put(p, "Loser");
            }
            else if(dealerValue <= 21 && p.getHandValue()<=21 && p.getHandValue()>dealerValue)
            {
                p.addWinnings(bet * 2);
                System.out.print(width+p.getName() + " wins with: " + p.getHandValue());
                //DataFile.log(p.getName() + " wins with: " + p.getHandValue()+" || New bank total = $" + p.getBalance());
                //message = p.getName() + " wins: Adding winnings to bank Balance";
                outcomes.put(p, "Winner");
                
            }
            else if(p.getHandValue() == dealerValue && p.getHandValue() <= 21)
            {
                p.addWinnings(bet);
                System.out.print(width+"Push (tie) with " + p.getName());
                //DataFile.log("\n"+"Push (tie) with " + p.getName()+" || New bank total = $" + p.getBalance());
                message = p.getName() + " draws: Returning bet amount";
                outcomes.put(p, "Draw");
            }
            else
            {
                
                System.out.print(width+dealer.getName() + " wins against " + p.getName());
                //DataFile.log(dealer.getName() + " wins against " + p.getName()+ " || New bank total = $" + p.getBalance());
                message = dealer.getName() + " wins against " + p.getName();
                outcomes.put(p, "Loser");
                
            }
            //messages[i] = message;
            System.out.println(" || New bank total = $" + p.getBalance());
            
        }
            return outcomes;
            //System.out.println("\n"+space.repeat(10)+star.repeat(17));
            //System.out.println(); 
    }
    
}

