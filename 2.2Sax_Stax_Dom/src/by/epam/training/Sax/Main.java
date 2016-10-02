package by.epam.training.Sax;

import by.epam.training.Entity.MenuModel;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        MenuHandler handler = new MenuHandler();
        reader.setContentHandler(handler);
        reader.parse(new InputSource("menu.xml"));
        MenuModel menu= handler.getModel();
        System.out.print(menu);
    }
}
