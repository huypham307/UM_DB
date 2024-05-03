package ui;
import javax.imageio.ImageIO;
import javax.swing.*;

import utils.HeaderPanelManager;
import utils.NavigationManager;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuakstagramHomeUI extends JFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts
    private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;

    private static final Logger LOGGER = Logger.getLogger(QuakstagramHomeUI.class.getName());


    public QuakstagramHomeUI() {
        setupMainFrame();
        setupCardLayoutPanels();
        initializeUI();
        setupHeader();
        setupNavigationBar();
        showHomePanel();
    }

    private void setupMainFrame() {
        setTitle("Quakstagram Home");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void setupCardLayoutPanels() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());
        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");
        add(cardPanel, BorderLayout.CENTER);
    }

    private void setupHeader() {
        HeaderPanelManager headerPanelManager = new HeaderPanelManager();
        JPanel headerPanel = headerPanelManager.createHeaderPanel("🐥 Quackstagram ");
        add(headerPanel, BorderLayout.NORTH);
    }

    private void setupNavigationBar() {
        NavigationManager navigationManager = new NavigationManager(this);
        JPanel navigationPanel = navigationManager.createNavigationPanel();
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private void showHomePanel() {
        cardLayout.show(cardPanel, "Home");
    }

    private void initializeUI() {
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = setupScrollPane(contentPanel);
        loadAndDisplaySampleData(contentPanel);

        homePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    private JScrollPane setupScrollPane(JPanel contentPanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private void loadAndDisplaySampleData(JPanel contentPanel) {
        String[][] sampleData = createSampleData();
        populateContentPanel(contentPanel, sampleData);
    }

    private void populateContentPanel(JPanel panel, String[][] sampleData) {
        for (String[] postData : sampleData) {
            JPanel itemPanel = createItemPanel();
            JLabel nameLabel = createLabel(postData[0]);
            JLabel imageLabel = prepareImageLabel(postData[3]);
            JLabel descriptionLabel = createLabel(postData[1]);
            JLabel likesLabel = createLabel(postData[2]);
            JButton likeButton = createLikeButton(likesLabel, postData[3]);


            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(descriptionLabel);
            itemPanel.add(likesLabel);
            itemPanel.add(likeButton);


            panel.add(itemPanel);
            panel.add(createSpacingPanel());
        }
    }

    private JPanel createItemPanel() {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return itemPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton createLikeButton(JLabel likesLabel, String imageFilePath) {
        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(LIKE_BUTTON_COLOR);
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false);
        String imageId = new File(imageFilePath).getName().split("\\.")[0];
        likeButton.addActionListener(e -> handleLikeAction(imageId, likesLabel));
        return likeButton;
    }

    private JLabel prepareImageLabel(String imagePath) {
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setImageLabelIcon(imageLabel, imagePath);
        return imageLabel;
    }

    private void setImageLabelIcon(JLabel imageLabel, String imagePath) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), IMAGE_WIDTH), Math.min(originalImage.getHeight(), IMAGE_HEIGHT));
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            imageLabel.setText("Image not found");
            // Log error or notify the user
        }
    }

    private JPanel createSpacingPanel() {
        JPanel spacingPanel = new JPanel();
        spacingPanel.setPreferredSize(new Dimension(WIDTH - 10, 5));
        spacingPanel.setBackground(new Color(230, 230, 230));
        return spacingPanel;
    }

    private void handleLikeAction(String imageId, JLabel likesLabel) {
        Path detailsPath = Paths.get("img", "image_details.txt");
        String currentUser = getCurrentUser();
        if (currentUser.isEmpty()) {
            LOGGER.severe("Current user could not be determined.");
            return;
        }

        boolean updated = false;
        StringBuilder newContent = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        updated = readAndUpdateDetails(detailsPath, imageId, newContent, likesLabel, currentUser);
        if (updated) {
            updateLikesOnUIAndFile(detailsPath, newContent);
            recordLikeNotification(currentUser, "", imageId, timestamp);
        } else {
            LOGGER.severe("Failed to update likes for imageId: " + imageId);
        }
    }

    private String getCurrentUser() {
        try (BufferedReader userReader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = userReader.readLine();
            if (line != null) {
                return line.split(":")[0].trim();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read current user.", e);
        }
        return "";
    }

    private boolean readAndUpdateDetails(Path detailsPath, String imageId, StringBuilder newContent, JLabel likesLabel, String currentUser) {
        boolean updated = false;
        try (BufferedReader reader = Files.newBufferedReader(detailsPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    updated = true;
                    boolean findUser = false;
                    String[] parts = line.split(", ");
                    String usersLiked = parts[5].split(": ")[1];
                    String[] userLiked = usersLiked.split("/");
                    
                    for(int i = 0; i < userLiked.length; i++){
                        if(userLiked[i].equals(currentUser)){
                            findUser = true;
                            continue;
                        }
                    }
                    if(findUser == false){
                        String checkNull = userLiked[0];
                        int likes = Integer.parseInt(parts[4].split(": ")[1]) + 1;
                        parts[4] = "Likes: " + likes;
                            if(checkNull.equals("null")){
                                parts[5] = "UsersLikes: " + currentUser;
                            }
                            else{
                                parts[5] = "UsersLikes: " + String.join("/", userLiked) + "/" + currentUser;
                            }
                        line = String.join(", ", parts);
                        likesLabel.setText(parts[4]);
                    }
                }
                newContent.append(line).append("\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read or update image details.", e);
            return false;
        }
        return updated;
    }

    private void updateLikesOnUIAndFile(Path detailsPath, StringBuilder newContent) {
        try (BufferedWriter writer = Files.newBufferedWriter(detailsPath)) {
            writer.write(newContent.toString());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write updated likes back to file.", e);
        }
    }

    //Writing data into notifications.txt
    private void recordLikeNotification(String currentUser, String imageOwner, String imageId, String timestamp) {
        String notification = String.format("%s; %s; %s; %s\n", imageOwner, currentUser, imageId, timestamp);
        try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("data", "notifications.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            notificationWriter.write(notification);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to record like notification.", e);
        }
    }


    private String[][] createSampleData() {
        String currentUser = loadCurrentUser();
        if (currentUser.isEmpty()) {
            LOGGER.severe("Failed to load the current user.");
            return new String[0][0];
        }

        String followedUsers = getFollowedUsers(currentUser);
        String[][] tempData = new String[100][]; // Assuming a maximum of 100 posts for simplicity
        int count = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null && count < tempData.length) {
                String[] details = line.split(", ");
                String imagePoster = details[1].split(": ")[1];
                if (followedUsers.contains(imagePoster)) {
                    String imagePath = "img/uploaded/" + details[0].split(": ")[1] + ".png";
                    String description = details[2].split(": ")[1];
                    String likes = "Likes: " + details[4].split(": ")[1];
                    tempData[count++] = new String[]{imagePoster, description, likes, imagePath};
                }
            }
        } catch (IOException e) { 
            LOGGER.log(Level.SEVERE, "Failed to load image details.", e);
        }

        return Arrays.copyOf(tempData, count); // Trim the array to the actual count of entries
    }

    private String loadCurrentUser() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                return line.split(":")[0].trim();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read current user.", e);
        }
        return "";
    }

    private String getFollowedUsers(String currentUser) {
        StringBuilder followedUsers = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("data", "following.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(currentUser + ":")) {
                    followedUsers.append(line.split(":")[1].trim());
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to read followed users for user: " + currentUser, e);
        }
        return followedUsers.toString();
    }
}