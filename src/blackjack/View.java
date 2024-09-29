/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author daniel
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class View extends JFrame {
    private JPanel welcomePanel;
    private JPanel statsPanel;
    private JPanel rulesPanel;
    private JPanel playersPanel;
    private JTextArea rulesTextArea;
    private JButton gameRulesButton;
    private JButton playGameButton;
    private JButton playerStatsButton;
    private JButton backButton;
    private JButton submitButton;
    private JSpinner spinner;
    private List<JTextField> playerNameFields;
    private int numberOfPlayers;
    private JPanel playerNames;
    private ArrayList<String> names = new ArrayList<>();
    private JButton submitNamesButton;
    
    public View() {
        setTitle("BlackJack Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        names = new ArrayList<>();
        initializeComponents();
        setContentPane(welcomePanel);
        setVisible(true);
        
    }

    private void initializeComponents() {
        welcomePanel = createWelcomePanel();
        statsPanel = createStatsPanel();
        rulesPanel = createRulesPanel();
        playersPanel = createPlayersPanel();
  
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBackground(new Color(53, 101, 77));

        JLabel welcomeLabel = createLabel("Welcome to Blackjack!", 32, Color.YELLOW);
        panel.add(welcomeLabel);
        JPanel spacerPanel = new JPanel(); // Spacer Panel
        spacerPanel.setBackground(new Color(53,101,77));
        panel.add(spacerPanel); // Add spacer panel to the main panel

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(53, 101, 77));

        gameRulesButton = createButton("Game Rules");
        playGameButton = createButton("Play Game"); // Placeholder for play game functionality
        playerStatsButton = createButton("Player Stats");

        buttonPanel.add(gameRulesButton);
        buttonPanel.add(playGameButton);
        buttonPanel.add(playerStatsButton);
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(53, 101, 77));
        
        JLabel statsLabel = createLabel("Player Stats", 32, Color.YELLOW);
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
       
        backButton = createButton("Back to Menu");
        headerPanel.add(backButton);
        headerPanel.add(statsLabel);
        panel.add(headerPanel, BorderLayout.NORTH);
        clickBackButton();

        return panel;
    }

    private JPanel createRulesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(53, 101, 77));

        JLabel rulesLabel = createLabel("Game Rules", 32, Color.YELLOW);
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        
        backButton = createButton("Back to Menu");
        headerPanel.add(backButton);
        headerPanel.add(rulesLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        rulesTextArea = new JTextArea();
        rulesTextArea.setEditable(false);
        rulesTextArea.setBackground(new Color(53, 101, 77));
        rulesTextArea.setForeground(Color.WHITE);
        rulesTextArea.setFont(new Font("Serif", Font.PLAIN, 16));
        panel.add(new JScrollPane(rulesTextArea), BorderLayout.CENTER);
        clickBackButton();

        return panel;
    }
    
    private JPanel createPlayersPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(53, 101, 77));
        
        
        JLabel playersLabel = createLabel("Enter the amount of Players (1-7)", 32, Color.YELLOW);
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        
        backButton = createButton("Back to Menu");
        
        SpinnerModel model = new SpinnerNumberModel(1, 1, 7, 1); // Initial value, min, max, step
        spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(60, 30));
        
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        spinnerPanel.setBackground(new Color(53, 101, 77)); // Keep consistent background
        spinnerPanel.add(spinner);
        
        submitButton = createButton("Submit");
        spinnerPanel.add(submitButton);
        
        headerPanel.add(backButton);
        headerPanel.add(playersLabel);
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(spinnerPanel, BorderLayout.CENTER);
        clickBackButton();
        clickSubmitButton();
        
        playerNames = new JPanel();
        playerNames.setLayout(new BoxLayout(playerNames, BoxLayout.Y_AXIS));
        panel.add(playerNames, BorderLayout.SOUTH);
        
        playerNameFields = new ArrayList<>();
        return panel;
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

    public void setRulesText(String rules) {
        rulesTextArea.setText(rules);
    }

    public void switchToPanel(JPanel newPanel) {
        setContentPane(newPanel);
        revalidate();
        repaint();
    }

    public JButton getGameRulesButton() {
        return gameRulesButton;
    }

    public JButton getPlayGameButton() {
        return playGameButton;
    }

    public JButton getPlayerStatsButton() {
        return playerStatsButton;
    }
   
    public JButton getBackButton() {
        return backButton;
    }
    
    public JButton getSubmitButton() {
        return submitButton;
    }

    JPanel getRulesPanel() {
        return rulesPanel;
    }

    JPanel getStatsPanel() {
        return statsPanel;
    }
    
    JPanel getPlayersPanel()
    {
        return playersPanel;
    }
    
    private void clickBackButton()
    {
         backButton.addActionListener(e -> {
         switchToPanel(welcomePanel);
        });
    }
    
    private void clickSubmitButton()
    {
            submitButton.addActionListener(e ->{
            numberOfPlayers = (int) spinner.getValue();
            //ActualPlayer.initializePlayers(numberOfPlayers);
            createPlayerNameFields(numberOfPlayers);
            
        });
        
    }
    private void clickSubmitNameButton()
    {
        submitNamesButton.addActionListener(e -> {
            for(JTextField field : playerNameFields)
            {
                String playerName = field.getText();  // Get text from the text field
                if (!playerName.trim().isEmpty()) {  // Check if the name is not empty
                    names.add(playerName);  // Add the name to the ArrayList
                }
                System.out.println("Player Names: " + names);
            }
            ActualPlayer.initializePlayers(names);
        });
    }
    
    
    public int getNumberOfPlayers()
    {
        return numberOfPlayers;
    }
    
    public void createPlayerNameFields(int numberOfPlayers) {
    playerNames.removeAll();  // Clear existing fields
    playerNameFields.clear();

    // Create text fields for player names
    for (int i = 1; i <= numberOfPlayers; i++) {
        JTextField playerNameField = new JTextField();
        playerNameField.setPreferredSize(new Dimension(80, 30));
        playerNames.add(new JLabel("Player " + i + " Name:"));
        playerNames.add(playerNameField);
        playerNameFields.add(playerNameField);  
    }
    
    // Create the submit button for names
    submitNamesButton = createButton("Submit Player Names");
    playerNames.add(submitNamesButton);

    // Add the action listener to the button
    clickSubmitNameButton();

    // Refresh the panel to show updated fields
    playerNames.revalidate();
    playerNames.repaint();
}
    
    
}