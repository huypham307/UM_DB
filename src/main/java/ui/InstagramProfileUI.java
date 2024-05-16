package ui;
import javax.swing.*;

import database.UserDAOImpl;
import datahandler.FileHandler;
import mediahandler.ImageLoader;
import usermanager.User;
import usermanager.UserAuthenticator;
import usermanager.UserRelationshipManager;
import utils.BioPanelManager;
import utils.ButtonFactory;
import utils.FollowStat;
import utils.NavigationManager;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.List;



public class InstagramProfileUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private JPanel headerPanel;  // Panel for the header
    private JPanel navigationPanel; // Panel for the navigation

    private User currentUser; // User object to store the current user's information
    private UserRelationshipManager userRelationshipManager;
    private BioPanelManager bioPanelManager;
    private ButtonFactory buttonFactory;
    private int loggedInUserID;
    private boolean isAlreadyFollowed;
    JButton followButton;
    Path imageDir = Paths.get("src/main/java/img", "uploaded");

    ActionListener followAction;
    JPanel topHeaderPanel;
    private FileHandler fileHandler;
    private int currentUserID;

//    private NavigationManager navigationManager;

    public InstagramProfileUI(User user) throws SQLException {
        initializeManagers(user);
        configureUserProfile();
        initializeUIComponents();
        setupWindow();
    }

    private void initializeManagers(User user) {
        this.fileHandler = new FileHandler();
        this.currentUser = user;
        this.currentUserID = user.getUserID();
        this.loggedInUserID = UserAuthenticator.getInstance().getAuthorizedUser().getUserID();
        this.userRelationshipManager = new UserRelationshipManager();
        this.buttonFactory = new ButtonFactory();

        this.bioPanelManager = new BioPanelManager(user);
    }

    private void configureUserProfile() throws SQLException {
        configureUserProfile(currentUser.getBio(),
                UserDAOImpl.getInstance().fecthFollower(currentUser.getUserID()),
                UserDAOImpl.getInstance().fecthFollowing(currentUser.getUserID()),
                UserDAOImpl.getInstance().fecthPostNum(currentUser.getUserID()));
    }


    private void initializeUIComponents() {
        this.contentPanel = new JPanel();
        this.headerPanel = createProfilePanel(this.currentUser);
        NavigationManager navigationManager = new NavigationManager(this);
        this.navigationPanel = navigationManager.createNavigationPanel();
        System.out.println("Navigation successfully created");
    }

    private void setupWindow() {
        setTitle("DACS Profile");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    public void initializeUI() {
        getContentPane().removeAll(); // Clear existing components

        // Re-add the header and navigation panels
        add(headerPanel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);
        System.out.println("Content panel initialized: " + (contentPanel != null)); // Debugging line

        // Initialize the image grid

        initializeImageGrid();

        revalidate();
        repaint();
    }

    private void checkFollowStatus(){
        try {
            this.isAlreadyFollowed = userRelationshipManager.isAlreadyFollowing(loggedInUserID, currentUserID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFollowButton(){
        this.followAction =  e -> {
            try {
                userRelationshipManager.followUser(loggedInUserID, currentUserID);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            followButton.setText("Following"); // Assuming followButton is accessible here
        };
    }

    public void configureUserProfile(String bio, int followersCount, int followingCount, int postCount){
        currentUser.setBio(bio);
        currentUser.setFollowersCount(followersCount);
        currentUser.setFollowingCount(followingCount);
        currentUser.setPostCount(postCount);
    }

    //Problem: This method is too long, let see what it is currently handling nonw.
    private JPanel createProfilePanel(User user) {

//        readUsername();

        // Header Panel
        headerPanel = new JPanel();
        boolean isCurrentUser = isCurrentUser(currentUser.getUsername());

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        // Top Part of the Header (Profile Image, Stats, Follow Button)
        topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        //Profile Picture
        profilePicture();

        checkFollowStatus();
        updateFollowButton();

        // Follow Button
        followButton = buttonFactory.createFollowButton(isCurrentUser, isAlreadyFollowed,  user, followAction);

        // Stats Panel
        statsPanel();

        // Profile Name and Bio Panel
        JPanel bioPanel = bioPanelManager.createBioPanel(currentUser);
        headerPanel.add(bioPanel);

        return headerPanel;

    }
    // Profile image
    public void profilePicture(){
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("src/main/java/img/storage/profile/"+currentUser.getUsername()+".png").getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);
    }

    public void statsPanel(){
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user"+currentUser.getPostCount());
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostCount()) , "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);
    }

    private void initializeImageGrid() {
        contentPanel.removeAll();
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5));

        ImageLoader imageLoader = new ImageLoader();
        List<ImageIcon> images = imageLoader.loadImages(currentUser.getUsername() + "_",GRID_IMAGE_SIZE);
        images.forEach(imageIcon -> {
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(imageIcon); // This can stay or move to a new class/method
                }
            });
            contentPanel.add(imageLabel);
        });

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    //    Problem: This should not be handled in this class.
    private void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            getContentPane().removeAll(); // Remove all components from the frame
            initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    private boolean isCurrentUser(String profileUserName){
        return profileUserName.equals(UserAuthenticator.getInstance().getAuthorizedUser().getUsername());
    }
}
