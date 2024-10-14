/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author daniel
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class View extends JFrame {
    
    private JPanel welcomePanel;
    private JPanel statsPanel;
    private JPanel rulesPanel;
    private JPanel playPanel;
    private JPanel blackJackPanel;
    private JPanel betPanel;
    private JPanel playersPanel;
    private JPanel centerPanel;
    private JPanel dealerPanel;
    
    private JButton submitButton;
    private JButton statsButton;
    private JButton rulesButton;
    private JButton playButton;
    private JButton backButton;
    private JButton startButton;
    private JButton hitButton;
    private JButton stayButton;
    private JButton doubleDownButton;
    private JButton betButton;
    
    private int currentPlayerIndex = 0; // Index to track which player's turn it is
    private JTextField betField = new JTextField();
    private JLabel playerPromptLabel = new JLabel(); // To display the current player's name
    private List<Integer> bets = new ArrayList<>(); // Store bets in sequence
            
    private JTextArea rulesTextArea;
    private ArrayList<JTextField> nameFields;
    //ArrayList<JLabel> playerBalanceLabels = new ArrayList<>();
   
    
    private final Model model;
    private Controller controller;
    
    
    private JSpinner spinner;
    private final String spacing = " ";
    
    private boolean betsComplete = false;
     
    public View(Model model)
    {
        this.model = model;
        setTitle("BlackJack Game");
        setSize(850, 550);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        setContentPane(welcomePanel);
        setVisible(true);
        
    }
    public void setController(Controller controller)
    {
        this.controller = controller;
    }
    
    private void initializeComponents() {
        welcomePanel = welcomePanel();
        rulesPanel = rulesPanel();
        statsPanel = statsPanel();
        playPanel = playPanel();
        hitButton = createButton("Hit");
        stayButton = createButton("Stay");
        doubleDownButton = createButton("Double Down");
        
    }
    
    private JPanel welcomePanel()
    {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));//this panel has 3 rows, 1 column, height is 10
        
        panel.setBackground(new Color(53, 101, 77));
        JLabel label = createLabel("Welcome to Blackjack!", 32, Color.YELLOW);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(53, 101, 77));
        statsButton = createButton("Statistics");
        rulesButton = createButton("Games Rules");
        playButton = createButton("Play");
        
        buttonPanel.add(statsButton);
        buttonPanel.add(playButton);
        buttonPanel.add(rulesButton);
       
        panel.add(label);
        panel.add(spacerPanel());
        panel.add(buttonPanel);
        
        return panel;
    }
    
    private JPanel rulesPanel()
    {
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(53, 101, 77));
        JLabel label = createLabel(spacing.repeat(20)+"Games Rules", 32, Color.YELLOW);
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        backButton = createButton("Back");
      
        headerPanel.add(backButton); 
        headerPanel.add(label);
        panel.add(headerPanel,BorderLayout.PAGE_START);
        
        rulesTextArea = new JTextArea();
        rulesTextArea.setEditable(false);
        rulesTextArea.setBackground(new Color(53, 101, 77));
        rulesTextArea.setForeground(Color.WHITE);
        rulesTextArea.setFont(new Font("Serif", Font.BOLD, 16));
        panel.add(new JScrollPane(rulesTextArea), BorderLayout.CENTER);
        
        clickBackButton(welcomePanel);
        return panel;
        
    }
    
    private JPanel statsPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(53, 101, 77));
        JLabel label = createLabel(spacing.repeat(25)+"Statistics", 32, Color.YELLOW);
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        backButton = createButton("Back");
      
        headerPanel.add(backButton); 
        headerPanel.add(label);
        panel.add(headerPanel,BorderLayout.PAGE_START);
        clickBackButton(welcomePanel);
        
        return panel;
        
    }
    
    private JPanel playPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(53, 101, 77));
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        backButton = createButton("Back");
        
        JPanel playerPanel = new JPanel(new FlowLayout());
        playerPanel.setBackground(new Color(53, 101, 77));
        JLabel numOfPlayers = createLabel("Select the Number of Players", 20, Color.YELLOW);
        playerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0)); // IMPPRTANT!!
        
        
        SpinnerModel model = new SpinnerNumberModel(1, 1, 7, 1); // Initial value, min, max, step
        spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(60, 30));
        submitButton = createButton("Submit");
        
        playerPanel.add(numOfPlayers);
        playerPanel.add(spinner);
        playerPanel.add(submitButton);
        headerPanel.add(backButton); 
        panel.add(headerPanel,BorderLayout.PAGE_START);
        panel.add(playerPanel,BorderLayout.CENTER);
        

        clickBackButton(welcomePanel);
        
        return panel;
    }
    
    public void nameFields(int numberOfPlayers)
    {
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBackground(new Color(53, 101, 77));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        backButton = createButton("Back");
        
        JLabel heading = createLabel(spacing.repeat(40)+"Enter Player Names:", 20, Color.YELLOW);

        JPanel nameBox = new JPanel();
        nameBox.setLayout(new BoxLayout(nameBox, BoxLayout.Y_AXIS));
        nameBox.setBackground(new Color(53, 101, 77));
        nameBox.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        nameFields = new ArrayList<>();
        
    for (int i = 1; i <= numberOfPlayers; i++)
    {
        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerPanel.setBackground(new Color(53, 101, 77));
        JLabel playerLabel = createLabel("Player " + i + " Name: ", 16, Color.YELLOW);
        JTextField nameField = new JTextField(10);  
        
        nameField.setMaximumSize(new Dimension(280, 30)); // Maximum width and height
        nameField.setHorizontalAlignment(JTextField.CENTER);
        
        nameFields.add(nameField);
        playerPanel.add(playerLabel);
        playerPanel.add(nameField);
        nameBox.add(playerPanel);
    }
    
        headerPanel.add(backButton);
        headerPanel.add(heading);
        startButton = createButton("Start Game");
        namePanel.add(headerPanel,BorderLayout.PAGE_START);
        namePanel.add(nameBox,BorderLayout.CENTER);
        namePanel.add(startButton,BorderLayout.PAGE_END);
        // Clear the current panel and add the new player panel
        switchToPanel(namePanel);
        clickBackButton(playPanel);
    }
    
    private JPanel betPanel()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(53, 101, 77));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        playersPanel = new JPanel();
        playersPanel.setBackground(new Color(53, 101, 77));
        
        JPanel placeBets = new JPanel();
        placeBets.setLayout(new BoxLayout(placeBets, BoxLayout.Y_AXIS));
        //betPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        placeBets.setBackground(new Color(53, 101, 77));
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Stack vertically
        centerPanel.setBackground(new Color(53, 101, 77));
        // Add label to show current player's turn
        playerPromptLabel.setForeground(Color.WHITE);
        playerPromptLabel.setFont(playerPromptLabel.getFont().deriveFont(Font.BOLD));
        playerPromptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        placeBets.add(playerPromptLabel);
    
        // Add the bet input field
        betField.setMaximumSize(new Dimension(100, 30)); // Adjust dimensions as needed
        betField.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the field
        placeBets.add(Box.createVerticalStrut(10));
        placeBets.add(betField);
    
        // Add button to submit bet
        betButton = createButton("Submit Bet");
        betButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button
        placeBets.add(Box.createVerticalStrut(10));
        betButton.addActionListener(e -> submitBet());
        placeBets.add(betButton);
        
        updatePlayerPanels(playersPanel);
        centerPanel.add(spacerPanel());
        centerPanel.add(placeBets);
        centerPanel.add(spacerPanel());
        centerPanel.add(playersPanel);
        backButton = createButton("Back to Home");
        headerPanel.add(backButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        //mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
        
        initializeBettingCycle();
        return mainPanel;  
          
    }
    
    private JPanel blackJackPanel()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(53, 101, 77));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(53, 101, 77));
        //JPanel playersPanel = new JPanel();
        playersPanel.setBackground(new Color(53, 101, 77));

        dealerPanel = new JPanel();
        dealerPanel.setLayout(new BoxLayout(dealerPanel, BoxLayout.Y_AXIS));
        dealerPanel.setBackground(new Color(53, 101, 77));
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Stack vertically
        centerPanel.setBackground(new Color(53, 101, 77));
        
        //hitButton = createButton("Hit");
        //stayButton = createButton("Stay");
        //doubleDownButton = createButton("Double Down");
        
        buttonPanel.add(hitButton);
        buttonPanel.add(stayButton);
        buttonPanel.add(doubleDownButton);
        
        updateDealerPanel(dealerPanel, controller);
        
        backButton = createButton("Back to Home");
        centerPanel.add(dealerPanel); // Add dealer panel first
        centerPanel.add(spacerPanel());
        centerPanel.add(spacerPanel());
        centerPanel.add(spacerPanel());
    
        updatePlayerPanels(playersPanel);
        centerPanel.add(playersPanel);
        
        headerPanel.add(backButton);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
        
        mainPanel.revalidate(); // Refresh the layout
        mainPanel.repaint();
        clickBacktoHomeButton(welcomePanel);
        return mainPanel;
    }
    
    private void updateDealerPanel(JPanel dealerPanel, Controller controller)
    {
        dealerPanel.removeAll(); // Clear previous content
        String dealer = "Dealer";
        JLabel dealerLabel = new JLabel(dealer);
        dealerLabel.setForeground(Color.WHITE);
        dealerLabel.setFont(dealerLabel.getFont().deriveFont(Font.BOLD));
        dealerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dealerPanel.add(dealerLabel);
        
        Dealer dealerObj = controller.getDealer();
        List<Card> dealerCards = dealerObj.getHand().getCards();

        if (dealerCards != null && !dealerCards.isEmpty())
        {
            for (Card card : dealerCards)
            {
                JLabel cardLabel = new JLabel(card.toString());
                cardLabel.setForeground(Color.WHITE);
                cardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                dealerPanel.add(cardLabel);
            }
        } 
        else
        {
            JLabel noCardsLabel = new JLabel("Dealer has no cards yet.");
            noCardsLabel.setForeground(Color.WHITE);
            dealerPanel.add(noCardsLabel);
        }

        dealerPanel.revalidate();
        dealerPanel.repaint();
    }
    
    private void updatePlayerPanels(JPanel playersPanel)
    {
        ArrayList<String> playerNames = model.getPlayerNames();
        List<ActualPlayer> players = Controller.players;
        
        System.out.println("Creating player panels...");
        if(playerNames!=null)
        {
            int numPlayers = playerNames.size();
            int totalWidth = 750; // Frame width
            int totalNameWidth = 0;
            for (String playerName : playerNames)
            {
                totalNameWidth += playerName.length() * 10; // Approximate width per character
            }
            int availableSpace = totalWidth - totalNameWidth;
            int spaceBetween = availableSpace / (numPlayers + 1); // +1 for space on each end
            
            playersPanel.removeAll();  // Ensure we start with a clean panel
            playersPanel.setLayout(new GridLayout(1, numPlayers, 10, 0)); // 1 row// numPlayers columsn// 10pixel horizontal gap
            Border normalBorder = new LineBorder(new Color(53, 101, 77), 2);
            Border turnBorder = new LineBorder(Color.YELLOW, 3);
            
            for(int i = 0; i< numPlayers; i++)
            {
                String playerName = playerNames.get(i);
                ActualPlayer player = players.get(i); // Get the corresponding ActualPlayer
                int balance = player.getBalance();
                
                JPanel playerPanel = new JPanel();
                playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS)); // Stack name and cards
                playerPanel.setBackground(new Color(53, 101, 77));
                if(betsComplete)
                {
                    if (i == controller.getPlayerIndex())
                    { // playerIndex is the current player's index
                        playerPanel.setBorder(turnBorder); // Yellow border for current turn
                    }    
                    else
                    {
                        playerPanel.setBorder(normalBorder); // Normal gray border
                    }
                }
                playerPanel.setMinimumSize(new Dimension(100, 80)); // Minimum width of 100 pixels
                playerPanel.setPreferredSize(new Dimension(120, 80));
                if(betsComplete)
                {
                    List<Card> cards = player.getHand().getCards(); // Get the player's cards
                    System.out.println(playerNames.get(i) + " has " + (cards != null ? cards.size() : 0) + " cards.");
                    for (Card card : cards)
                    {
                        JLabel cardLabel = new JLabel(card.toString()); // Assuming Card has a toString method to display it
                        cardLabel.setForeground(Color.WHITE);
                        cardLabel.setFont(cardLabel.getFont().deriveFont(Font.BOLD));
                        cardLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center card label
                        playerPanel.add(cardLabel);
                        System.out.println("displaying " + card.toString());
                    }                    
                }
                
                JLabel nameLabel = new JLabel(playerName);
                nameLabel.setForeground(Color.WHITE);
                nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                playerPanel.add(nameLabel);
                
                JLabel balanceLabel = new JLabel("$" + balance);
                balanceLabel.setForeground(Color.WHITE);
                balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
                balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                playerPanel.add(balanceLabel);
                
                playersPanel.add(playerPanel);   
            } 
        }
        playersPanel.revalidate();
        playersPanel.repaint();
        
    }
    
    private JLabel createLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setForeground(color);
        label.setFont(new Font("Serif", Font.BOLD, fontSize));
        return label;
    }
    
     private JButton createButton(String text) {
        return new JButton(text);    
    }
     
    private void clickBackButton(JPanel panel)
    {
        backButton.addActionListener(e -> {
            switchToPanel(panel);
        });
    }
     
    private void clickBacktoHomeButton(JPanel panel)
    {
        backButton.addActionListener(e -> {
            
            int response = JOptionPane.showConfirmDialog(
            null, 
            "Are you sure? All progress will be lost!", 
            "Return to Home Screen", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE            
        );
            if (response == JOptionPane.YES_OPTION)
            {
                resetGameState();
                switchToPanel(panel);
            }
        });
    } 
     
    private JPanel spacerPanel()
    {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(53, 101, 77));
        return panel;  
    }
    
    public void switchToPanel(JPanel newPanel) {
        setContentPane(newPanel);
        revalidate();
        repaint();
    }
    
    public JButton getRulesButton() {
        return rulesButton;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JButton getStatsButton() {
        return statsButton;
    }
    
    public JButton getSubmitButton()
    {
        return submitButton;
    }
    
    public JButton getStartButton()
    {
        return startButton;
    }
    
    public JButton getHitButton()
    {
        return hitButton;
    }
    
    public JButton getStayButton()
    {
        return stayButton;
    }
    public JButton getDoubleDownButton()
    {
        return doubleDownButton;
    }
    
    JPanel getRulesPanel() {
        
        return rulesPanel;
    }

    JPanel getStatsPanel() {
        
        return statsPanel;
    }
    
    JPanel getPlayPanel()
    {
        
        return playPanel;
    }
    
    JPanel getBlackJackPanel()
    {
        //betPanel = betPanel();
        blackJackPanel = blackJackPanel(); 
        
        return blackJackPanel;
    }
    
     JPanel getBetPanel()
    {
        betPanel = betPanel();
        return betPanel;
    }
    public JPanel getPlayersPanel()
    {
        return playersPanel;
    }
    public JPanel getDealerPanel()
    {
        return dealerPanel;
    }
    
    public void refreshPlayerPanels(JPanel playersPanel)
    {
        playersPanel.removeAll();
        updatePlayerPanels(playersPanel);
        centerPanel.revalidate(); // Refresh the layout of the centerPanel
        centerPanel.repaint(); 
        blackJackPanel.revalidate();
        blackJackPanel.repaint();
    }
    public void refreshDealerPanel(JPanel dealerPanel, Controller controller)
    {
        dealerPanel.removeAll();
        updateDealerPanel(dealerPanel, controller);
        centerPanel.revalidate(); // Refresh the layout of the centerPanel
        centerPanel.repaint(); 
        blackJackPanel.revalidate();
        blackJackPanel.repaint();   
    }
    
    public void setRulesText(String rules)
    {
        rulesTextArea.setText(rules);
    }
    
    private void addImageToPanel(JPanel panel, String imagePath) { // not used yet
        // Load the image
        ImageIcon imageIcon = new ImageIcon(imagePath);
        
        // Optionally, resize the image to fit the panel
        Image img = imageIcon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH); // You can change dimensions
        ImageIcon resizedIcon = new ImageIcon(img);
        
        // Create a label with the resized image
        JLabel imageLabel = new JLabel(resizedIcon);

        // Add the image label to the panel
        panel.add(imageLabel);
    }
    
    public int getSpinnerValue()
    {
        return (int) spinner.getValue();
    }
    
    public ArrayList<JTextField> getNameFields()
    {
        return nameFields;
    }
    
    private void resetGameState()
    {
        nameFields.clear();
        model.getDealer().clearHand();
        betPanel.removeAll();
        blackJackPanel.removeAll(); 
        // Optionally reset other game-related components or variables here, like scores or game state
    
        // Rebuild the Blackjack panel UI
        betPanel = betPanel();
        blackJackPanel = blackJackPanel();  // Reinitialize the Blackjack panel
    }
    
    private void initializeBettingCycle()
    {
        currentPlayerIndex = 0;
        promptNextPlayer(); // Prompt the first player
      
    }

    private void promptNextPlayer()
    {
        if(currentPlayerIndex < model.getNumOfPlayers())
        {
            String currentPlayerName = model.getPlayerNames().get(currentPlayerIndex);
            playerPromptLabel.setText(currentPlayerName + ", enter your bet: ");
            betField.setText(""); // Clear the previous input
            betField.requestFocus();
        }
        else
        {
            System.out.println("All bets collected. Proceed with the game.");// debugger

            System.out.println("Removed components from betPanel.");
            betPanel.revalidate();
            betPanel.repaint();
            
            currentPlayerIndex = 0;
            betsComplete = true;
 
            blackJackPanel = blackJackPanel();
            switchToPanel(blackJackPanel);
            //betsComplete = false;  
        }   
    }
    
    private void submitBet()
    {
        String betInput = betField.getText();
        int betAmount;
        try
        {
            betAmount = Integer.parseInt(betInput);

            // Ask the controller to handle the bet logic
            if (controller.processBet(currentPlayerIndex, betAmount))
            {
                currentPlayerIndex++; // Move to the next player
                promptNextPlayer();   // Prompt the next player
            } else
            {
                JOptionPane.showMessageDialog(null, "Invalid bet amount. Try again.");
            }
        }   catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a integer.");
            }
    }
    
    public void showPlayerBust(ActualPlayer player)
    {
        String message = player.getName() + " has busted!";
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Player Bust");
        dialog.setModal(false);
        
        Timer showDialogTimer = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.setVisible(true);
            
            // Create another timer to close the dialog after 1 second
            Timer closeDialogTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose(); // Close the dialog
                }
            });
            closeDialogTimer.setRepeats(false); // Ensure this timer only runs once
            closeDialogTimer.start(); // Start the timer to close the dialog
        }
        });
        showDialogTimer.setRepeats(false); // Ensure this timer only runs once
        showDialogTimer.start();
    }
    
    public void showDealerBustMessage()
    {
        String message = "Dealer has busted!";
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Dealer Bust");

        // Show the message dialog
        dialog.setVisible(true);

        // Automatically close the dialog after 1 second
        Timer closeDialogTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            dialog.dispose();
            }
        });
        closeDialogTimer.setRepeats(false);
        closeDialogTimer.start();
    }
    
}
