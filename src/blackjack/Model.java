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

public class Model {
    private String rules;
    private int numOfPlayers = 0;
    private ArrayList<String> playerNames;

    public Model()
    {
        rules = loadRulesFromFile("Game_rules.txt");
        playerNames = new ArrayList<>();
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
        this.playerNames = names;
    }
}