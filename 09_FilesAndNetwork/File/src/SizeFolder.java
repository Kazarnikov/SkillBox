import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class SizeFolder {

    private static String dirName = "D:/java/";
    private static long sumBytes;
    private static File file;

    public static void main(String[] args) throws IOException {
        file = new File(dirName);

        FileVisitor<Path> fileVisitor = new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                sumBytes += file.toFile().length();
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        };

        try {
            Files.walkFileTree(Paths.get(dirName), fileVisitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printSize("Размер содержимого папки " + dirName, sumBytes);
        printSize("Объем диска", file.getTotalSpace());
        printSize("Свободно на диска", file.getFreeSpace());
    }

    static void printSize(String msg, long bytes) {
        int unit = 1024;
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = (" KMGTPE").charAt(exp);
        System.out.printf(msg + " : %.1f %sB\n", bytes / Math.pow(unit, exp), pre);
    }
}