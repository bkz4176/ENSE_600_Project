/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack;

/**
 *
 * @author daniel
 */
//import com.sun.jdi.connect.spi.Connection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;

public class Model {
    // Initilize attributes of the model class
    private int numOfPlayers = 0;
    private ArrayList<String> playerNames;
    private List<ActualPlayer> players;
    private final Dealer dealer;
    private final Deck deck;
    
    
    private static final String DB_URL = "jdbc:derby:myBlackjackDB;create=true"; // Database URL
    private Connection connection;

    public Model()
    {
        playerNames = new ArrayList<>();
        dealer = new Dealer();
        this.deck = new Deck();
        try {
            this.connection = DriverManager.getConnection(DB_URL);
            System.out.println(DB_URL + " Get Connected Successfully ....");
            //dropPlayersTable(); // use if need to remove all players from database
            createTables(); // Create tables if they don't exist
            createRulesTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void shutdownDatabase()
    {
        try {
            // Close existing connections if any
            if (connection != null)
            {
                connection.close();
            }
            // Shutdown Derby
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            System.out.println("Database shut down successfully.");
            } catch (SQLException e) {
            // Handle the exception for shutdown attempt
            if ("XJ015".equals(e.getSQLState()))
            {
                System.out.println("Derby shutdown successfully.");
            }
            else
            {
                e.printStackTrace();
            }
        }
    }
    
    public int getNumOfPlayers() // getter for number of players
    {
        return numOfPlayers;
    }
    
    public void setNumOfPlayer(int x) // setter for number of players
    {
        this.numOfPlayers = x;
    }
    
    public ArrayList<String> getPlayerNames() // getter for player names
    {
        return playerNames;
    }

    public void setPlayerNames(ArrayList<String> names) //setter for player names
    {
        for (String name : names)
        {
            if (name == null || name.trim().isEmpty())
            {
                throw new IllegalArgumentException("Player names cannot be null or empty");
            }
        }
        this.playerNames = names;
    }
    
    public void setPlayers(List<ActualPlayer> players) // setter for players
    {
        //players.clear();
        this.players = players;
        playerNames.clear();
        for(ActualPlayer p : players)
        {
            playerNames.add(p.getName());
        }   
    }
    
    public List<ActualPlayer> getPlayers() // getter for players
    {
        return players;
    }
    public Dealer getDealer() // getter for dealer
    {
        return dealer;
    }
    public Deck getDeck() 
    {
        return deck; // Getter for the deck
    }
    
    private void createTables() throws SQLException {// table creation
        String createPlayersTable = "CREATE TABLE Players ("
                + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "Name VARCHAR(50) UNIQUE, "
                + "Balance INT, "
                + "TotalEarnings INT, "
                + "GamesPlayed INT, "
                + "GamesWon INT, "
                + "GamesLost INT, "
                + "GamesDrawn INT, "
                + "CurrentWinStreak INT, "
                + "JoinDate DATE)";
        
        try (Statement stmt = connection.createStatement())
        {
            stmt.execute(createPlayersTable);
            System.out.println("Players table created successfully.");
        } 
        catch (SQLException e)
        {
            // Catch exception if the table already exists
            if (e.getSQLState().equals("X0Y32")) // Table already exists code
            {  
                System.out.println("Table already exists, skipping creation.");
            }
            else
            {
                throw e;
            }
        }
    }
    
    private void createRulesTable() throws SQLException { // table creation
        String checkTableExists = "SELECT COUNT(*) FROM SYS.SYSTABLES WHERE TABLENAME = 'RULES'";

        try (Statement checkStmt = connection.createStatement();
        ResultSet rs = checkStmt.executeQuery(checkTableExists))
        {
            if (rs.next() && rs.getInt(1) > 0)
            {
                System.out.println("Rules table already exists.");
                return; // Exit if the table exists
            }
        }
        
        String createRulesTable = "CREATE TABLE Rules ("
                + "ID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                + "RuleText CLOB)";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createRulesTable);
            System.out.println("Rules table created successfully.");
            insertRules();
        } catch (SQLException e) {
                System.err.println("Error creating Rules table: " + e.getMessage());
        }
        
    }
    
    public void clearPlayersTable() throws SQLException 
    {
        String deleteSQL = "DELETE FROM Players";
        try (Statement stmt = connection.createStatement())
        {
            stmt.executeUpdate(deleteSQL);
            System.out.println("All players and stats cleared successfully.");
        } catch (SQLException e)
        {
            System.err.println("Error while clearing the Players table: " + e.getMessage());
        }
    }
    
    private void dropPlayersTable() throws SQLException // method to drop players table if needed.
    {
        String dropTableSQL = "DROP TABLE Players";
        try (Statement stmt = connection.createStatement())
        {
            stmt.execute(dropTableSQL);
            System.out.println("Players table dropped successfully.");
        } catch (SQLException e)
        {
            System.err.println("Error while dropping the Players table: " + e.getMessage());
        }
    }
    
    public void addPlayer(String name, int balance, Date joinDate) // method to add a new player to the database
    {
        String checkPlayerSQL = "SELECT COUNT(*) FROM Players WHERE Name = ?";
        String insertPlayerSQL = "INSERT INTO Players (Name, Balance, TotalEarnings, GamesPlayed, GamesWon,"
                             + " GamesLost, GamesDrawn, CurrentWinStreak, JoinDate) VALUES (?, ?, 0, 0, 0, 0, 0, 0, ?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkPlayerSQL))
        {
            checkStmt.setString(1, name);
            ResultSet rs = checkStmt.executeQuery();
        
            if (rs.next() && rs.getInt(1) > 0)
            {
                System.out.println("Player " + name + " already exists in the database. Skipping addition.");
                return;  // Exit the method if the player already exists
            }
            } catch (SQLException e)
            {
                e.printStackTrace();
                return;  // Exit the method if there is an error in checking
        }

        // Proceed to insert the player if the name does not exist
        try (PreparedStatement pstmt = connection.prepareStatement(insertPlayerSQL))
        {
            pstmt.setString(1, name);
            pstmt.setInt(2, balance);
            pstmt.setDate(3, new java.sql.Date(joinDate.getTime()));
            pstmt.executeUpdate();
            System.out.println("Player " + name + " added to database.");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //Chat gpt assisted me with creating initializePlayers method
    public List<ActualPlayer> initializePlayers(ArrayList<String> names) throws SQLException // method to intitlize players from user input to play the game
    {
        List<ActualPlayer> players = new ArrayList<>();
        // Updated query to include all relevant player stats
        String query = "SELECT Balance, TotalEarnings, GamesPlayed, GamesWon, GamesLost, GamesDrawn, CurrentWinStreak FROM Players WHERE Name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (String name : names)
            {
                pstmt.setString(1, name);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next())
                    { // Player exists in the database
                        int balance = rs.getInt("Balance");
                        int totalEarnings = rs.getInt("TotalEarnings");
                        int gamesPlayed = rs.getInt("GamesPlayed");
                        int gamesWon = rs.getInt("GamesWon");
                        int gamesLost = rs.getInt("GamesLost");
                        int gamesDrawn = rs.getInt("GamesDrawn");
                        int currentStreak = rs.getInt("CurrentWinStreak");

                        if (balance == 0)
                        {
                            System.out.println("Welcome back, " + name + "! A new balance has been loaded for you.");
                            //players.add(new ActualPlayer(name)); // Initialize with default balance
                            players.add(new ActualPlayer(name, 100, totalEarnings, gamesPlayed, gamesWon, gamesLost, gamesDrawn, currentStreak));
                        } 
                        else
                        {
                            System.out.println("Welcome back, " + name + "! Your balance has been loaded.");
                            players.add(new ActualPlayer(name, balance, totalEarnings, gamesPlayed, gamesWon, gamesLost, gamesDrawn, currentStreak)); // Initialize with the existing balance and stats
                            System.out.println(name + " balance = " + balance);
                        }
                    } 
                    else
                    { // New player
                        System.out.println("Welcome new Player: " + name);
                        Date currentDate = new Date(System.currentTimeMillis());
                        addPlayer(name, 100, currentDate); // Add player with initial balance
                        players.add(new ActualPlayer(name)); // New player
                    }
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
        }
        return players;
    }
    
    public void savePlayerStats(List <ActualPlayer> player) // method to update and save player stats to the database after each round
    {
        String sql = "UPDATE players SET Balance = ?, TotalEarnings = ?, GamesPlayed = ?, GamesWon = ?,"
                     + " GamesLost = ?, GamesDrawn = ?, CurrentWinStreak = ? WHERE Name = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (ActualPlayer p : player)
            {
                pstmt.setInt(1, p.getBalance());
                pstmt.setInt(2, p.getTotalEarnings());
                pstmt.setInt(3, p.getGamesPlayed());
                pstmt.setInt(4, p.getGamesWon());
                pstmt.setInt(5, p.getGamesLost());
                pstmt.setInt(6, p.getGamesDrawn());
                pstmt.setInt(7, p.getCurrentStreak());
                pstmt.setString(8, p.getName()); 

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0)
                {
                    System.out.println("Player stats updated successfully.");
                }
                else
                {
                    System.out.println("No player found with that name.");
                }
            }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
    }
    
    public List<String> getPlayerStats() throws SQLException { // method to get all players stat and display them in the stats panel
        List<String> statsList = new ArrayList<>();
    
        // Example SQL query to fetch selective data
        String query = "SELECT Name, Balance, TotalEarnings, GamesPlayed, GamesWon, GamesLost, GamesDrawn, CurrentWinStreak, JoinDate FROM Players";  
    
        final String format = "%-15s | %12s | %15s | %13s | %10s | %11s | %13s | %20s | %-10s";
        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            String header = String.format(format,
            "Player", "Balance", "Total Earnings", "Games Played",
            "Games Won", "Games Lost", "Games Drawn", "Current Win Streak", "Join Date"
        );
        statsList.add(header);
        statsList.add("-".repeat(header.length())); // separator line

            
             while (rs.next()) {
            String name = rs.getString("Name");
            String balance = String.format("$%,d", rs.getInt("Balance"));
            String totalEarnings = String.format("$%,d", rs.getInt("TotalEarnings"));
            String gamesPlayed = String.valueOf(rs.getInt("GamesPlayed"));
            String gamesWon = String.valueOf(rs.getInt("GamesWon"));
            String gamesLost = String.valueOf(rs.getInt("GamesLost"));
            String gamesDrawn = String.valueOf(rs.getInt("GamesDrawn"));
            String currentWinStreak = String.valueOf(rs.getInt("CurrentWinStreak"));
            String joinDate = rs.getDate("JoinDate") != null ? rs.getDate("JoinDate").toString() : "N/A";

            String stat = String.format(format,
                name, balance, totalEarnings, gamesPlayed,
                gamesWon, gamesLost, gamesDrawn, currentWinStreak, joinDate);

            statsList.add(stat);
        }
        }   
        return statsList;
    }
    
    private void insertRules() throws SQLException { // rules insertion into the rules table
        String rules = "Rules of the Game:\n\n" +
                   "Each player must make a bet to play the game.\n" +
                   "Each player is dealt two cards. The dealer is dealt two cards with one face up and one face down.\n" +
                   "Cards are equal to their value with face cards being 10 and an Ace being 1 or 11.\n" +
                   "The players cards are added up for their hand total.\n" +
                   "Players have 3 options:\n" +
                   "    'hit' to gain another card from the deck \n" +
                   "    'stand' to keep their current hand\n" +
                   "    'double' Player gains one more card, then automatically 'stands'. The players Initial\n" +
                   "    bet is doubled. Doubling is only applicable on players first turn.\n" +
                   "Dealer 'hits' until the dealers hand equals or exceeds 17\n" +
                   "The aim is to have a higher hand total than the dealer without exceeding 21\n" +
                   "The player loses if the hand total is greater than 21 or less than the dealer hand\n" +
                   "If player and dealer hand total are equal, its a 'push' (draw). Initial bet is returned.\n" +
                   "If player hand exceeds 21 and dealer hand exceeds 21 then the player loses.";

        String insertRules = "INSERT INTO Rules (RuleText) VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertRules)) {
            pstmt.setString(1, rules);
            pstmt.executeUpdate();
            System.out.println("Rules inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting rules: " + e.getMessage());
        }
    }
    
    public String getRules() throws SQLException { // method to display rules into the rules panel.
        String rules = null;
        String selectRules = "SELECT RuleText FROM Rules";

        try (Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectRules)) {
            if (rs.next()) {
                rules = rs.getString("RuleText");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving rules: " + e.getMessage());
        }
        return rules;
    }
}