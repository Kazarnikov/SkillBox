import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {
    private static String url = "https://lenta.ru/";
    private static String downloadPath = "downloads";
    private static InputStream in = null;
    private static String urls = null;
    private static File file = null;

    public static void main(String[] args) {
        createFolder();

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
        elements.forEach(element -> {
            try {
                urls = element.absUrl("src");
                file = new File(urls);
                in = new URL(urls).openStream();
                Files.copy(in, Paths.get(downloadPath).resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("\u001B[34m Скачено файлов: " + elements.size());
    }

    private static void createFolder() {
        if (!Files.exists(Paths.get(downloadPath))) {
            new File(downloadPath).mkdirs();
            System.out.println("\u001B[35m Создана папка: " + downloadPath.substring(downloadPath.lastIndexOf('/') + 1));
        }
    }
}





















