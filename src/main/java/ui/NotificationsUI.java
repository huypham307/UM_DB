package ui;
import javax.swing.*;

import utils.HeaderPanelManager;
import utils.NavigationManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationsUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final Logger LOGGER = Logger.getLogger(NotificationsUI.class.getName());

    private JPanel contentPanel;
    public NotificationsUI() {
        setTitle("Notifications");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        NavigationManager navigationManager = new NavigationManager(this);
        HeaderPanelManager headerPanelManager = new HeaderPanelManager();
        JPanel headerPanel = headerPanelManager.createHeaderPanel(" Notifications ");
        JPanel navigationPanel = navigationManager.createNavigationPanel();

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = setupScrollPane();

        String currentUsername = readUsername();
        List<String> notifications = readNotifications(currentUsername);
        for (String notification : notifications) {
            displayNotification(notification);
        }

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private JScrollPane setupScrollPane() {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private String readUsername() {
        String currentUsername = "";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/java/data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                currentUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read username", e);
        }
        return currentUsername;
    }

    private List<String> readNotifications(String currentUsername) {
        List<String> notifications = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/java/data", "notifications.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].trim().equals(currentUsername)) {
                    String notificationMessage = formatNotification(parts);
                    notifications.add(notificationMessage);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read notifications", e);
        }
        return notifications;
    }

    private String formatNotification(String[] parts) {
        // Format the notification message
        String userWhoLiked = parts[1].trim();
        String timestamp = parts[3].trim();
        return userWhoLiked + " liked your picture - " + getElapsedTime(timestamp) + " ago";
    }

    private void displayNotification(String notificationMessage) {
        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel notificationLabel = new JLabel(notificationMessage);
        notificationPanel.add(notificationLabel, BorderLayout.CENTER);

        contentPanel.add(notificationPanel);
        contentPanel.revalidate();
    }

    private String getElapsedTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long totalHoursBetween = ChronoUnit.HOURS.between(timeOfNotification, currentTime);
        long hoursBetween = totalHoursBetween % 24; // Hours that don't complete a full day
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        return formatElapsedTime(daysBetween, hoursBetween, minutesBetween);
    }

    private String formatElapsedTime(long days, long hours, long minutes) {
        StringBuilder timeElapsed = new StringBuilder();
        if (days > 0) {
            timeElapsed.append(days).append(" day").append(days > 1 ? "s" : "");
        }
        if (hours > 0) {
            if (!timeElapsed.isEmpty()) {
                timeElapsed.append(", ");
            }
            timeElapsed.append(hours).append(" hour").append(hours > 1 ? "s" : "");
        }
        if (minutes > 0 || timeElapsed.isEmpty()) {
            if (!timeElapsed.isEmpty()) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutes).append(" minute").append(minutes > 1 ? "s" : "");
        }

        return timeElapsed.toString();
    }
}