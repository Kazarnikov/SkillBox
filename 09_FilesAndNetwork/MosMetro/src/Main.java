import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static String urlData = "https://www.moscowmap.ru/metro.html#lines";
    private static String fileData = "data/test.json";
    private static JSONObject objA = new JSONObject();
    private static JSONObject objB = new JSONObject();
    private static JSONArray arr = null;

    public static void main(String[] args) {
        try {
            parseData();
            writerJsonFile();
            readerJsonFile();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseData() throws IOException {
        Document document = Jsoup.connect(urlData).maxBodySize(0).get();
        Elements elements = document.select("div[class=t-text-simple] > div > span[data-line~=^[0-9]]");
        elements.forEach(element -> {
            String numLine = element.attr("data-line");
            arr = new JSONArray();
            Elements stations = document.select("div[data-depend-set=lines-" + numLine + "] > div > p > a > span:eq(1)");
            stations.forEach(station -> arr.add(station.text()));
            objA.put(numLine, arr);
        });
        objB.put("stations", objA);
    }

    private static void writerJsonFile() throws IOException {
        FileWriter file = new FileWriter(fileData);
        file.write(objB.toJSONString());
        file.flush();
        file.close();
    }

    private static void readerJsonFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonData = (JSONObject) parser.parse(new FileReader(fileData));
        JSONObject stations = (JSONObject) jsonData.get("stations");
        stations.keySet().forEach(lineNum -> {
            System.out.print("На " + lineNum);
            JSONArray stationsArray = (JSONArray) stations.get(lineNum);
            System.out.println("-ой линий расположено - " + stationsArray.size() + " станций");
        });
    }
}
