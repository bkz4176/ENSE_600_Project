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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Controller {
    private final View view;
    private final Model model;
    public static List<ActualPlayer> players;
    private int playerIndex = 0;
    private boolean firstTurn = true;
    private boolean doubleDown = false;
    private boolean checkPlayAgain = false;
    Map<ActualPlayer, String> outcomes = new HashMap<>();
    

    
    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        view.getRulesButton().addActionListener(e -> {
            try {
                showRules();
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        view.getPlayButton().addActionListener(e -> showPlayers()); // Placeholder for play game
        view.getStatsButton().addActionListener(e -> showStats());
        view.getSubmitButton().addActionListener(e -> {
            getNumPlayers();  
            view.getStartButton().addActionListener(e2 -> {
                try {
                    getPlayerNames();
                } catch (SQLException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
        view.getHitButton().addActionListener(e -> handleHit());
        view.getStayButton().addActionListener(e -> handleStay());
        view.getDoubleDownButton().addActionListener(e -> handleDoubleDown());
    }

    private void showStats() {
        view.switchToPanel(view.getStatsPanel());
    }

    private void showRules() throws SQLException {
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
    
    private void getPlayerNames() throws SQLException
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
        players = model.initializePlayers(playerNames);
        DataFile.playerInfo(Controller.players, "Player_Info.txt");
        startNewGame();
        showBetPanel();
        
        for (ActualPlayer p : players)
        {
            System.out.println("Debugger: " + p.getName() + ", Balance: " + p.getBalance());
        }
        
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
        currentPlayer.setBetAmount(betAmount);

        // Optionally log or print the bet for debugging
        System.out.println(currentPlayer.getName() + " placed a bet of $" + currentPlayer.getBetAmount());
        view.refreshBetPanel(view.getPlayersPanel());

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
        //System.out.println(currentPlayer + " " + currentPlayer.getHand().toString());
        firstTurn = false;
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
        if(currentPlayer.getBalance() >= currentPlayer.getBetAmount() && firstTurn)
        {
            int bet = (currentPlayer.getBetAmount());
            currentPlayer.getBankAccount().placeBet(bet);
            currentPlayer.setBetAmount(bet*2);
        
            currentPlayer.addCardToHand(model.getDeck().drawCard());
            doubleDown = true;
            view.refreshPlayerPanels(view.getPlayersPanel());
            checkPlayerStatus(currentPlayer);
        }
        else
        {
            if (currentPlayer.getBalance() < currentPlayer.getBetAmount())
            {
            view.showMessage("Insufficient balance to double down!");
            }
            else if (!firstTurn)
            {
                view.showMessage("You can only double down on your first turn!");
            }
        }
    }
    
    private void checkPlayerStatus(ActualPlayer player)
    {
        if (player.getHandValue() > 21)
        {
            // Handle player bust (e.g., notify the player, update UI)
            view.showPlayerBust(player); 
            Timer delayTimer = new Timer(1500, (ActionEvent e) -> {
                moveToNextPlayer();
            });
        delayTimer.setRepeats(false); // Ensure the timer runs only once
        delayTimer.start(); // Start the timer with a delay
    
        }
        if(doubleDown && player.getHandValue()<=21)
        {
            moveToNextPlayer();
        }
        
    }
    
    private void moveToNextPlayer()
    {
        playerIndex++;
        firstTurn = true;
        doubleDown = false;

        // Check if it's the dealer's turn
        if (playerIndex >= players.size())
        {
            view.refreshPlayerPanels(view.getPlayersPanel()); 
            Timer timer = new Timer(1000, (ActionEvent e) -> {
                ((Timer) e.getSource()).stop(); // Stop the timer
                dealerPlay(); // Start the dealer's turn after 1 second
            });

        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start(); // Start the timer
        }
        else
        {
            view.refreshPlayerPanels(view.getPlayersPanel()); // Move to the next player
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
            Timer dealerTimer = new Timer(1000, (ActionEvent e) -> {
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
                    
                    outcomes = Winners.determineWinner(model.getDealer(), players);
                    model.savePlayerStats(players);
                    view.refreshStatsPanel(view.getstatsDisplayPanel());
                    view.refreshPlayerPanels(view.getPlayersPanel());
                    
                    Timer outcomeDisplayTimer = new Timer(1000, (ActionEvent e1) -> {
                        outcomes = null; // Clear outcomes after displaying them
                        DataFile.playerInfo(players, "Player_Info.txt");
                        playAgain(); // Proceed to the next round
                        ((Timer) e1.getSource()).stop(); // Stop this timer
                    });
                    outcomeDisplayTimer.setRepeats(false); // Only run once
                    outcomeDisplayTimer.start(); // Start the timer for delaying playAgai
                }
            });
            dealerTimer.start(); // Start the timer for dealer actions
        }
        else
        {
            playAgain();
        }
    }
    
    private void playAgain()
    {
        List<ActualPlayer> playersToKeep = new ArrayList<>();
        
        for (ActualPlayer player : players)
        {
            if (player.getBalance() == 0)
            {
                handlePlayerExit(player);
                continue; // Skip the dialog if they have no balance
            }

            int response = JOptionPane.showConfirmDialog(
                view, // Reference to your main GUI frame or panel
                player.getName() + ", do you want to play again?", 
                "Play Again", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (response == JOptionPane.YES_OPTION)
            {
                // Player wants to play again, reset their status
                playersToKeep.add(player);
                resetForNextGame(player);
            
            } 
            else
            {
            // Handle the case where the player chooses not to play again
            handlePlayerExit(player);
            //playersToKeep.add(player);
            }
        }
    
        System.out.println("Before assignment: Current players size = " + players.size());
        System.out.println("Players to keep size = " + playersToKeep.size());
        players = playersToKeep;
        model.setPlayers(players);
        model.setNumOfPlayer(players.size());
    
        System.out.println("After assignment: New players size = " + players.size());
    
        System.out.println("Remaining players: " + players.size());
        for (ActualPlayer p : players)
        {
            System.out.println("Player: " + p.getName() + " Balance: " + p.getBalance());
        }
        checkPlayAgain = true;
        startNextGame();
        
    }
    
    private void resetForNextGame(ActualPlayer player)
    {
        player.clearHand(); // Clear player's hand
        player.setBetAmount(0);
    }
    
    private void handlePlayerExit(ActualPlayer player)
    {
        if(player.getBalance()==0)
        {
            System.out.println(player.getName() + " is out of money and will be removed from the game.");
        }
        else
        {
            System.out.println(player.getName() + " has chosen to exit the game.");
        }
    }
    private void startNextGame()
    {
        if(checkPlayAgain)
        {
            checkPlayAgain = false;
            playerIndex = 0;
            model.getDealer().clearHand();
            if (!players.isEmpty())
            {
            startNewGame();
            view.refreshDealerPanel(view.getDealerPanel(), this);
            view.refreshPlayerPanels(view.getPlayersPanel());
            view.switchToPanel(view.getBetPanel());
            }
            else
            {
                endGame();
                System.out.println("Players list is empty");
            }
        }
        
    }
    
    public void startNewGame()
    {
        model.getDeck().shuffle();
        for (ActualPlayer p : players)
        {
            p.clearHand(); // Ensure each player starts with an empty hand
        }

        // Deal cards to each player
        for (ActualPlayer p : players)
        {
            p.addCardToHand(model.getDeck().drawCard());
            p.addCardToHand(model.getDeck().drawCard());
        }
        model.getDealer().addCardToHand(model.getDeck().drawCard());

    }
    private void endGame()
    {
        if (players.isEmpty())
        {

            JOptionPane optionPane = new JOptionPane(
            "All players are out. Returning to the home screen.", 
            JOptionPane.INFORMATION_MESSAGE
            );
        
            // Create a JDialog from the JOptionPane
            JDialog dialog = optionPane.createDialog(view, "Game Over");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
            // Start a Timer to close the dialog after 1 second
            Timer timer = new Timer(1000, (ActionEvent e) -> {
                dialog.dispose(); // Close the dialog automatically
                view.resetGameState(); // Return to home screen after closing
                
                // Stop the timer
                ((Timer) e.getSource()).stop();
            });
        
            timer.setRepeats(false); // Ensure the timer only runs once
            timer.start(); // Start the timer
        
            // Show the dialog (it will auto-close after 1 second)
            dialog.setVisible(true);
        }
    }
}