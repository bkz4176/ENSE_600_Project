/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package blackjack;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author daniel
 */
public class BlackJackTestTest {
    
    private Model model;
    private Connection connection;
    
    public BlackJackTestTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        model = new Model();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAddPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add("Alice");
        playerNames.add("Bob");
        
        model.setPlayerNames(playerNames);
        
        List<String> addedNames = model.getPlayerNames();
        assertEquals(2, addedNames.size());
        assertTrue(addedNames.contains("Alice"));
        assertTrue(addedNames.contains("Bob"));
    }

    @Test
    public void testAddEmptyPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add("");  // Adding an empty name
    
        try {
            model.setPlayerNames(playerNames);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Match the exact message thrown by the method
            assertEquals("Player names cannot be null or empty", e.getMessage());
        }
    }
    
    @Test
    public void testSetBetAmount()
    {
        ActualPlayer player = new ActualPlayer("Test1");  // Assuming you have a Player class
    
        boolean result = player.getBankAccount().placeBet(50);
        assertTrue(result);  // Check that the bet was successful

        result = player.getBankAccount().placeBet(-50);
        assertFalse(result);  // Check that the bet failed
    }
    
    @Test
    public void testAddNewPlayerToDatabase()
    {
        // Create a player and add to the database
        String playerName = "Test";
        int balance = 100;
        Date joinDate = new Date(System.currentTimeMillis());

        model.addPlayer(playerName, balance, joinDate); // Assuming this method adds a player to the DB

    }
    
    public void testSaveMultiplePlayerStats()
    
    {
        // Step 1: Add two test players to the database
        String playerName1 = "Test";
        String playerName2 = "Test1";
        int initialBalance = 100;
        Date joinDate = new Date(System.currentTimeMillis());
        model.addPlayer(playerName1, initialBalance, joinDate);
        model.addPlayer(playerName2, initialBalance, joinDate);
    
        // Step 2: Create two ActualPlayer objects and update their stats
        ActualPlayer player1 = new ActualPlayer(playerName1);
        player1.getBankAccount().placeBet(50);
        player1.addWinnings(100);
        player1.incrementGamesPlayed();
        player1.incrementGamesWon();
        player1.setTotalEarnings();

        ActualPlayer player2 = new ActualPlayer(playerName2);
        player2.getBankAccount().placeBet(30);
        player2.incrementGamesPlayed();
        player2.incrementGamesLost();
    
        // Add players to the list for updating
        List<ActualPlayer> playersToUpdate = new ArrayList<>();
        playersToUpdate.add(player1);
        playersToUpdate.add(player2);
    
        // Step 3: Call savePlayerStats to update both players in the database
        model.savePlayerStats(playersToUpdate);
    
        // Step 4: Manually query the database for both players
        String sql = "SELECT Balance, TotalEarnings, GamesPlayed, GamesWon, GamesLost, GamesDrawn, CurrentWinStreak "
                 + "FROM players WHERE Name = ?";
    
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Check player 1's stats
            pstmt.setString(1, playerName1);  // Set player 1 name
            try (ResultSet rs1 = pstmt.executeQuery()) {
                if (rs1.next()) {
                    int balance1 = rs1.getInt("Balance");
                    int totalEarnings1 = rs1.getInt("TotalEarnings");
                    int gamesPlayed1 = rs1.getInt("GamesPlayed");
                    int gamesWon1 = rs1.getInt("GamesWon");
                    int gamesLost1 = rs1.getInt("GamesLost");
                    int gamesDrawn1 = rs1.getInt("GamesDrawn");
                    int currentStreak1 = rs1.getInt("CurrentWinStreak");
                
                    // Step 5: Assert that player 1's updated values match the expected ones
                    assertEquals(150, balance1);
                    assertEquals(50, totalEarnings1);
                    assertEquals(1, gamesPlayed1);
                    assertEquals(1, gamesWon1);
                    assertEquals(0, gamesLost1);
                    assertEquals(0, gamesDrawn1);
                    assertEquals(1, currentStreak1);
                } else {
                fail("Player 1 not found in the database.");
                }
            }
        
            // Check player 2's stats
            pstmt.setString(1, playerName2);  // Set player 2 name
            try (ResultSet rs2 = pstmt.executeQuery()) {
                if (rs2.next()) {
                    int balance2 = rs2.getInt("Balance");
                    int totalEarnings2 = rs2.getInt("TotalEarnings");
                    int gamesPlayed2 = rs2.getInt("GamesPlayed");
                    int gamesWon2 = rs2.getInt("GamesWon");
                    int gamesLost2 = rs2.getInt("GamesLost");
                    int gamesDrawn2 = rs2.getInt("GamesDrawn");
                    int currentStreak2 = rs2.getInt("CurrentWinStreak");
                
                    // Step 6: Assert that player 2's updated values match the expected ones
                    assertEquals(70, balance2);  // Bet of 30 placed
                    assertEquals(0, totalEarnings2);  // No winnings
                    assertEquals(1, gamesPlayed2);
                    assertEquals(0, gamesWon2);
                    assertEquals(1, gamesLost2);
                    assertEquals(0, gamesDrawn2);
                    assertEquals(0, currentStreak2);  // Lost the game, so no win streak
                } else {
                    fail("Player 2 not found in the database.");
                }
            }
        
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Database query failed.");
        }
    }
 
}
