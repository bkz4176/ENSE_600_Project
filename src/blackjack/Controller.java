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
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Controller {
    private View view;
    private Model model;
    public static List<ActualPlayer> players;

    
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
    
    private void showBetPanel()
    {
        JPanel betPanel = view.getBetPanel();
        if (betPanel == null) {
    System.out.println("Error: betPanel is null!");
} else {
    System.out.println("betPanel is valid, switching to it.");
}
        view.switchToPanel(betPanel);
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
        //showBlackjack();
        //ActualPlayer.initializePlayers(playerNames);
        BlackJack game = new BlackJack();
        players = ActualPlayer.initializePlayers(playerNames);
        game.start();
        showBetPanel();
        //showBlackjack();
        
        
        for (ActualPlayer p : players)
        {
            System.out.println("Debugger: " + p.getName() + ", Balance: " + p.getBalance());
        }
        //game.start();
        
    }
    
    public boolean processBet(int playerIndex, int betAmount)
    {
        // Get the player object based on the index
        ActualPlayer currentPlayer = players.get(playerIndex);

        // Validate the bet
        if (betAmount <= 0 || betAmount > currentPlayer.getBalance())
        {
            return false; // Invalid bet
        }

        // Save the bet
        currentPlayer.getBankAccount().placeBet(betAmount);

        // Optionally log or print the bet for debugging
        System.out.println(currentPlayer.getName() + " placed a bet of $" + betAmount);
        //updateBalanceLabel(playerIndex);

        return true; // Bet was successful
    }
    
    private void updateBalanceLabel(int playerIndex)
    {
        // Get the player object
        ActualPlayer currentPlayer = players.get(playerIndex);

        // Find the JLabel associated with this player (ensure you have a way to map playerIndex to JLabel)
        JLabel balanceLabel = view.playerBalanceLabels.get(playerIndex); // Assuming you have a list of JLabels for balance

        // Update the label text to reflect the new balance
        balanceLabel.setText("$" + currentPlayer.getBalance());

        // Optionally revalidate and repaint the panel if needed
        balanceLabel.revalidate();
        balanceLabel.repaint();
    }
    
    
}