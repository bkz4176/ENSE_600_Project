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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Controller {
    private View view;
    private Model model;
    public static List<ActualPlayer> players;
    private int playerIndex = 0;
    private boolean firstTurn = true;

    
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
        view.getHitButton().addActionListener(e -> handleHit());
        view.getStayButton().addActionListener(e -> handleStay());
        view.getDoubleDownButton().addActionListener(e -> handleDoubleDown());
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
        if (betPanel == null)
        {
            System.out.println("Error: betPanel is null!");
        } 
        else
        {
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
        BlackJack game = new BlackJack(model);
        players = ActualPlayer.initializePlayers(playerNames);
        game.start();
        showBetPanel();
        
        for (ActualPlayer p : players)
        {
            System.out.println("Debugger: " + p.getName() + ", Balance: " + p.getBalance());
        }
        //game.start();
        
    }
    public int getPlayerIndex()
    {
        return playerIndex;
    }
    public void setPlayerIndex(int x)
    {
        this.playerIndex= x;
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
    
    public Dealer getDealer()
    {
        return model.getDealer();
    }
    
    private void handleHit()
    {
        System.out.println("Player " + (playerIndex+1) + ": Hit");

        //List<ActualPlayer> players = model.getPlayers();
        System.out.println("Total players: " + players.size());
        if (playerIndex < 0 || playerIndex >= players.size())
        {
            System.out.println("Invalid player index: " + playerIndex);
            return; // Exit early to avoid NullPointerException
        }
        
        ActualPlayer currentPlayer = players.get(playerIndex);
        
        if (currentPlayer == null)
        {
            System.out.println("Current player is null at index: " + playerIndex);
            return; // Exit if currentPlayer is null
        }

        // Ensure the deck is initialized and not null
        if (model.getDeck() == null)
        {
            System.out.println("The deck is not initialized.");
            return; // Exit if the deck is null
        }

        currentPlayer.addCardToHand(model.getDeck().drawCard());
        System.out.println(currentPlayer + " " + currentPlayer.getHand().toString());
        view.refreshPlayerPanels(view.getPlayersPanel());
        checkPlayerStatus(currentPlayer); // Check if the player has busted
    }
    private void handleStay()
    {

        moveToNextPlayer();
        
    }
    private void handleDoubleDown()
    {
        ActualPlayer currentPlayer = players.get(playerIndex);
        int currentBet = currentPlayer.getBetAmount();
        currentPlayer.getBankAccount().placeBet(currentBet);
        
        currentPlayer.addCardToHand(model.getDeck().drawCard());
        firstTurn = false;
        view.refreshPlayerPanels(view.getPlayersPanel());
        checkPlayerStatus(currentPlayer);
    }
    
    private void checkPlayerStatus(ActualPlayer player)
    {
        if (player.getHandValue() > 21)
        {
            // Handle player bust (e.g., notify the player, update UI)
            view.showPlayerBust(player); 
            Timer delayTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveToNextPlayer();
            }
        });
        delayTimer.setRepeats(false); // Ensure the timer runs only once
        delayTimer.start(); // Start the timer with a delay
    
        }
        if(!firstTurn && player.getHandValue()<=21)
        {
            moveToNextPlayer();
        }
    }
    
    private void dealerPlay()
    {
        boolean notBusted = false;
        // Dealer's turn if player hasn't busted
        for(ActualPlayer p : players)
        {
            if(p.getHandValue()<=21)
            {
                notBusted = true;
            }
        }
        if(notBusted)
        {
            model.getDealer().addCardToHand(model.getDeck().drawCard());
            view.refreshDealerPanel(view.getDealerPanel(), this);
            Timer dealerTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if the dealer's hand value is less than 17
                if (model.getDealer().getHandValue() < 17) {
                    // Draw another card for the dealer
                    model.getDealer().addCardToHand(model.getDeck().drawCard());
                    view.refreshDealerPanel(view.getDealerPanel(), Controller.this);
                } else {
                    // Stop the timer if dealer's hand value is 17 or more
                    ((Timer)e.getSource()).stop();

                    // Check if the dealer has busted
                    if (model.getDealer().getHandValue() > 21) {
                        view.showDealerBustMessage(); // Show bust message if dealer busts
                    }
                }
            }
        });
            dealerTimer.start(); // Start the timer for dealer actions
        }
    }
    
    private void moveToNextPlayer()
    {
        playerIndex++;
        firstTurn = true;

        // Check if it's the dealer's turn
        if (playerIndex >= players.size())
        {
            view.refreshPlayerPanels(view.getPlayersPanel()); 
            dealerPlay(); // Start the dealer's turn if all players have played
        }
        else
        {
            view.refreshPlayerPanels(view.getPlayersPanel()); // Move to the next player
        }
    }
    
}