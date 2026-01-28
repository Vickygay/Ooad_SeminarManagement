package models;

import java.util.*;
import java.io.*;

public class UserDatabase {
    private static Map<String, User> users = new HashMap<>();

    // Load user information from a file when called
    public static void loadUsersFromFile() {
        System.out.println("Opening user.txt file for reading..."); // Debug message when starting to read the file

        try {
            // Debugging: Print the current working directory to check the path
            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            // Use relative path to point to user.txt (ensure it's placed in the root folder, not in src)
            File userFile = new File("../user.txt"); // Adjust the path here (go one level up)

            // Check if the file exists
            if (!userFile.exists()) {
                System.out.println("File not found: " + userFile.getPath());
                return; // Exit if the file doesn't exist
            } else {
                System.out.println("File found: " + userFile.getPath());
            }

            // Open the file for reading
            BufferedReader br = new BufferedReader(new FileReader(userFile));
            String line;
            int lineNumber = 1; // To track the line number in the file

            // Read each line from the file
            while ((line = br.readLine()) != null) {
                System.out.println("Reading line " + lineNumber + ": " + line); // Debug message for each line being read

                // Split the line by comma
                String[] userData = line.split(",");

                // Ensure the line contains exactly 4 parts
                if (userData.length == 4) {
                    String userID = userData[0];
                    String name = userData[1];
                    String email = userData[2];
                    String password = userData[3];

                    users.put(userID, new User(userID, name, email, password));

                    System.out.println("Added user: " + userID); // Debug message for adding user to the map
                } else {
                    System.out.println("Skipping invalid line " + lineNumber + ": " + line); // Debug message for invalid lines
                }

                lineNumber++; // Increment line number
            }

            br.close(); // Close the BufferedReader after reading the file
        } catch (IOException e) {
            System.out.println("Error while reading the file: " + e.getMessage()); // Error message if something goes wrong
            e.printStackTrace();
        }

        System.out.println("Finished loading users from file."); // Debug message when file loading is done
    }

    // Fetch a user by userID
    public static User getUser(String userID) {
        System.out.println("Fetching user with ID: " + userID); // Debug message when fetching user by ID
        User user = users.get(userID);

        if (user != null) {
            System.out.println("User found: " + user.getName()); // Debug message if user is found
        } else {
            System.out.println("User not found for ID: " + userID); // Debug message if user is not found
        }

        return user; // Retrieve the user object based on userID
    }

    // Getter for users map
    public static Map<String, User> getUsers() {
        return users; // Return the map containing all users
    }
}
