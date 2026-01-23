package models;

public class Student extends User {
    // We REMOVE 'id' and 'name' because they are now in the User class
    
    // Presentation details specific to the Student
    private String title;
    private String abstractText;
    private String supervisorName;
    private String presentationType;

    // Constructor: Must include Email and Password to satisfy the Parent (User)
    public Student(String id, String name, String email, String password) {
        super(id, name, email, password); // Pass data to the User class
    }

    public void registerPresentation() {
        // Logic to register the presentation
        // Note: We use getters (getUserID, getName) from the parent User class
        System.out.println("Presentation Registered: " + title);
        System.out.println("Student ID: " + getUserID());
        System.out.println("Student Name: " + getName());
        System.out.println("Supervisor: " + supervisorName);
        System.out.println("Abstract Text: " + abstractText);
        System.out.println("Presentation Type: " + presentationType);
    }

    // --- Setters and Getters for Presentation details ---
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setPresentationType(String presentationType) {
        this.presentationType = presentationType;
    }

    public String getPresentationType() {
        return presentationType;
    }
}