package ui;
import javax.swing.*;

import database.PostDAOImpl;
import entities.Notification;
import usermanager.User;
import usermanager.UserAuthenticator;
import utils.HeaderPanelManager;
import utils.NavigationManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
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
    private User currentUser;
    private JPanel contentPanel;
    public NotificationsUI() {
        setTitle("Notifications");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        currentUser = UserAuthenticator.getInstance().getAuthorizedUser();
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

        int currentUserID = currentUser.getUserID();
        List<String> notifications = readNotifications(currentUserID);
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

    private List<String> readNotifications(int currentUserID) {
        List<String> notificationString = new ArrayList<>();
        ArrayList<Notification> notifications = PostDAOImpl.getInstance().fectchNotifications(currentUserID);

        for (Notification notification: notifications){
            notificationString.add(formatNotification(notification));
        }
        return notificationString;
    }

    private String formatNotification(Notification notification) {
        // Format the notification message
        String userWhoLiked = notification.getLikername();
        Timestamp timestamp = notification.getLikeTime();
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

    private static String getElapsedTime(Timestamp timestamp) {
        LocalDateTime timeOfNotification = timestamp.toLocalDateTime(); // Convert to LocalDateTime
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long totalHoursBetween = ChronoUnit.HOURS.between(timeOfNotification, currentTime);
        long hoursBetween = totalHoursBetween % 24; // Hours not making a full day
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        return formatElapsedTime(daysBetween, hoursBetween, minutesBetween);
    }



    private static String formatElapsedTime(long days, long hours, long minutes) {
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

    public static void main(String[] args) {
        ArrayList<Notification> notifications = PostDAOImpl.getInstance().fectchNotifications(4);
        for (Notification notification : notifications){
            System.out.println(getElapsedTime(notification.getLikeTime()));
        }
    }
}