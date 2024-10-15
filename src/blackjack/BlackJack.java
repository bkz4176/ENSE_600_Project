package blackjack;

/**
 *
 * @author daniel
 */
import java.util.List;
import java.util.Scanner;

public class BlackJack {
    private static final Scanner scanner = new Scanner(System.in);
    private final Dealer dealer;
    private Model model;
    private final Deck deck; 
    public static List<ActualPlayer> players;


    public BlackJack(Model model) {
        this.model = model;
        deck = model.getDeck();
        dealer = new Dealer();
        DataFile.createFile("Game_log.txt");
    }

    public void start() {
        
        //DataFile.playerInfo(Controller.players, "Player_Info.txt"); // this is now working correctly. Names and balances get saved c
        //deck.shuffle(); // shuffle deck at the start of each game.
        
        /*for(ActualPlayer p : Controller.players)
        {
            p.getBet(); // get bets for each player   
        }*/

        /*for(ActualPlayer p : Controller.players) // adding cards to each players hand
        {
            p.addCardToHand(deck.drawCard());
            p.addCardToHand(deck.drawCard());
        }*/

        //model.getDealer().addCardToHand(deck.drawCard());
        //model.getDealer().addCardToHand(deck.drawCard());
        
        DataFile.log("\nThe The Game is Starting\n");

        //dealer.dealerInitialCard(); // show dealers face up card
        
        /*for(ActualPlayer p : players) // Player's turn
        {
            p.play(deck);   
        }*/

        /*dealer.addCardToHand(deck.drawCard());
        dealer.checkAndPlay(players,deck);
          
        /*Winners.determineWinner(dealer, players);

        DataFile.playerInfo(players, "Player_Info.txt");
        PlayerActions.handlePlayerDecisions(players);

        for(ActualPlayer p : players)
        {
            p.clearHand();
        }
        dealer.clearHand(); */
    }

    public static void main(String[] args) {
       
        //DataFile.playerInfo(Controller.players, "Player_Info.txt");
        
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(view, model);
        view.setController(controller);
    }
}
