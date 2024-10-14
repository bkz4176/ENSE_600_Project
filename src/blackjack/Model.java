/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author daniel
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private final String rules;
    private int numOfPlayers = 0;
    private ArrayList<String> playerNames;
    private List<ActualPlayer> players;
    private final Dealer dealer;
    private Deck deck;

    public Model()
    {
        rules = loadRulesFromFile("Game_rules.txt");
        playerNames = new ArrayList<>();
        dealer = new Dealer();
        this.deck = new Deck();
    }

    private String loadRulesFromFile(String fileName)
    {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                content.append(line).append("\n");
            }
        } catch (IOException e)
        {
            content.append("Error reading file: ").append(e.getMessage());
        }
        return content.toString();
    }

    public String getRules()
    {
        return rules;
    }
    
    public int getNumOfPlayers()
    {
        return numOfPlayers;
    }
    
    public void setNumOfPlayer(int x)
    {
        this.numOfPlayers = x;
    }
    
    public ArrayList<String> getPlayerNames()
    {
        return playerNames;
    }

    public void setPlayerNames(ArrayList<String> names)
    {
        playerNames.clear();
        this.playerNames = names;
    }
    
    public void setPlayers(List<ActualPlayer> players)
    {
        players.clear();
        this.players = players;
    }
    
    public List<ActualPlayer> getPlayers()
    {
        return players;
    }
    public Dealer getDealer()
    {
        return dealer;
    }
    public Deck getDeck()
    {
        return deck; // Getter for the deck
    }
  
}