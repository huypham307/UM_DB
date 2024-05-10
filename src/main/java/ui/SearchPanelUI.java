package ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

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
    private final String credentialsFilePath = "src/main/java/data/credentials.txt";
    ArrayList<String> usernameList;

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

        usernameList = new ArrayList<>(); // Arraylist for storing usernames including the searched string
        searchString = ExploreUIPanel.input;
        System.out.println("Search: " + searchString);

        contentPanel = new JPanel(); //Main panel for holding ui elements
        contentPanel.setLayout(new GridLayout(10 + usernameList.size(),1,4,4));
 
        addResults(searchString);
        displayResults();

        JScrollPane scrollPane = new JScrollPane(contentPanel);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }
     
    // Add appropriate results to the ArrayList
    public void addResults(String search) {
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (line.startsWith(search)) {
                    usernameList.add(credentials[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display the search results in a list view
    public void displayResults(){
        if(usernameList.size() > 0){
            for (String element : usernameList){
                JButton button = new JButton(element);
                contentPanel.add(button);
                button.addActionListener(e -> {
                    this.dispose(); // Close the current frame
                    User user = new User(element); 
                    InstagramProfileUI profileUI = new InstagramProfileUI(user);
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