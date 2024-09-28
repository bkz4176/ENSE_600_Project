package blackjack;
/**
 *
 * @author daniel
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ActualPlayer extends Player {
    
    private final BankAccount bankAccount; // final
    static Scanner scanner = new Scanner(System.in);
    private int betAmount;
    private boolean firstTurn;

    public ActualPlayer(String name) // constructor for initilizing a new player.
    {
        super(name);
        this.bankAccount = new BankAccount(100);
        this.firstTurn = true;
    }

    public ActualPlayer(String name, int balance) { // constructor for initializing an existing player 
        super(name);
        this.bankAccount = new BankAccount(balance);
        this.firstTurn = true;
    }

    public static List<ActualPlayer> initializePlayers() // method to get the number of players and the name of each player
    //If the player has played a game previously it loads the existing balance of the player.
    {
        
        List<ActualPlayer> players = new ArrayList<>();
        
        String space = " ";
        String width = space.repeat(11)+"* ";
        System.out.print(width+"Enter the number of players(1-7): ");
        int numberOfPlayers = 0;
        boolean validInput = false;
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
                numberOfPlayers = Integer.parseInt(input);
                if(numberOfPlayers <=0 || numberOfPlayers >7)
                {
                    System.out.println(width + "Please enter an interger greater than zero or less than 8.");
                    System.out.print(width + "Enter the number of players: ");
                }
                else
                {
                    DataFile.log("Enter the number of players: "+ numberOfPlayers+"\n");
                    validInput = true;
                }
            }
            catch(NumberFormatException e)
            {
                DataFile.log("Enter the number of players: "+ input);
                System.out.println(width+"Invalid input. Please enter a integer!");
                DataFile.log("Invalid input. Please enter a integer!");
                System.out.print(width+"Enter the number of players: ");
            }
        }
        System.out.println();
        Map<String, Integer> existingPlayerInfo = DataFile.readPlayerInfo("Player_Info.txt");
        for (int i = 1; i <= numberOfPlayers; i++) 
        {
            System.out.print(width+"Enter the name of player " + i + ": ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("q"))
                {
                    System.out.println(width+"Quitting the game.");
                    DataFile.log("Quitting the game.");
                    System.exit(0); // Exit the program
                }
            DataFile.log("Enter the name of player " + i + ": "+ name);
            if(existingPlayerInfo.containsKey(name))
            {
                int balance = existingPlayerInfo.get(name);
                if(balance == 0)
                {
                    System.out.println(width + "Welcome back, " + name + "! A new balance has been loaded for you");
                    players.add(new ActualPlayer(name));
                }
                else
                {
                    System.out.println(width + "Welcome back, " + name + "! Your balance has been loaded");
                    players.add(new ActualPlayer(name, balance));
                }    
            }
            else
            {
                players.add(new ActualPlayer(name));
            }
        }
        DataFile.log(""); 
        return players;
    }

    public void getBet() //
    {
        this.betAmount = PlayerActions.getBet(this);
    }
    public int getBetAmount() // method to get the bet amount
    {
        return betAmount;
    }
    public BankAccount getBankAccount()
    {
        return bankAccount;
    }

    public void addWinnings(int amount)// add winnings to bank account
    {
        bankAccount.addWinnings(amount);
    }

    public int getBalance()//retuns the current bank balance.
    {
        return bankAccount.getBalance();
    }

    public void clearHand()//calls the clear method in the hand class to clear the cards in the list.
    {
        hand.clear(); 
        this.firstTurn = true; 
    }

    public boolean isFirstTurn()//checks if it is the first turn for a player.
    {
        return firstTurn;
    }

    public void setFirstTurn(boolean firstTurn)
    {
        this.firstTurn = firstTurn;
    }

    @Override
    public void play(Deck deck)//Method for players to play the game.
    {
        String space = " " ;
        int totalWidth = 50;
        int nameLength = name.length();
        int paddingBefore = (totalWidth - 12 - nameLength) / 2;
        String width = space.repeat(11)+"* ";
        
        while (true) {

            System.out.print(width+"\n");
            System.out.println(width+space.repeat(paddingBefore)+name + "'s turn!" );
            System.out.println(width+hand + " (value: " + getHandValue() + ")");
            DataFile.log(name + "'s turn!");
            DataFile.log(hand + " (value: " + getHandValue() + ")");

                if(getHandValue() == 21)
                {
                    break;
                }

                String action = PlayerActions.getAction(this);

                switch (action.toLowerCase()) {
                    case "h":
                        PlayerActions.hit(this, deck);
                        if (getHandValue() > 21) {
                            
                            break;
                        }
                        break;
                    case "s":
                        break;
                    case "d":
                        if (getBalance() >= this.betAmount) {
                            PlayerActions.doubleDown(this, deck);
                            this.betAmount = this.betAmount*2;
                            return;
                        } else {
                            System.out.println(width+ "Unable to Double Down");
                            DataFile.log("Unable to Double Down");
                        }
                        break;
                }
                firstTurn = false;
                if (action.equalsIgnoreCase("s") || getHandValue() > 21) {
                    break;
                }
            }
        }
        
    }

