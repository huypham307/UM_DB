package utils;
import javax.swing.*;

import usermanager.User;

import java.awt.*;

public class BioPanelManager {

    User user;
    private BioRepository bioRepo;
    public BioPanelManager(User user){
        this.user = user;
    }

    public JPanel createBioPanel(User user){
        JPanel profileNameAndBioPanel = new JPanel();

        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(user.getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

        bioRepo = new BioRepository(user);
        String bio = bioRepo.readBio();
        user.setBio(bio);

        JTextArea profileBio = new JTextArea(user.getBio());
        System.out.println("This is the bio: "+ user.getBio());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        return profileNameAndBioPanel;
    }
}
