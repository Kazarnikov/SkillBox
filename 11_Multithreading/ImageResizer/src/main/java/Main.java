import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Main {
    public static long start = System.currentTimeMillis();

    public static void main(String[] args) {

        int newWidth = 300;
        String srcFolder = "D:\\Java\\src";
        String dstFolder = "D:\\Java\\dst";
        Iterator<File> iterator = new ArrayList<>(
                Arrays.asList(Objects.requireNonNull(new File(srcFolder).listFiles()))).iterator();

        int processors = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < processors; i++) {
            new ImageResizer(newWidth, dstFolder, iterator).start();
        }
        System.out.println("Main Thread time " + (System.currentTimeMillis() - start) + " ms\n" +
                "Total Thread " + processors);
    }
}
