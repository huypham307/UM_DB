package mediahandler;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;


public class ImageLoader {
    private static final String IMAGE_DIR = "src/main/java/img/uploaded";

    public List<ImageIcon> loadImages(String userName, int GRID_IMAGE_SIZE) {
        List<ImageIcon> images = new ArrayList<>();
        Path imageDir = Paths.get(IMAGE_DIR);
        try (Stream<Path> paths = Files.list(imageDir)) {
            paths.filter(path -> path.getFileName().toString().startsWith(userName))
                    .forEach(path -> {
                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(path.toString()).getImage().getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
                        images.add(imageIcon);
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle exception
        }
        return images;
    }
}
