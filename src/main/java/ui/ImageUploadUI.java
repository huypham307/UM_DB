package ui;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import database.ImageDAOImpl;
import database.PostDAOImpl;
import database.UserDAOImpl;
import usermanager.User;
import utils.HeaderPanelManager;
import utils.NavigationManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageUploadUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    private JLabel imagePreviewLabel;
    private JTextArea bioTextArea;
    private JButton uploadButton;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private JPanel navigationPanel;
    ImageIcon imageIcon;
    JFileChooser fileChooser;


    public ImageUploadUI() {
        setTitle("Upload Image");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
        
    }

    private void initializeUI() {
         // Reuse the createNavigationPanel method
        HeaderPanelManager headerPanelManager = new HeaderPanelManager();
        headerPanel = headerPanelManager.createHeaderPanel("Upload Imge"); // Reuse the createHeaderPanel method

        NavigationManager navigationManager = new NavigationManager(this);
        navigationPanel = navigationManager.createNavigationPanel();

        // Main content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        createImageLabel();
        bioArea();
        createUploadButton();

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    // Bio text area
    public void bioArea(){
        bioTextArea = new JTextArea("Enter a caption");
        bioTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        bioTextArea.setLineWrap(true);
        bioTextArea.setWrapStyleWord(true);
        JScrollPane bioScrollPane = new JScrollPane(bioTextArea);
        bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        contentPanel.add(bioScrollPane);
    }

    // Upload button
    public void createUploadButton(){
        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
        contentPanel.add(uploadButton);
    }

    // Image preview
    public void createImageLabel(){
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));

        // Set an initial empty icon to the imagePreviewLabel
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);
        contentPanel.add(imagePreviewLabel);
    }

    private void uploadAction(ActionEvent event) {
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);
    
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            tryFile();
        }
    }

    public void tryFile(){
        File selectedFile = fileChooser.getSelectedFile();
            try {
                String username = readUsername(); // Read username from users.txt
                int imageId = getNextImageId(username);
                String fileExtension = getFileExtension(selectedFile);
                String newFileName = username + "_" + imageId + "." + fileExtension;
    
                Path destPath = Paths.get("src/main/java/img", "uploaded", newFileName);
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
    
                // write data to the database
                saveImageInfo(username, bioTextArea.getText());
    
                // Load the image from the saved path
                imageIcon = new ImageIcon(destPath.toString());
    
                // Check if imagePreviewLabel has a valid size
                if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
                    scaleImage();
                }
    
                imagePreviewLabel.setIcon(imageIcon);
    
    
                // Change the text of the upload button
                uploadButton.setText("Upload Another Image");
    
                JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    }
    public void scaleImage(){
        Image image = imageIcon.getImage();

        int previewWidth = imagePreviewLabel.getWidth();
        int previewHeight = imagePreviewLabel.getHeight();
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        double widthRatio = (double) previewWidth / imageWidth;
        double heightRatio = (double) previewHeight / imageHeight;
        double scale = Math.min(widthRatio, heightRatio);
        int scaledWidth = (int) (scale * imageWidth);
        int scaledHeight = (int) (scale * imageHeight);
        
        imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
    }
    
    private int getNextImageId(String username) throws IOException {
        Path storageDir = Paths.get("src/main/java/img", "uploaded"); // Ensure this is the directory where images are saved
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
        }
    
        int maxId = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(storageDir, username + "_*")) {
            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                int idEndIndex = fileName.lastIndexOf('.');
                if (idEndIndex != -1) {
                    String idStr = fileName.substring(username.length() + 1, idEndIndex);
                    try {
                        int id = Integer.parseInt(idStr);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException ex) {
                        // Ignore filenames that do not have a valid numeric ID
                    }
                }
            }
        }
        return maxId + 1; // Return the next available ID
    }
    
    private void saveImageInfo(String username, String bio) throws IOException {
        User user = UserDAOImpl.getInstance().fecthUserData(username);
        int user_id = user.getUserID();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        ImageDAOImpl.getInstance().insert(user_id, bio, timestamp);
    }



//file handlers not needed anymore
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }


    // this will use sessions table
   private String readUsername() throws IOException {
    Path usersFilePath = Paths.get("src/main/java/data", "users.txt");
    try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
        String line = reader.readLine();
        if (line != null) {
            return line.split(":")[0]; // Extract the username from the first line
        }
    }
    return null; // Return null if no username is found
   }
 }
