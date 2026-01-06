package models;

public class Student {
    private String id;
    private String name;
    private String title;
    private String abstractText;
    private String supervisorName;
    private String presentationType;

    public Student(String id, String name, String title, String abstractText, String supervisorName,
            String presentationType) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.abstractText = abstractText;
        this.supervisorName = supervisorName;
        this.presentationType = presentationType;
    }

    public void registerPresentation() {
        // Logic to register the presentation, maybe save to a database or file
        System.out.println("Presentation Registered: " + title);
        System.out.println("Student ID: " + id);
        System.out.println("Student Name: " + name);
        System.out.println("Supervisor: " + supervisorName);
        System.out.println("Abstract Text: " + abstractText);
        System.out.println("Presentation Type: " + presentationType);
    }

    // Setters and getters for the fields
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public void setPresentationType(String presentationType) {
        this.presentationType = presentationType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getPresentationType() {
        return presentationType;
    }
}
