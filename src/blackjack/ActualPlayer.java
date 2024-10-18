package blackjack;
/**
 *
 * @author daniel
 */

public class ActualPlayer extends Player {
    
    private final BankAccount bankAccount; // final
    private int betAmount;
    private boolean firstTurn;
    
    private int totalEarnings;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesDrawn;
    private int currentStreak;
    private int totalLoses;
    private int totalWinnings;

    public ActualPlayer(String name) // constructor for initilizing a new player.
    {
        super(name);
        this.bankAccount = new BankAccount(100);
        this.firstTurn = true;
        this.totalEarnings = 0;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.gamesDrawn = 0;
        this.currentStreak = 0;
        this.totalLoses = 0;
        this.totalWinnings = 0;
    }

    public ActualPlayer(String name, int balance, int totalEarnings, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn, int currentStreak) { // constructor for initializing an existing player 
        super(name);
        this.bankAccount = new BankAccount(balance);
        this.firstTurn = true;
        this.totalEarnings = totalEarnings; // Existing total earnings
        this.gamesPlayed = gamesPlayed; // Existing games played
        this.gamesWon = gamesWon; // Existing games won
        this.gamesDrawn = gamesDrawn;
        this.gamesLost = gamesLost; // Existing games lost
        this.currentStreak = currentStreak; // Existing current streak
        this.totalLoses = 0; // You can adjust how to handle this
        this.totalWinnings = 0;
    }
    
    public void setBetAmount(int betAmount)
    {
        this.betAmount = betAmount;
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
    
    // New methods for player statistics
    public void incrementGamesPlayed() {
        gamesPlayed++;
    }

    public void incrementGamesWon() {
        gamesWon++;
        currentStreak++;
    }

    public void incrementGamesLost() {
        gamesLost++;
        currentStreak = 0; // Reset streak on loss
    }
    
    public void incrementGamesDrawn(){
        gamesDrawn++;
    }
    
    public void setTotalLoss(int bet)
    {
        this.totalLoses = bet;
    }
    
    public void setTotalWinnings(int bet)
    {
        this.totalWinnings = bet;
    }
    
    public void setTotalEarnings()
    {
        this.totalEarnings += this.totalWinnings-this.totalLoses;
        this.totalLoses = 0;
        this.totalWinnings = 0;
    }

    public int getTotalEarnings() {
        return totalEarnings;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }
    
    public int getGamesDrawn(){
        return gamesDrawn;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    @Override
    public void play(Deck deck)//Method for players to play the game.
    {
    }
 }

