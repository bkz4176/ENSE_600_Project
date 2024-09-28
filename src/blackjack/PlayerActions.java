package blackjack;

/**
 *
 * @author daniel
 */
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class PlayerActions {
    static String space = " ";
    static String width = space.repeat(11)+"* ";

    static Scanner scanner = new Scanner(System.in);

    public static int getBet(ActualPlayer player) // get the bet of a player. It checks if the player is able to make a bet of x amount.
    {
        System.out.println();
        System.out.print(width+ player.getName() + ": Current Bank balance: $" + player.getBalance() + " - enter bet amount: ");
        boolean validInput = false;
        int bet =0;
        while(!validInput)
        {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q"))
            {
                System.out.println(width+"Quitting the game.");
                DataFile.log("Quitting the game.");
                System.exit(0); // Exit the program
            }
            try
            {    
                bet = Integer.parseInt(input);
                validInput = true;
                DataFile.log(player.getName() + ": Current Bank balance: $" + player.getBalance() + " - enter bet amount: " + bet);
            }
            catch(NumberFormatException e)
            {
                System.out.println(width+"Invalid input. Please enter a integer!");
                DataFile.log("Invalid input. Please enter a integer!");
                System.out.print(width);
            }
        }
        if(bet <0)
        {
            System.out.println(width+"Invalid bet amount! You must bet an amount greater than 0!");
            DataFile.log("Invalid bet amount! You must bet an amount greater than 0!");
            return getBet(player);
        }
        else if(bet > player.getBalance())
        {
            System.out.println(width+"Invalid bet amount! Bet exceeds current bank balance!");
            DataFile.log("Invalid bet amount! Bet exceeds current bank balance!");
            return getBet(player);
        }
        else{
            player.getBankAccount().placeBet(bet);
            return bet;   
        }
    }

    public static void addWinnings(ActualPlayer player, int amount) // adding winnings to players bank balance
    {
        player.getBankAccount().addWinnings(amount);
    }
    public static void hit(ActualPlayer player, Deck deck) // method to add a card to players hand when option 'hit' is selected.
    {
        player.addCardToHand(deck.drawCard());
        System.out.println(width+player.getHand() + " (value: " + player.getHandValue() + ")");
        DataFile.log(player.getHand() + " (value: " + player.getHandValue() + ")"); 
        if (player.getHandValue() > 21)
        {
            System.out.println(width+"Bust!");
            DataFile.log("Bust!");
        }
    }

    public static void doubleDown(ActualPlayer player, Deck deck) // method for player to double down
    {
        int bet = player.getBetAmount();
        player.getBankAccount().placeBet(bet);
        player.addCardToHand(deck.drawCard());
        System.out.println(width+player.getHand() + " (value: " + player.getHandValue() + ")");
        DataFile.log(player.getHand() + " (value: " + player.getHandValue() + ")");   
    }

    public static String getAction(ActualPlayer player) // get the action of the player. allows player input to select which move they wish to do.
    {
        String action;
    
        while (true) {
            if(player.isFirstTurn())
            {
                System.out.print(width+"Hit, Stand, Double Down? (h/s/d): ");
            }
            else
            {
                System.out.print(width+"Hit, Stand (h/s): ");
                
            }
            action = scanner.nextLine().trim().toLowerCase();
            if (action.equalsIgnoreCase("q"))
            {
                System.out.println(width+"Quitting the game.");
                DataFile.log("Quitting the game.");
                System.exit(0); // Exit the program
            }
            
            if(player.isFirstTurn())
            {
                if (action.equals("h") || action.equals("s") || action.equals("d")) {
                    DataFile.log("Hit, Stand, Double Down? (h/s/d): "+action);
                    if(action.equals("d"))
                    {
                        player.setFirstTurn(false);
                    }
                    return action;
                } else {
                    System.out.println(width+"Invalid Action. Please enter 'h' for hit, 's' for stand, or 'd' for double down.");
                    DataFile.log("Invalid Action. Please enter 'h' for hit, 's' for stand, or 'd' for double down.");
                }
            }
            else
            {
                DataFile.log("Hit, Stand (h/s): "+action);
                if (action.equals("h") || action.equals("s"))
                {  
                    return action; 
                }   
                else {
                    System.out.println(width+"Invalid Action. Please enter 'h' for hit or 's' for stand");
                    DataFile.log("Invalid Action. Please enter 'h' for hit or 's' for stand");
                }
            }   
        }
    }

    public static void handlePlayerDecisions(List<ActualPlayer> players)//checks if players wish to play again at the end of each round.
    //this method will also remove a player automatically if they are out of money.
    {
        Iterator<ActualPlayer> iterator = players.iterator();

        while (iterator.hasNext()) {
            ActualPlayer player = iterator.next();

            if (player.getBalance() <= 0) {
                System.out.println(width+player.getName() + " is out of money and will be removed from the game.");
                DataFile.log("\n"+player.getName() + " is out of money and will be removed from the game.");
                iterator.remove();  
                continue;   
            }
            while(true)
            {
                System.out.print(width+player.getName() + ", do you want to play again? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                if (response.equalsIgnoreCase("q"))
                {
                    System.out.println(width+"Quitting the game.");
                    DataFile.log("Quitting the game.");
                    System.exit(0); // Exit the program
                }
                if (response.equals("y") || response.equals("n"))
                {
                    DataFile.log("\n"+player.getName() + ", do you want to play again? (y/n): "+response);
                    if (response.equals("n")) 
                    {
                        System.out.println(width + player.getName() + " has left the game!");
                        DataFile.log(player.getName() + " has left the game!");
                        iterator.remove();  
                    }
                    break;  
                } 
                else 
                {
                    System.out.println(width + "Invalid input. Please enter 'y' for yes or 'n' for no.");
                    DataFile.log("Invalid input. Please enter 'y' for yes or 'n' for no.");
                }
            }
            
        }

    }
}
    
