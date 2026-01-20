package models;

public class Registration {
    private String title;
    private String abstractText;
    private String supervisor;
    private String type;
    private String filePath; // New field for materials 

    public Registration(String title, String abstractText, String supervisor, String type, String filePath) {
        this.title = title;
        this.abstractText = abstractText;
        this.supervisor = supervisor;
        this.type = type;
        this.filePath = filePath;
    }

    public String toFileString() {
        // Updated to include file path in the saved data
        return title + "|" + abstractText + "|" + supervisor + "|" + type + "|" + filePath;
    }
}