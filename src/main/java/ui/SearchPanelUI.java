package ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import database.UserDAO;
import database.UserDAOImpl;
import usermanager.User;
import utils.HeaderPanelManager;
import utils.NavigationManager;

public class SearchPanelUI extends JFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JPanel navigationPanel;
    String searchString;
    List<User> usernameList;

    public SearchPanelUI(){
        setTitle("Search results");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        initializeUI();
    }
    public void initializeUI(){
        HeaderPanelManager headerPanelManager = new HeaderPanelManager();
        headerPanel = headerPanelManager.createHeaderPanel("Search results"); // Reuse the createHeaderPanel method

        NavigationManager navigationManager = new NavigationManager(this);
        navigationPanel = navigationManager.createNavigationPanel(); // Reuse the createNavigationPanel method

        List<User> usernameList = new ArrayList<>(); // Arraylist for storing usernames including the searched string

        searchString = ExploreUIPanel.input;
        System.out.println("Search: " + searchString);

        contentPanel = new JPanel(); //Main panel for holding ui elements
        addResults(searchString);
        contentPanel.setLayout(new GridLayout(10 + usernameList.size(),1,4,4));

        displayResults();

        JScrollPane scrollPane = new JScrollPane(contentPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }
     
    // Add appropriate results to the ArrayList
    public void addResults(String search) {
        usernameList = UserDAOImpl.getInstance().fecthUsernames(search);
    }

    // Display the search results in a list view
    public void displayResults(){
        if(!usernameList.isEmpty()){
            for (User element : usernameList){
                String elementName = element.getUsername();
                JButton button = new JButton(elementName);
                contentPanel.add(button);
                button.addActionListener(e -> {
                    this.dispose(); // Close the current frame
                    User user = new User(elementName);
                    InstagramProfileUI profileUI = null;
                    try {
                        profileUI = new InstagramProfileUI(user);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    profileUI.setVisible(true);
                });
            }
        }
        else{
            System.out.println("No results");
            JLabel resultsLabel = new JLabel("No results");
            contentPanel.add(resultsLabel);
        }
    }
    //o
}