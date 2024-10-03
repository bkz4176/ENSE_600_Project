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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.*;


public class View extends JFrame {
    
    private JPanel welcomePanel;
    private JPanel statsPanel;
    private JPanel rulesPanel;
    private JPanel playPanel;
    private JPanel blackJackPanel;
    
    private JButton submitButton;
    private JButton statsButton;
    private JButton rulesButton;
    private JButton playButton;
    private JButton backButton;
    private JButton startButton;
    
    private GridBagConstraints gbc = new GridBagConstraints();
    private JTextArea rulesTextArea;
    private ArrayList<JTextField> nameFields;
    
    
    private JSpinner spinner;
    private String spacing = " ";
     
    public View()
    {
        setTitle("BlackJack Game");
        setSize(750, 550);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        setContentPane(welcomePanel);
        setVisible(true);
        
    }
    
    private void initializeComponents() {
        welcomePanel = welcomePanel();
        rulesPanel = rulesPanel();
        statsPanel = statsPanel();
        playPanel = playPanel();
        blackJackPanel = blackJackPanel();
       
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
    
    private JPanel blackJackPanel()
    {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(53, 101, 77));
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(53, 101, 77));
        
        
        backButton = createButton("Back to Home");
        headerPanel.add(backButton);
        mainPanel.add(headerPanel);
        clickBackButton(welcomePanel);
        return mainPanel;
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
        return blackJackPanel;
    }
    
    public void setRulesText(String rules)
    {
        rulesTextArea.setText(rules);
    }
    
    private void addImageToPanel(JPanel panel, String imagePath) {
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
}
