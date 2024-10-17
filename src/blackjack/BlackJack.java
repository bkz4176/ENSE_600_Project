package blackjack;

/**
 *
 * @author daniel
 */
import java.util.List;

public class BlackJack {
    private final Dealer dealer;
    private final Model model;
    private final Deck deck; 
    public static List<ActualPlayer> players;


    public BlackJack(Model model) {
        this.model = model;
        deck = model.getDeck();
        dealer = new Dealer();
        DataFile.createFile("Game_log.txt");
    }

    public static void main(String[] args) {
       
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(view, model);
        view.setController(controller);
    }
}
