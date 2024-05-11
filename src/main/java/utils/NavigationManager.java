package utils;
import javax.swing.*;

import ui.ExploreUI;
import ui.ImageUploadUI;
import ui.InstagramProfileUI;
import ui.NotificationsUI;
import ui.QuakstagramHomeUI;
import ui.SearchPanelUI;
import usermanager.User;
import usermanager.UserAuthenticator;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NavigationManager {

    private static JFrame frame;
    public NavigationManager(JFrame tab){

        frame = tab;
        this.buttonFactory = new ButtonFactory();
    }
    private final ButtonFactory buttonFactory;

    public JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(buttonFactory.createIconButton("src/main/java/img/icons/home.png", e -> openHomeUI()));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(buttonFactory.createIconButton("src/main/java/img/icons/search.png", e -> exploreUI()));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(buttonFactory.createIconButton("src/main/java/img/icons/add.png", e -> ImageUploadUI()));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(buttonFactory.createIconButton("src/main/java/img/icons/heart.png", e -> notificationsUI()));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(buttonFactory.createIconButton("src/main/java/img/icons/profile.png", e -> openProfileUI()));

        return navigationPanel;
    }

        private void ImageUploadUI() {
        // Open InstagramProfileUI frame
        frame.dispose();
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }

    private  void openProfileUI() {
       // Open InstagramProfileUI frame
       frame.dispose();
       String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
    try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/java/data", "users.txt"))) {
        String line = reader.readLine();
        if (line != null) {
            loggedInUsername = line.split(":")[0].trim();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
     User user = UserAuthenticator.getInstance().getAuthorizedUser();
       InstagramProfileUI profileUI = new InstagramProfileUI(user);
       profileUI.setVisible(true);
   }

     private  void notificationsUI() {
        // Open InstagramProfileUI frame
        frame.dispose();
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }

    private  void openHomeUI() {
        // Open InstagramProfileUI frame
        frame.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI();
        homeUI.setVisible(true);
    }

    private  void exploreUI() {
        // Open InstagramProfileUI frame
        frame.dispose();
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }

    public void searchUI() {
        frame.dispose();
        SearchPanelUI searchPanelUI = new SearchPanelUI();
        searchPanelUI.setVisible(true);
    }

}
