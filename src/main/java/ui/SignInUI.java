package ui;
import javax.swing.*;

import datahandler.Encryptor;
import usermanager.User;
import usermanager.UserAuthenticator;
import utils.HeaderPanelManager;

import java.awt.*;
import java.awt.event.ActionEvent;

public class SignInUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSignIn, btnRegisterNow;
    private JLabel lblPhoto;
    private JPanel headerPanel;
    private JPanel photoPanel;
    private JPanel fieldsPanel;
    private JPanel buttonPanel;

    public SignInUI() {
        setTitle("Quackstagram - Sign In");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        initializeUI();
    }

    private void initializeUI() {
        HeaderPanelManager headerPanelManager = new HeaderPanelManager();
        headerPanel = headerPanelManager.createHeaderPanel("Quackstagram"); //Reuse createHeaderPanel method

        labelPhoto();
        fieldsPanel();
        signInButton();
        registerButton();
        
        // Panel to hold both buttons
        buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 2 rows, 1 column
        buttonPanel.setBackground(Color.red);
        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnRegisterNow);

        // Adding components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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

    // Text fields panel
    public void fieldsPanel(){
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = new JTextField("Username");
        txtPassword = new JPasswordField("Password");
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));
    }

    // Sign in button with black text
    public void signInButton(){
        btnSignIn = new JButton("Sign-In");
        btnSignIn.addActionListener(this::onSignInClicked);
        btnSignIn.setBackground(new Color(255, 90, 95)); // Use a red color that matches the mockup
        btnSignIn.setForeground(Color.BLACK); // Set the text color to black
        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));
    }
    
    // New button for navigating to SignUpUI
    public void registerButton(){
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);
        btnRegisterNow.setBackground(Color.WHITE); // Set a different color for distinction
        btnRegisterNow.setForeground(Color.BLACK);
        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);
    }

    public void onSignInClicked(ActionEvent event) {
        String enteredUsername = txtUsername.getText();
        String enteredPassword = txtPassword.getText();
        System.out.println(enteredUsername+" <-> "+enteredPassword);

        UserAuthenticator userAuthenticator = UserAuthenticator.getInstance();

        if (userAuthenticator.verifyCredentials(enteredUsername, enteredPassword)) {
            System.out.println("User successfully logged in!");
            // Close the SignUpUI frame
        dispose();

        // Open the SignInUI frame
        SwingUtilities.invokeLater(() -> {
            InstagramProfileUI profileUI = new InstagramProfileUI(userAuthenticator.getAuthorizedUser());
            profileUI.setVisible(true);
        });
        } else {
            System.out.println("It Didn't");
        }
    }

    private void onRegisterNowClicked(ActionEvent event) {
        // Close the SignInUI frame
        dispose();

        // Open the SignUpUI frame
        SwingUtilities.invokeLater(() -> {
            SignUpUI signUpFrame = new SignUpUI();
            signUpFrame.setVisible(true);
        });
    }

}
