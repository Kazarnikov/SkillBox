import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Loader {
    private static final SimpleDateFormat birthDayFormat = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat visitDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private static final HashMap<Integer, WorkTime> voteStationWorkTimes = new HashMap<>();
    private static final HashMap<Voter, Integer> voterCounts = new HashMap<>();

    public static void main(String[] args) throws Exception {

        String fileName = "res/data-1572M.xml";
        countingVoters(fileName);
        long initMemorySize = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.currentTimeMillis();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLHandler handler = new XMLHandler();
        parser.parse(fileName, handler);

        long end = System.currentTimeMillis() - startTime;
        long e = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - initMemorySize) / 1048576;
        System.out.println("Lead Time: " + end + " ms.");
        System.out.println("Memory used: " + e + " MB");
    }

    private static void parseFile(String fileName) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(fileName));

//        findEqualVoters(doc);
        fixWorkTimes(doc);
    }

    private static void findEqualVoters(Document doc) throws Exception {
        NodeList voters = doc.getElementsByTagName("voter");
        int votersCount = voters.getLength();
        for (int i = 0; i < votersCount; i++) {
            Node node = voters.item(i);
            NamedNodeMap attributes = node.getAttributes();

            String name = attributes.getNamedItem("name").getNodeValue();
            Date birthDay = birthDayFormat.parse(attributes.getNamedItem("birthDay").getNodeValue());

            Voter voter = new Voter(name, birthDay);
            Integer count = voterCounts.get(voter);
            voterCounts.put(voter, count == null ? 1 : count + 1);
        }
    }

    private static void fixWorkTimes(Document doc) throws Exception {
        NodeList visits = doc.getElementsByTagName("visit");
        int visitCount = visits.getLength();
        for (int i = 0; i < visitCount; i++) {
            Node node = visits.item(i);
            NamedNodeMap attributes = node.getAttributes();

            Integer station = Integer.parseInt(attributes.getNamedItem("station").getNodeValue());
            Date time = visitDateFormat.parse(attributes.getNamedItem("time").getNodeValue());
            WorkTime workTime = voteStationWorkTimes.get(station);
            if (workTime == null) {
                workTime = new WorkTime();
                voteStationWorkTimes.put(station, workTime);
            }
            workTime.addVisitTime(time.getTime());
        }
    }

    private static void countingVoters(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            System.out.printf("\u001B[34m" + "In file %,d voters." + "\u001B[0m\n", reader
                    .lines()
                    .filter(e -> e.contains("<voter name="))
                    .count());
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}