package models;

public class Evaluator extends User {
    // Constructor
    public Evaluator(String userID, String name, String email, String password) {
        super(userID, name, email, password);
    }
    
    // You can add setters if you want to allow changing details
    public void setEmail(String email) {
        super.setEmail(email);
    }
}