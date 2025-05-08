package bogus.assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;

public class ImageAsset extends Asset {
    private BufferedImage image;

    public ImageAsset(String name, String location) {
        super(name, "assets/" + location);
    }

    @Override
    public void load() {
        try {
            // First try loading from filesystem
            File file = new File(location);
            if (file.exists()) {
                image = ImageIO.read(file);
                logger.log("Loaded image from filesystem: " + location);
                return;
            }

            // If not on filesystem try loading from JAR
            try (InputStream is = getClass().getResourceAsStream(location.startsWith("/") ? location : "/" + location)) {
                if (is == null) {
                    throw new IOException("Image not found: " + location);
                }
                image = ImageIO.read(is);
                System.out.println("Loaded image from JAR: " + location);
            }

        } catch (IOException e) {
            System.err.println("Failed to load image: " + location);
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }
}
