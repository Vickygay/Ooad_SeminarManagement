package controllers;

import views.LoginView;

public class LoginController {
    private LoginView view; // Corrected variable name

    // Constructor to initialize the controller with the view
    public LoginController(LoginView view) {
        this.view = view;
    }

    // Add method to handle login (this can be extended)
    public boolean validateLogin(String userID, String password) {
        // Example: Validate user input with hardcoded values
        if (userID.equals("admin") && password.equals("admin")) {
            // If login is successful, you can update the view (e.g., navigate to the next screen)
            view.showWelcomeScreen(); // Example method to show next screen
            return true;
        } else {
            // If login fails, you can show an error on the view
            view.showErrorMessage("Invalid login credentials!");
            return false;
        }
    }

    // More methods can be added to handle other logic
}