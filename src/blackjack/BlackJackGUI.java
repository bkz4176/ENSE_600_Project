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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class BlackJackGUI extends JFrame {
    
        public BlackJackGUI() {
        setTitle("BlackJack Game");
        setSize(800, 600); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window on screen
        setContentPane(welcomePanel());
        
        setVisible(true);
        
        
        }
        
        public JPanel welcomePanel()
        {
            JPanel panel = new JPanel();
            panel.setBackground(new Color(53,101,77));
            panel.setLayout(new GridLayout(3, 1, 10, 10));
        
            JLabel welcomeLabel = new JLabel("Welcome to Blackjack!",JLabel.CENTER);
            welcomeLabel.setForeground(Color.YELLOW); // Set the text color to yellow
            welcomeLabel.setFont(new Font("Serif", Font.BOLD, 32)); // Set the font size and style
            panel.add(welcomeLabel);
            
            JPanel spacerPanel = new JPanel(); // Spacer Panel
            spacerPanel.setBackground(new Color(53,101,77));
            panel.add(spacerPanel); // Add spacer panel to the main panel
            
            JPanel buttonPanel = new JPanel();
            
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
            buttonPanel.setBackground(new Color(53,101,77));
          
            JButton gameRulesButton = new JButton("Game Rules");
            JButton playGameButton = new JButton("Play Game");            
            JButton playerStatsButton = new JButton("Player Stats");
            
            buttonPanel.add(gameRulesButton);
            buttonPanel.add(playGameButton);
            buttonPanel.add(playerStatsButton);
      
            playerStatsButton.addActionListener(e -> {
            // Switch to stats panel
            setContentPane(statsPanel());
            revalidate(); // Revalidate the frame
            repaint(); // Repaint the frame
            });
            
            gameRulesButton.addActionListener(e -> {
            // Switch to stats panel
            setContentPane(rulesPanel());
            revalidate(); // Revalidate the frame
            repaint(); // Repaint the frame
            });
            
            panel.add(buttonPanel);
        
            setContentPane(panel);
            return panel;
        }
        
        
    public JPanel statsPanel()
    {
        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN);
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel statsLabel = new JLabel("Player Stats", JLabel.CENTER);
        statsLabel.setForeground(Color.YELLOW); // Set the text color to yellow
        statsLabel.setFont(new Font("Serif", Font.BOLD, 32)); // Set the font size and style
        panel.add(statsLabel);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
        // Switch back to welcome panel
        setContentPane(welcomePanel());
        revalidate(); // Revalidate the frame
        repaint(); // Repaint the frame
        });
        panel.add(backButton);

        return panel;
    }
    
    public JPanel rulesPanel()
    {
        String width = " ";
        JPanel panel = new JPanel();
        panel.setBackground(new Color(53, 101, 77));
        panel.setLayout(new BorderLayout()); // Use BorderLayout for the main panel

        // Create a header panel for the title and back button
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout for left alignment
        headerPanel.setBackground(new Color(53, 101, 77));

        // Create the back button
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> {
        // Switch back to welcome panel
        setContentPane(welcomePanel());
        revalidate(); // Revalidate the frame
        repaint(); // Repaint the frame
        });

        headerPanel.add(backButton); // Add the back button to the header panel

        // Create the rules label
        JLabel rulesLabel = new JLabel(width.repeat(25)+"Game Rules");
        rulesLabel.setForeground(Color.YELLOW); // Set the text color to yellow
        rulesLabel.setFont(new Font("Serif", Font.BOLD, 32));

        // Add the heading to the header panel as well
        headerPanel.add(rulesLabel); // Add the rules label to the header panel

        // Add the header panel to the main panel (NORTH)
        panel.add(headerPanel, BorderLayout.NORTH); 
        
        JTextArea rulesTextArea = new JTextArea();
        rulesTextArea.setEditable(false); // Make it read-only
        rulesTextArea.setBackground(new Color(53, 101, 77));
        rulesTextArea.setForeground(Color.WHITE);
        rulesTextArea.setFont(new Font("Serif", Font.PLAIN, 16));

        // Load the rules from a text file
        String rules = loadRulesFromFile("Game_rules.txt");
        rulesTextArea.setText(rules); // Set the text area to display the rules

        panel.add(new JScrollPane(rulesTextArea), BorderLayout.CENTER); // Scrollable area for the rules

        return panel;
    }
    
    private String loadRulesFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n"); // Add each line to the content
            }
        } catch (IOException e) {
            content.append("Error reading file: ").append(e.getMessage());
        }
        return content.toString(); // Return the full content as a string
    }
        
        
}
