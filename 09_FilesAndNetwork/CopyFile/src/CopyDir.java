import java.io.IOException;
import java.nio.file.*;

public class CopyDir extends SimpleFileVisitor {
    public static void main(String[] args) throws IOException {
        Path srcDir = Paths.get("D:/testA/");
        Path destDir = Paths.get("D:/testB/");
        copyFile(srcDir, destDir);
        System.out.println("Содержимое папки " + srcDir.toFile().getName()
                + " скопировано в папку " + destDir.toFile().getName());
    }

    public static void copyFile(Path srcDir, Path destDir) throws IOException {
        Files.walk(srcDir)
                .forEach(x -> {
                    try {
                        Files.copy(x, destDir.resolve(srcDir.relativize(x)), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                       // e.printStackTrace();
                    }
                });
    }
}
