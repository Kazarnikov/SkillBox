import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class ImageResizer extends Thread {

    private static int newWidth = 300;
    private static String srcFolder = "D:\\Java\\src";
    private static String dstFolder = "D:\\Java\\dst";
    private static Iterator<File> iterator = new ArrayList<>(
            Arrays.asList(Objects.requireNonNull(new File(srcFolder).listFiles()))).iterator();

    @Override
    public void run() {
        int count = 0;
        try {
            while (iterator.hasNext()){
                File file = getFileNext();

                BufferedImage image = ImageIO.read(file);
                if (image == null) {
                    continue;
                }
                count++;
                int newHeight = (int) Math.round(
                        image.getHeight() / (image.getWidth() / (double) newWidth)
                );
                BufferedImage newImage = new BufferedImage(
                        newWidth, newHeight, BufferedImage.TYPE_INT_RGB
                );
                int widthStep = image.getWidth() / newWidth;
                int heightStep = image.getHeight() / newHeight;

                for (int x = 0; x < newWidth; x++) {
                    for (int y = 0; y < newHeight; y++) {
                        int rgb = image.getRGB(x * widthStep, y * heightStep);
                        newImage.setRGB(x, y, rgb);
                    }
                }
                File newFile = new File(dstFolder + "/" + file.getName());
                ImageIO.write(newImage, "jpg", newFile);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Time " + (System.currentTimeMillis() - Main.start)  + " ms Processed files " + count + " Thread " + getName() );
    }

    private synchronized File getFileNext() {
        return iterator.next();
    }
}
