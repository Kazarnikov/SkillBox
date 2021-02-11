import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.SQLException;

public class XMLHandler extends DefaultHandler {

    @Override
    public void startDocument() {
        System.out.println("Start parse XML... ");
    }

    @Override
    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attributes) {
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
            System.out.println("Stop parse XML...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
