package usermanager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import database.UserDAO;
import database.UserDAOImpl;
import datahandler.Encryptor;
import exception.UserNotFoundException;
import ui.SignUpUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class UserRegistrationHandler extends JFrame {


    private final String profilePhotoStoragePath = "/Users/mac/IdeaProjects/QuackstagramDB/src/main/java/img/storage/profile/";

    public boolean doesUsernameExist(String username) {
        try{
            User existingUser = UserDAOImpl.getInstance().fecthUserData(username);
            return true;
        }catch (UserNotFoundException e){
            return false;
        }
    }

    // Method to handle profile picture upload
    public void handleProfilePictureUpload(Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        String username = SignUpUI.getCurrentSignUpUser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(parentComponent) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveProfilePicture(selectedFile, username);
        }
    }

    public void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Write image successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveCredentials(String username, String password, String bio) {
        Encryptor encryptor = Encryptor.getInstance();
        password = encryptor.encrypt(password);

        UserDAOImpl.getInstance().insert(username,password,bio);
    }
}
