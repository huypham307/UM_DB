package ui;
import javax.swing.*;

import usermanager.UserRegistrationHandler;
import utils.HeaderPanelManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SignUpUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    public static JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtBio;
    private JButton btnRegister;
    private JLabel lblPhoto;
    private JButton btnUploadPhoto;
    private JButton btnSignIn;
    private JPanel headerPanel;
    private JPanel photoPanel;
    private JPanel fieldsPanel;
    private JPanel registerPanel;
    UserRegistrationHandler newUser = new UserRegistrationHandler();
    private String password;
    private static String currentSignUpUser;

    public SignUpUI() {
        setTitle("Quackstagram - Register");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        initializeUI();

    }

    private void initializeUI() {
        // Header with the Quackstagram label
        HeaderPanelManager headerPanelManager = new HeaderPanelManager();
        headerPanel = headerPanelManager.createHeaderPanel("Quackstagram"); //Reuse createHeaderPanel method

        labelPhoto();
        fieldsPanel();
        uploadButton();
        registerButton();

        // Adding components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(registerPanel, BorderLayout.SOUTH);

        signInOption();
    }

    // Profile picture placeholder without border
    public void labelPhoto(){
        lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(new ImageIcon("src/main/java/img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));

        photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);
    }

    // Button to upload profile picture
    public void uploadButton(){
        btnUploadPhoto = new JButton("Upload Photo");
        
        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newUser();
            }
        });
        JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoUploadPanel.add(btnUploadPhoto);
        fieldsPanel.add(photoUploadPanel);
    }
    public void newUser(){
        newUser.handleProfilePictureUpload(this);
    }

    // Text fields panel
    public void fieldsPanel(){
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = new JTextField("Username");
        txtPassword = new JPasswordField("Password");
        txtBio = new JTextField("Bio");
        txtBio.setForeground(Color.GRAY);
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtBio);
    }

    // Register button with black text
    public void registerButton(){
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(this::onRegisterClicked);
        btnRegister.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnRegister.setForeground(Color.BLACK); // Set the text color to black
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnRegister, BorderLayout.CENTER);
    }

    // Adding the sign in button to the register panel
    public void signInOption(){
        btnSignIn = new JButton("Already have an account? Sign In");
        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignInUI();
            }
        });
        registerPanel.add(btnSignIn, BorderLayout.SOUTH);
    }

    private void onRegisterClicked(ActionEvent event) {
        String username = getCurrentSignUpUser();
        char[] passwordField = txtPassword.getPassword() ;
        String bio = txtBio.getText();
        
        password = String.valueOf(passwordField);
        
        if (newUser.doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            newUser.saveCredentials(username, password, bio);
            dispose();
    
        // Open the SignInUI frame
        SwingUtilities.invokeLater(() -> {
            SignInUI signInFrame = new SignInUI();
            signInFrame.setVisible(true);
        });
        }
    }

    private void openSignInUI() {
        // Close the SignUpUI frame
        dispose();

        // Open the SignInUI frame
        SwingUtilities.invokeLater(() -> {
            SignInUI signInFrame = new SignInUI();
            signInFrame.setVisible(true);
        });
    }

    public void setCurrentSignUpUser(String username){
        currentSignUpUser = username;
    }

    public static String getCurrentSignUpUser(){
        return txtUsername.getText();
    }
}
