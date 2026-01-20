package controllers;

import models.Registration;
import java.io.*;

public class RegistrationController {
    private final String FILE_PATH = "registrations.txt";

    public boolean saveRegistration(Registration reg) {
        // 'true' enables append mode so we don't overwrite previous students
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(reg.toFileString());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}