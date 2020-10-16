import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

public class ImageResizer extends Thread {
    private final int newWidth;
    private final String dstFolder;
    private final Iterator<File> iterator;

    public ImageResizer(int newWidth, String dstFolder, Iterator<File> iterator) {
        this.newWidth = newWidth;
        this.dstFolder = dstFolder;
        this.iterator = iterator;
    }

    @Override
    public void run() {
        int count = 0;
        try {
            while (iterator.hasNext()) {
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
        System.out.println("Time " + (System.currentTimeMillis() - Main.start) + " ms Processed files " + count + " Thread " + getName());
    }

    private synchronized File getFileNext() {
        return iterator.next();
    }
}
