package blackjack;

/**
 *
 * @author daniel
 */
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;

public class BlackJack {
    private static final Scanner scanner = new Scanner(System.in);
    private final Deck deck; 
    private final Dealer dealer;
    public static List<ActualPlayer> players;


    public BlackJack() {
        deck = new Deck();
        dealer = new Dealer();
        DataFile.createFile("Game_log.txt");
    }

    public void start() {

        DataFile.playerInfo(players, "Player_Info.txt");
        deck.shuffle(); // shuffle deck at the start of each game.
        for(ActualPlayer p : players)
        {
            p.getBet(); // get bets for each player   
        }

        for(ActualPlayer p : players) // adding cards to each players hand
        {
            p.addCardToHand(deck.drawCard());
            p.addCardToHand(deck.drawCard());
        }
        
        dealer.addCardToHand(deck.drawCard());
        
        DataFile.log("\nThe The Game is Starting\n");

        dealer.dealerInitialCard(); // show dealers face up card
        
        for(ActualPlayer p : players) // Player's turn
        {
            p.play(deck);   
        }

        dealer.addCardToHand(deck.drawCard());
        dealer.checkAndPlay(players,deck);
          
        Winners.determineWinner(dealer, players);

        DataFile.playerInfo(players, "Player_Info.txt");
        PlayerActions.handlePlayerDecisions(players);

        for(ActualPlayer p : players)
        {
            p.clearHand();
        }
        dealer.clearHand(); 
    }

    public static void main(String[] args) {
       

        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(view, model);
        try (scanner) {
            BlackJack game = new BlackJack();
      
            //players = ActualPlayer.initializePlayers();
            while(!players.isEmpty())
            {  
                game.start();
            }
        }
        DataFile.log("\nGame over. All players are out");
        
    }
    
}
