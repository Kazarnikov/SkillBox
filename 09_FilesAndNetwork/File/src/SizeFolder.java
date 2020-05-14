import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class SizeFolder {

    private static String dirName = "D:/java/";
    private static long sumBytes;
    private static File folder;

    public static void main(String[] args) throws IOException {
        folder = new File(dirName);
        getSizeFolderRecursion(folder);
        //getSizeFolderWalk(folder);

        printSize("Размер содержимого папки " + dirName, sumBytes);
        printSize("Объем диска", folder.getTotalSpace());
        printSize("Свободно на диска", folder.getFreeSpace());
    }

    private static void printSize(String msg, long bytes) {
        int unit = 1024;
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = (" KMGTPE").charAt(exp);
        System.out.printf(msg + " : %.1f %sB\n", bytes / Math.pow(unit, exp), pre);
    }

    private static void getSizeFolderRecursion(File folder) {
        File[] listFiles = folder.listFiles();
        for (File file : listFiles != null ? listFiles : new File[0]) {
            if (file.isDirectory()) {
                getSizeFolderRecursion(file);
            } else {
                sumBytes += file.length();
            }
        }
    }

    private static void getSizeFolderWalk(File folder) {
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
    }
}