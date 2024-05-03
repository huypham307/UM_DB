import javax.swing.SwingUtilities;

import ui.SignInUI;

public class Program {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI();
            frame.setVisible(true);
        
        });
    }
}
