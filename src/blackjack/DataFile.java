package blackjack;
/**
 *
 * @author daniel
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFile {

    private static BufferedWriter writer;

    public static void createFile(String fileName)
    {
        try {
            writer = new BufferedWriter(new FileWriter(fileName, false));  // only saves the current game log. once a new game has started
            //the file will overwite with the current game. Can change false to true in writer to append games logs.
        } catch (IOException e) {
            System.out.println("An error occurred while initializing the logger: " + e.getMessage());
        }
    }

    public static void log(String data) // method to write game log to file
    { 
        try {
            writer.write(data);
            writer.newLine();
            writer.flush();  // Ensures data is written to the file immediately
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the log file: " + e.getMessage());
        }
    }

    public void close()//method to close writer
    {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while closing the log file: " + e.getMessage());
        }
    }

    //Method to witer player info file. ChatGpt assisted me in writing this method
    public static void playerInfo(List<ActualPlayer> players, String fileName)
    {
        Map<String, Integer> existingPlayerInfo = readPlayerInfo(fileName);
        for (ActualPlayer p : players) 
        {
            existingPlayerInfo.put(p.name, p.getBalance());
        }

        try (BufferedWriter playerInfo = new BufferedWriter(new FileWriter(fileName, false)))
        {
            playerInfo.write("Name, Bank Balance");
            playerInfo.newLine();
            playerInfo.newLine();
            for (Map.Entry<String, Integer> entry : existingPlayerInfo.entrySet())
            {
                playerInfo.write(entry.getKey() + ", " + entry.getValue());
                playerInfo.newLine();
               
            }
            playerInfo.flush();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read player information from the file. ChatGpt assisted me in writing this method
    public static Map<String, Integer> readPlayerInfo(String fileName)
    {
        Map<String, Integer> playerInfoMap = new HashMap<>();
        File file = new File(fileName);
    
        if (!file.exists())
        {
        return playerInfoMap;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            if ((line = reader.readLine()) != null) 
            {
                // Skip the header
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String name = parts[0];
                    int balance = Integer.parseInt(parts[1]); // Parse balance as integer
                    playerInfoMap.put(name, balance);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading player info from the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("An error occurred while parsing balance as an integer: " + e.getMessage());
        }
        return playerInfoMap;
    }
}
