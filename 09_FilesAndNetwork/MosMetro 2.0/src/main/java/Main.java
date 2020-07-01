import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static String urlData = "https://www.moscowmap.ru/metro.html#lines";
   // private static String urlData = "D:\\Desktop\\1\\Карта метро Москвы со станциями МЦК и МЦД 2020.html";
    private static String fileData = "src/main/resources/test.json";
    private static ObjectMapper objectMapper = new ObjectMapper();
    static Line line = null;

    public static void main(String[] args) {
        try {
            parseData();
            writerJsonFile();
            readerJsonFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseData() throws IOException {
        //Document document = Jsoup.parse(new File(urlData), "UTF-8");
        Document document = Jsoup.connect(urlData).maxBodySize(0).get();
        Elements elements = document.select("div[class=t-text-simple] > div > span[data-line~=^[0-9]]");
        for (Element element : elements) {
            String numLine = element.attr("data-line");
            line = new Line();
            Elements stations = document.select("div[data-depend-set=lines-" + numLine + "] > div > p > a > span:eq(1)");
            List<String> collect = stations
                    .stream()
                    .map(Element::text).distinct()
                    .collect(Collectors.toList());
            line.setListMap(numLine, collect);
            line.setMap();
        }
    }

    private static void writerJsonFile() throws IOException {
        objectMapper.writeValue(new File(fileData), Line.getMap());
    }

    private static void readerJsonFile() throws IOException {
        JsonNode jsonNode = objectMapper.readTree(new File(fileData));
        JsonNode arrayNode = jsonNode.path("stations");
        Iterator<String> keyJson = arrayNode.fieldNames();
        for (; keyJson.hasNext(); ) {
            String numberLine = keyJson.next();
            JsonNode listStations = arrayNode.get(numberLine);
            System.out.print("На " + numberLine);
            System.out.println("-ой линий расположено - " + listStations.size() + " станций");
        }
    }
}
