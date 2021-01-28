import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XMLHandler extends DefaultHandler {

    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void startDocument() {
        System.out.println("Start parse XML... " + format.format(new Date()));
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals("voter")) {
                String birthDay = attributes.getValue("birthDay");
                String name = attributes.getValue("name");
                DBConnection.countVoter(name, birthDay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endDocument() {
        try {
            DBConnection.executeMultiInsert();
            System.out.println("Stop parse XML..."  + format.format(new Date()));
            DBConnection.printVoterCounts();
            DBConnection.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
