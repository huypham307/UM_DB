package utils;
import javax.swing.*;

import usermanager.User;

import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonFactory {

    public  ButtonFactory(){
    }

    private static final int ICON_SIZE = 20; // Default icon size, adjust as needed

    /**
     * Creates and returns a JButton with an icon, minimal styling, and an action listener.
     *
     * @param iconPath The path to the icon image.
     * @param actionListener The ActionListener to attach to the button.
     * @return A styled JButton with an icon.
     */
    public JButton createIconButton(String iconPath, ActionListener actionListener) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.addActionListener(actionListener);
        return button;
    }

    public JButton createFollowButton(boolean isCurrentUser, boolean isAlreadyFollowed, User user, ActionListener actionListener){
        // Follow Button
        JButton followButton = new JButton();

        if (isCurrentUser) {
            followButton.setText("Edit Profile");
        } else {
            followButton.setText("Follow");

            if(isAlreadyFollowed){
                followButton.setText("Following");
            }
            followButton.addActionListener(actionListener);

        }

        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal space
        followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding

        return followButton;
    }
}
