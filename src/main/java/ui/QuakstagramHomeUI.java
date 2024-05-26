package ui;
import javax.imageio.ImageIO;
import javax.swing.*;

import database.FollowDAOImpl;
import database.ImageDAOImpl;
import database.PostDAOImpl;
import entities.Image;
import exception.LikeDuplicateException;
import usermanager.User;
import usermanager.UserAuthenticator;
import utils.HeaderPanelManager;
import utils.NavigationManager;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    int currentUserID;
    private static final Logger LOGGER = Logger.getLogger(QuakstagramHomeUI.class.getName());

    public QuakstagramHomeUI() {
        currentUserID = UserAuthenticator.getInstance().getAuthorizedUser().getUserID();
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
        ArrayList<Image> sampleData = createSampleData();
        populateContentPanel(contentPanel, sampleData);
    }

    private void populateContentPanel(JPanel panel,ArrayList<Image> sampleData) {
        String likeCount;
        for (Image image : sampleData) {
            likeCount = Integer.toString(PostDAOImpl.getInstance().fetchLikeCounts(image.getImageID()));
            JPanel itemPanel = createItemPanel();
            JLabel nameLabel = createLabel(image.getUsername());
            JLabel imageLabel = prepareImageLabel(image.getFilePath());
            JLabel descriptionLabel = createLabel(image.getImageBio());
            JLabel likesLabel = createLabel(likeCount);
            JButton likeButton = createLikeButton(likesLabel, image.getImageID());


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

    private JButton createLikeButton(JLabel likesLabel, String imageID) {
        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(LIKE_BUTTON_COLOR);
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false);
        likeButton.addActionListener(e -> handleLikeAction(imageID, likesLabel));
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
        //Track the user who liked the post
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        int currentLikeCount = PostDAOImpl.getInstance().fetchLikeCounts(imageId);;
        boolean isSuccessful = PostDAOImpl.getInstance().insert(currentUserID, imageId, timestamp);
        if (isSuccessful) {
            int updatedLikeCount = currentLikeCount + 1;
            likesLabel.setText(Integer.toString(updatedLikeCount));
        } else {
            System.out.println("Already liked");
        }
    }

    private ArrayList<Image> createSampleData() {
        return ImageDAOImpl.getInstance().fetchFollowedImage(currentUserID);
    }

}