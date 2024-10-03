/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author daniel
 */
import java.util.ArrayList;
import javax.swing.JTextField;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        view.getRulesButton().addActionListener(e -> showRules());
        view.getPlayButton().addActionListener(e -> showPlayers()); // Placeholder for play game
        view.getStatsButton().addActionListener(e -> showStats());
        view.getSubmitButton().addActionListener(e -> {
            getNumPlayers();  
            view.getStartButton().addActionListener(e2 -> getPlayerNames());
        });
    }

    private void showStats() {
        view.switchToPanel(view.getStatsPanel());
    }

    private void showRules() {
        view.setRulesText(model.getRules());
        view.switchToPanel(view.getRulesPanel());
    }
    
    private void showPlayers()
    {
        view.switchToPanel(view.getPlayPanel());
    }
    
    private void showBlackjack()
    {
        view.switchToPanel(view.getBlackJackPanel());
    }
    
    private void getNumPlayers()
    {
        int numberOfPlayers = view.getSpinnerValue();
        model.setNumOfPlayer(numberOfPlayers);
        System.out.println("Number of Players: "+ model.getNumOfPlayers());//debugger
        view.nameFields(numberOfPlayers);
    }
    
    private void getPlayerNames()
    {
        System.out.println("Start button clicked!");
        ArrayList<String> playerNames = new ArrayList<>();
        for(JTextField f : view.getNameFields())
        {
            playerNames.add(f.getText().trim());
        }
        model.setPlayerNames(playerNames);
        int x =1;
        for(String s : playerNames)
        {
            System.out.println("Player "+x+": "+s); // debugger
            x++;
        }
        showBlackjack();
        ActualPlayer.initializePlayers(playerNames);
    }
}