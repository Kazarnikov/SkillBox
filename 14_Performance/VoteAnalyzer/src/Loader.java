import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static java.lang.Runtime.*;

public class Loader {
    public static void main(String[] args) throws Exception {
        int sizeMB = 1048576;
        long startTime = System.currentTimeMillis();

        String fileName = "res/data-18M.xml";
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLHandler handler = new XMLHandler();

        long InitMemorySize = getRuntime().totalMemory() - getRuntime().freeMemory();
        parser.parse(fileName, handler);
        long e = (getRuntime().totalMemory() - getRuntime().freeMemory() - InitMemorySize) / sizeMB;

        handler.printDuplicatedVoter();

        long end = System.currentTimeMillis() - startTime;
        System.out.println("Lead Time: " + end + " ms.");
        System.out.println("Free of memory " + getRuntime().freeMemory() / sizeMB + " MB");
        System.out.println("Maximum memory " + getRuntime().maxMemory() / sizeMB + " MB");
        System.out.println("Memory used: " + e + " MB");
    }
}
