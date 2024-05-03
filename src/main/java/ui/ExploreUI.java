package ui;
import javax.swing.*;

import utils.HeaderPanelManager;
import utils.NavigationManager;

import java.awt.*;

public class ExploreUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private JPanel mainContentPanel;
    private JPanel headerPanel;
    private JPanel navigationPanel;
    JButton searchButton;
    NavigationManager navigationManager = new NavigationManager(this);


    public ExploreUI() {
        setupWindow();
        initializeUIComponent();
        initializeUI();
    }

    public void initializeUIComponent() {
        HeaderPanelManager headerPanelManager = new HeaderPanelManager();
        headerPanel = headerPanelManager.createHeaderPanel("Explore");
        //navigationManager = new NavigationManager(this);
        navigationPanel = navigationManager.createNavigationPanel();
        ExploreUIPanel exploreUIPanel = new ExploreUIPanel();
        mainContentPanel = exploreUIPanel.createMainContentPanel(this, headerPanel,navigationPanel);
    }

    public void setupWindow() {
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }
    
    public void initializeUI() {

        getContentPane().removeAll(); // Clear existing components
        setLayout(new BorderLayout()); // Reset the layout manager

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

}