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
    // Initilize attributes of the controller class
    private final View view;
    private final Model model;
    public static List<ActualPlayer> players;
    private int playerIndex = 0;
    private boolean firstTurn = true;
    private boolean doubleDown = false;
    private boolean checkPlayAgain = false;
    Map<ActualPlayer, String> outcomes = new HashMap<>();
   
 /**
 * Constructor for the Controller class, initializing it with the provided view
 * and model. Sets up action listeners for various buttons to link user interactions 
 * to the corresponding methods for handling game rules, player setup, statistics,
 * and gameplay actions like hitting, staying, and doubling down.
 */
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
        view.getPlayButton().addActionListener(e -> showPlayers());
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
        view.switchToPanel(view.getStatsPanel()); // switches to the stats panel
    }

    private void showRules() throws SQLException { // switches to the rules panel
        view.setRulesText(model.getRules());
        view.switchToPanel(view.getRulesPanel());
    }
    
    private void showPlayers()
    {
        view.switchToPanel(view.getPlayPanel()); // switches to the play panel
    }
    
    /*private void showBlackjack()
    {
        view.switchToPanel(view.getBlackJackPanel());
        
    }*/
    
    private void showBetPanel() // switches to the bet Panel
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
    
    private void getNumPlayers() // gets the number of players from the spinner then sets it to the model
    {
        int numberOfPlayers = view.getSpinnerValue();
        model.setNumOfPlayer(numberOfPlayers);
        System.out.println("Number of Players: "+ model.getNumOfPlayers());//debugger
        view.nameFields(numberOfPlayers);
    }
    
    private void getPlayerNames() throws SQLException // gets playernames from user input
    {
        System.out.println("Start button clicked!");
        ArrayList<String> playerNames = new ArrayList<>();
        
        boolean hasBlankNames = false;
        
        for(JTextField f : view.getNameFields())
        {
            String playerName = f.getText().trim();
            if(playerName.isEmpty())
            {
                hasBlankNames = true;
                break; 
            }
            playerNames.add(playerName);
        }
        
        if (hasBlankNames)
        {
            JOptionPane.showMessageDialog(view, "Player name can not be blank!.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return; // Exit the method to prevent further processing
        }
        model.setPlayerNames(playerNames);//set the player names from user input
        players = model.initializePlayers(playerNames); // pass the player names to intilize players for game play.
        startNewGame(); // start new game
        showBetPanel();// switch to BetPanel
        
    }
    public int getPlayerIndex()
    {
        return playerIndex;
    }
    public void setPlayerIndex(int x)// setting player index for handling player actions such as Hit, stay , doubledown
    {
        this.playerIndex= x;
    }
    
    public boolean processBet(int playerIndex, int betAmount) // method to process bets from user input
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

        view.refreshBetPanel(view.getPlayersPanel());// update betPanel to reflect visual changes

        return true; // Bet was successful
    }
    
    public Dealer getDealer()
    {
        return model.getDealer();
    }
    
    private void handleHit()// method for handlining play action hit.
    {
        //System.out.println("Player " + (playerIndex+1) + ": Hit");
        //System.out.println("Total players: " + players.size());
        if (playerIndex < 0 || playerIndex >= players.size())// debbugers during testing
        {
            System.out.println("Invalid player index: " + playerIndex);
            return; // Exit early to avoid NullPointerException
        }
        
        ActualPlayer currentPlayer = players.get(playerIndex);// assign current player who's turn it is.
        
        if (currentPlayer == null)
        {
            System.out.println("Current player is null at index: " + playerIndex);// debbugger during testing
            return; // Exit if currentPlayer is null
        }

        // Ensure the deck is initialized and not null. Debbugger
        if (model.getDeck() == null)
        {
            System.out.println("The deck is not initialized.");
            return; // Exit if the deck is null
        }
        
        currentPlayer.addCardToHand(model.getDeck().drawCard());// add card to current players hand.
        firstTurn = false;// set first turn to false. player can no longer double down
        view.refreshPlayerPanels(view.getPlayersPanel());// update PlayersPanel to reflect visual changes.
        checkPlayerStatus(currentPlayer); // Check if the player has busted
    }
    private void handleStay()// method to handle a player selecting stay.
    {
        moveToNextPlayer();
    }
    
    private void handleDoubleDown()//method for handling a players action to double down.
    {
        ActualPlayer currentPlayer = players.get(playerIndex); // assign current player who's turn it is.
        if(currentPlayer.getBalance() >= currentPlayer.getBetAmount() && firstTurn) // conditions to allow current player to doubledown.
        {
            int bet = (currentPlayer.getBetAmount());
            currentPlayer.getBankAccount().placeBet(bet);
            currentPlayer.setBetAmount(bet*2);
        
            currentPlayer.addCardToHand(model.getDeck().drawCard());
            doubleDown = true;
            view.refreshPlayerPanels(view.getPlayersPanel());
            checkPlayerStatus(currentPlayer);
        }
        else // display message on screen if player is unable to doubledown.
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
    
    private void checkPlayerStatus(ActualPlayer player) // checks the players ability to make another action.
    {
        if (player.getHandValue() > 21)
        {
            view.showPlayerBust(player); // displays a bust message if handvalue is greater than 21
            Timer delayTimer = new Timer(1500, (ActionEvent e) -> {
                moveToNextPlayer();
            });
        delayTimer.setRepeats(false); // Ensure the timer runs only once
        delayTimer.start(); // Start the timer with a delay
    
        }
        if(doubleDown && player.getHandValue()<=21) //if player doubleDown and hand is <= 21 then move to next player.
        {
            moveToNextPlayer();
        }
        
    }
    
    private void moveToNextPlayer() // moves to next player.
    {
        playerIndex++; // increment player index by 1.
        firstTurn = true;// set first turn to true
        doubleDown = false;// set duouble down to false.

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
    
    private void dealerPlay() // method for the dealer to play.
    {
        boolean notBusted = false;
        // Dealer only playes if at least one player has not busted.
        for(ActualPlayer p : players)
        {
            if(p.getHandValue()<=21)
            {
                notBusted = true;
            }
        }
        if(notBusted) // Dealer play.
        {
            model.getDealer().addCardToHand(model.getDeck().drawCard());
            view.refreshDealerPanel(view.getDealerPanel(), this);// refresh panel to reflect changes.
            Timer dealerTimer = new Timer(1000, (ActionEvent e) -> {
                
                // Check if the dealer's hand value is less than 17
                if (model.getDealer().getHandValue() < 17) {
                    
                    // Draw another card for the dealer
                    model.getDealer().addCardToHand(model.getDeck().drawCard());
                    view.refreshDealerPanel(view.getDealerPanel(), Controller.this);
                } 
                else
                {
                    // Stop the timer if dealer's hand value is 17 or more
                    ((Timer)e.getSource()).stop();

                    // Check if the dealer has busted
                    if (model.getDealer().getHandValue() > 21) {
                        view.showDealerBustMessage(); // Show bust message if dealer busts
                        
                    }
                    
                    outcomes = Winners.determineWinner(model.getDealer(), players); // store outcomes of the game in a map. keyValue pair = actualPlayer and string
                    model.savePlayerStats(players);// update the database with update plaer stats
                    view.refreshStatsPanel(view.getstatsDisplayPanel());// refresh stats panel to reflect changes
                    view.refreshPlayerPanels(view.getPlayersPanel());// refresh players panel to display which player won or lost.
                    
                    Timer outcomeDisplayTimer = new Timer(1500, (ActionEvent e1) -> { 
                        outcomes = null; // Clear outcomes after displaying them
                        playAgain(); // check which players wish to play again
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
            playAgain(); // check which players wish to play again
        }
    }
    
    private void playAgain() // check which players wish to play again
    {
        List<ActualPlayer> playersToKeep = new ArrayList<>();
        
        for (ActualPlayer player : players)
        {
            if (player.getBalance() == 0)
            {
                handlePlayerExit(player);
                continue; // Skip the dialog if they have no balance
            }

            int response = JOptionPane.showConfirmDialog( // message prompt
                view, 
                player.getName() + ", do you want to play again?", 
                "Play Again", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (response == JOptionPane.YES_OPTION)
            {
                // if Player wants to play again, reset their status
                playersToKeep.add(player); // if players wish to play again store them in a temp ActualPlayer list
                resetForNextGame(player);
            
            } 
            else
            {
                // Handle the case where the player chooses not to play again
                handlePlayerExit(player);
                //playersToKeep.add(player);
            }
        }

        players = playersToKeep; // set players equal to the players that wish to play again list
        model.setPlayers(players); // update the model with update players.
        model.setNumOfPlayer(players.size()); // update model with new number of players.
    
        System.out.println("Remaining players: " + players.size()); // debbuger
        for (ActualPlayer p : players)
        {
            System.out.println("Player: " + p.getName() + " Balance: " + p.getBalance());
        }
        checkPlayAgain = true; // set to true.
        startNextGame(); // start the next game
        
    }
    
    private void resetForNextGame(ActualPlayer player)
    {
        player.clearHand(); // Clear player's hand
        player.setBetAmount(0); // set betAmount to zero.
    }
    
    private void handlePlayerExit(ActualPlayer player) // debugger for handling for removing a player.
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
    
    private void startNextGame() // start new game with update players list
    {
        if(checkPlayAgain)
        {
            checkPlayAgain = false;
            playerIndex = 0;// resey player index
            model.getDealer().clearHand(); // clear the dealers hand
            if (!players.isEmpty())
            {
                startNewGame();// start new game ad update neccsary panels
                view.refreshDealerPanel(view.getDealerPanel(), this); 
                view.refreshPlayerPanels(view.getPlayersPanel());
                view.switchToPanel(view.getBetPanel());
            }
            else
            {
                endGame(); // if players list is empty then end game.
                System.out.println("Players list is empty");
            }
        }
        
    }
    
    public void startNewGame() // intitlize new game
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
    private void endGame() // end game. return to home screen.
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