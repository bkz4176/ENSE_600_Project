package blackjack;
/**
 *
 * @author daniel
 */
public class BankAccount
{

    private int balance;

    public BankAccount(int startingBalance)
    {

        this.balance = startingBalance;
    }

    public int getBalance()
    {
        return balance;
    }

    public void addWinnings(int amount) // adding winnings to bank bank balance.
    {
        if(amount > 0)
        {
            balance += amount;
        }
    }

    public boolean placeBet(int amount)//removing money from bank balance once a bet is placed
    {
        if(amount > 0 && amount <= this.balance)
        {
            balance -= amount;
            return true;
        }
        return false;
    }
    
}

