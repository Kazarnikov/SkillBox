import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;

public class XMLHandler extends DefaultHandler {

    private Voter voter;
    private final HashMap<Voter, Byte> voterCounts;

    public XMLHandler() {
        voterCounts = new HashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("voter") && voter == null) {
            String birthDay = attributes.getValue("birthDay");
            String name = attributes.getValue("name");
            voter = new Voter(name, birthDay);
        } else if (qName.equals("visit") && voter != null) {
            byte count = voterCounts.getOrDefault(voter, (byte) 0);
            voterCounts.put(voter, (byte) (count + 1));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("voter")) {
            voter = null;
        }
    }

    public void printDuplicatedVoter() {
        for (Voter voter : voterCounts.keySet()) {
            byte count = voterCounts.get(voter);
            if (count > 1) {
                System.out.println(voter.toString() + " - " + count);
            }
        }
    }
}
