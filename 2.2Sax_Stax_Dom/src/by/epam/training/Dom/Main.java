package by.epam.training.Dom;

import by.epam.training.Entity.Dish;
import by.epam.training.Entity.MenuModel;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * Created by User on 02.10.2016.
 */
public class Main {

    public static void main(String[] args) throws IOException, SAXException {
        DOMParser domParser = new DOMParser();
        domParser.parse("menu.xml");
        Document document = domParser.getDocument();
        Element root = document.getDocumentElement();
        MenuModel menu = new MenuModel();

        NodeList sectionNodes = root.getElementsByTagName("section");
        menu.addSections(getSectionMap(sectionNodes));
        System.out.print(menu);

    }


    /**
     * @param section
     * @return
     */
    private static LinkedList<Dish> getDishes(Element section) {
        LinkedList<Dish> dishes = new LinkedList<>();
        NodeList dishesNodes = section.getElementsByTagName("dish");
        for (int i = 0; i < dishesNodes.getLength(); i++) {
            dishes.add(getDish((Element) dishesNodes.item(i)));
        }
        return dishes;
    }

    /**
     *
     * @param sectionsNodes
     * @return
     */
    private static HashMap<String, LinkedList<Dish>> getSectionMap(NodeList sectionsNodes) {
        HashMap<String, LinkedList<Dish>> sectionMap = new HashMap<>();
        for (int i = 0; i < sectionsNodes.getLength(); i++) {
            Element sectionEl = (Element) sectionsNodes.item(i);
            LinkedList<Dish> dishes = getDishes(sectionEl);
            String sectionName = sectionEl.getAttribute("name");
            sectionMap.put(sectionName, dishes);
        }
        return sectionMap;
    }

    /**
     *
     * @param nodes
     * @return
     */
    private static String getFieldValue(NodeList nodes) {
        Element el = (Element) nodes.item(0);
        if (el != null) {
            String value = el.getTextContent();
            if (value != null) {
                return value;
            }
        }
        return "";
    }

    /**
     *
     * @param dishEl
     * @return
     */
    private static Dish getDish(Element dishEl) {
        NodeList photoEl = dishEl.getElementsByTagName("photo");
        NodeList nameEl = dishEl.getElementsByTagName("name");
        NodeList descEl = dishEl.getElementsByTagName("desc");
        NodeList weightEl = dishEl.getElementsByTagName("weight");
        NodeList countEl = dishEl.getElementsByTagName("count");
        NodeList priceEl = dishEl.getElementsByTagName("price");

        Dish d = new Dish();
        d.setPhoto(getFieldValue(photoEl));
        d.setName(getFieldValue(nameEl));
        d.setDescription(getFieldValue(descEl));
        d.setWeight(getFieldValue(weightEl));
        d.setCount(getFieldValue(countEl));
        d.setPrice(getFieldValue(priceEl));
        return d;
    }

}
