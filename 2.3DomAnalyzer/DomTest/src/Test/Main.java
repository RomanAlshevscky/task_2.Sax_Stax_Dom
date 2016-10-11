package Test;

import by.tc.les03.entity.Document;
import by.tc.les03.entity.Element;
import by.tc.les03.service.DomParser;
import by.tc.les03.service.impl.DomFactory;

public class Main {

    public static void main(String[] args) {
        DomFactory factory = DomFactory.getInstance();
        DomParser dom = factory.create();
        try {
            Document doc = dom.parse("menu.xml");
            Element mainEl = doc.getDocumentElement();
            System.out.print(doc.getXmlEncoding()+"\n");
            System.out.print(doc.getXmlVersion()+"\n");
            System.out.print(mainEl.getNodeName()+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
