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

    public static void main(String[] args) {
       
        //DataFile.playerInfo(Controller.players, "Player_Info.txt");
        
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(view, model);
        view.setController(controller);
    }
}
