/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author daniel
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        view.getGameRulesButton().addActionListener(e -> showRules());
        view.getPlayGameButton().addActionListener(e -> showPlayers()); // Placeholder for play game
        view.getPlayerStatsButton().addActionListener(e -> showStats());
        
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
        view.switchToPanel(view.getPlayersPanel());
    }

    private void showPlayGame() {
        // Implement play game logic here
    }
    
    
}