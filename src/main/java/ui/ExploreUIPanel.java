package ui;
import javax.swing.*;

import usermanager.User;
import utils.NavigationManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public class ExploreUIPanel {
    private static final int WIDTH = 300;
    private static final int IMAGE_SIZE = WIDTH / 3; // Size for each image in the grid
    JButton searchButton;
    NavigationManager navigationManager;
    public static String input;
    JTextField searchField;

    public JPanel createMainContentPanel(JFrame window, JPanel headerPanel, JPanel navigationPanel) {
        JPanel searchPanel = createSearchPanel();
        JScrollPane imageGridScrollPane = createImageGridScrollPane(window, headerPanel, navigationPanel);
        return assembleMainContentPanel(searchPanel, imageGridScrollPane, headerPanel, navigationPanel);
    }

    private JScrollPane createImageGridScrollPane(JFrame window, JPanel headerPanel, JPanel navigationPanel) {
        JPanel imageGridPanel = createImageGridPanel(window, headerPanel, navigationPanel);
        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private JPanel createImageGridPanel(JFrame window, JPanel headerPanel, JPanel navigationPanel) {
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows
        loadImageGrid(imageGridPanel, "src/main/java/img/uploaded", window, headerPanel, navigationPanel);
        return imageGridPanel;
    }

    private void loadImageGrid(JPanel imageGridPanel, String imageDirPath, JFrame window, JPanel headerPanel, JPanel navigationPanel) {
        File imageDir = new File(imageDirPath);
        if (imageDir.exists() && imageDir.isDirectory()) {
            File[] imageFiles = imageDir.listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg)"));
            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    ImageIcon imageIcon = scaleImageIcon(imageFile.getPath(), IMAGE_SIZE);
                    JLabel imageLabel = new JLabel(imageIcon);
                    imageLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            displayImage(imageFile.getPath(), window, headerPanel, navigationPanel);
                        }
                    });
                    imageGridPanel.add(imageLabel);
                }
            }
        }
    }

    private ImageIcon scaleImageIcon(String imagePath, int imageSize) {
        return new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH));
    }

    private JPanel assembleMainContentPanel(JPanel searchPanel, JScrollPane scrollPane, JPanel headerPanel, JPanel navigationPanel) {
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane);
        // Note: headerPanel and navigationPanel are not directly added here but passed for consistency
        return mainContentPanel;
    }

        private void displayImage(String imagePath, JFrame window, JPanel headerPanel, JPanel navigationPanel) {
        window.getContentPane().removeAll();
        window.setLayout(new BorderLayout());

        // Extract image ID from the imagePath
        String imageId = new File(imagePath).getName().split("\\.")[0];

        // Read image details
        String username = "";
        String bio = "";
        String timestampString = "";
        int likes = 0;
        Path detailsPath = Paths.get("src/main/java/img", "image_details.txt");
        try (Stream<String> lines = Files.lines(detailsPath)) {
            String details = lines.filter(line -> line.contains("ImageID: " + imageId)).findFirst().orElse("");
            if (!details.isEmpty()) {
                String[] parts = details.split(", ");
                username = parts[1].split(": ")[1];
                bio = parts[2].split(": ")[1];
                System.out.println(bio + "this is where you get an error " + parts[3]);
                timestampString = parts[3].split(": ")[1];
                likes = Integer.parseInt(parts[4].split(": ")[1]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle exception
        }

        // Calculate time since posting
        String timeSincePosting = "Unknown";
        if (!timestampString.isEmpty()) {
            LocalDateTime timestamp = LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(timestamp, now);
            timeSincePosting = days + " day" + (days != 1 ? "s" : "") + " ago";
        }

        // Top panel for username and time since posting
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton usernameLabel = new JButton(username);
        JLabel timeLabel = new JLabel(timeSincePosting);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);


        // Prepare the image for display
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            ImageIcon imageIcon = new ImageIcon(originalImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            imageLabel.setText("Image not found");
        }

        // Bottom panel for bio and likes
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea bioTextArea = new JTextArea(bio);
        bioTextArea.setEditable(false);
        JLabel likesLabel = new JLabel("Likes: " + likes);
        bottomPanel.add(bioTextArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);

        // Adding the components to the frame
        window.add(topPanel, BorderLayout.NORTH);
        window.add(imageLabel, BorderLayout.CENTER);
        window.add(bottomPanel, BorderLayout.SOUTH);

        // Re-add the header and navigation panels
        window.add(headerPanel, BorderLayout.NORTH);
        window.add(navigationPanel, BorderLayout.SOUTH);

        // Panel for the back button
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");

        // Make the button take up the full width
        backButton.setPreferredSize(new Dimension(WIDTH - 20, backButton.getPreferredSize().height));

        backButtonPanel.add(backButton);

        backButton.addActionListener(e -> {
            window.getContentPane().removeAll();
            window.add(headerPanel, BorderLayout.NORTH);
            window.add(createMainContentPanel(window, headerPanel,navigationPanel), BorderLayout.CENTER);
            window.add(navigationPanel, BorderLayout.SOUTH);
            window.revalidate();
            window.repaint();
        });
        final String finalUsername = username;

        usernameLabel.addActionListener(e -> {
            User user = new User(finalUsername); // Assuming User class has a constructor that takes a username
            InstagramProfileUI profileUI = new InstagramProfileUI(user);
            profileUI.setVisible(true);
            window.dispose(); // Close the current frame
        });

        // Container panel for image and details
        JPanel containerPanel = new JPanel(new BorderLayout());

        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the container panel and back button panel to the frame
        window.add(backButtonPanel, BorderLayout.NORTH);
        window.add(containerPanel, BorderLayout.CENTER);

        window.revalidate();
        window.repaint();
    }

    private JPanel createSearchPanel() {
        searchField = new JTextField("Search Users");
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            input = searchField.getText();
            ExploreUI exploreUI = new ExploreUI();
            navigationManager = exploreUI.navigationManager;
            navigationManager.searchUI();
        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));
        return searchPanel;
    }

    // Other helper methods (like displayImage) as necessary...
}
