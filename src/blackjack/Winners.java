package blackjack;

/**
 *
 * @author daniel
 */
import java.util.List;

public class Winners {

    public static void determineWinner(Dealer dealer, List<ActualPlayer> players)// Method do determine the winners from the from the current game
    //and to add the winnings to the players bank account.
    {
        String space = " ";
        String star = " * ";
        int dealerValue = dealer.getHandValue();
        String width = space.repeat(11)+"* ";

        for(ActualPlayer p : players)
        {
            int bet = p.getBetAmount();
            if (dealerValue > 21 && p.getHandValue()<= 21)
            {
                p.addWinnings(bet*2);
                System.out.print(width+p.getName() + " wins with: " + p.getHandValue());
                DataFile.log(p.getName() + " wins with: " + p.getHandValue()+" || New bank total = $" + p.getBalance());
                
            }
            else if(p.getHandValue() > 21)
            {
                System.out.print(width+dealer.getName() + " wins against " + p.getName());
                DataFile.log(dealer.getName() + " wins against " + p.getName()+" || New bank total = $" + p.getBalance());
            }
            else if(dealerValue <= 21 && p.getHandValue()<=21 && p.getHandValue()>dealerValue)
            {
                p.addWinnings(bet*2);
                System.out.print(width+p.getName() + " wins with: " + p.getHandValue());
                DataFile.log(p.getName() + " wins with: " + p.getHandValue()+" || New bank total = $" + p.getBalance());
                
            }
            else if(p.getHandValue() == dealerValue && p.getHandValue() <= 21)
            {
                p.addWinnings(bet);
                System.out.print(width+"Push (tie) with " + p.getName());
                DataFile.log("\n"+"Push (tie) with " + p.getName()+" || New bank total = $" + p.getBalance());
            }
            else
            {
                
                System.out.print(width+dealer.getName() + " wins against " + p.getName());
                DataFile.log(dealer.getName() + " wins against " + p.getName()+ " || New bank total = $" + p.getBalance());
                
            }

            System.out.println(" || New bank total = $" + p.getBalance());
            
        }
            System.out.println("\n"+space.repeat(10)+star.repeat(17));
            System.out.println(); 
    }
    
}

