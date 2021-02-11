import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.BufferedReader;
import java.io.FileReader;

import static java.lang.Runtime.getRuntime;

public class Loader {
    public static void main(String[] args) throws Exception {
        int sizeMB = 1048576;
        long startTime = System.currentTimeMillis();

        String fileName = "res/data-1572M.xml";
        countingVoters(fileName);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLHandler handler = new XMLHandler();

        long InitMemorySize = getRuntime().totalMemory() - getRuntime().freeMemory();
        parser.parse(fileName, handler);
        long e = (getRuntime().totalMemory() - getRuntime().freeMemory() - InitMemorySize) / sizeMB;

        DBConnection.printVoterCounts();

        long startQueryTime = System.currentTimeMillis();
        DBConnection.customSelect();
        long endQueryTime = System.currentTimeMillis() - startQueryTime;

        DBConnection.getConnection().close();

        long end = System.currentTimeMillis() - startTime;
        System.out.println("Lead Time: " + end + " ms.");
        System.out.println("Query duration: " + endQueryTime + " ms.");
        System.out.println("Free of memory " + getRuntime().freeMemory() / sizeMB + " MB");
        System.out.println("Maximum memory " + getRuntime().maxMemory() / sizeMB + " MB");
        System.out.println("Memory used: " + e + " MB");
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
