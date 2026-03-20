package utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    public final static String path = "Resources/images/";
    public final static String ext = ".png";

    public static Map<String, Image> images = new HashMap<String, Image>();

    public static Image getImage(String s) {
        return images.get(s);
    }
public static Image loadImage(String fname) throws IOException {
    File file = new File(path + fname);
    System.out.println("Trying to load: " + file.getAbsolutePath());
    System.out.println("Exists? " + file.exists());

    BufferedImage img = ImageIO.read(file);
    images.put(fname, img);
    System.out.println("Loaded image: " + fname);
    return img;
}

    public static Image loadImage(String imName, String fname) throws IOException {
        BufferedImage img = ImageIO.read(new File(path + fname));
        images.put(imName, img);
        return img;
    }

    public static void loadImages(String[] fNames) throws IOException {
        for (String s : fNames)
            loadImage(s);
    }

    public static void loadImages(Iterable<String> fNames) throws IOException {
        for (String s : fNames)
            loadImage(s);
    }

}
