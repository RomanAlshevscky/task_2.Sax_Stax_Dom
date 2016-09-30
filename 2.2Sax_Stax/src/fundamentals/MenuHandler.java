package fundamentals;

import fundamentals.Entity.Dish;
import fundamentals.Entity.MenuModel;
import fundamentals.Entity.TagsName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by User on 30.09.2016.
 */
public class MenuHandler extends DefaultHandler {

    private MenuModel menu = new MenuModel();
    private String currentSectionName;
    private LinkedList<Dish> currentSectionList;
    private Dish currentDish;
    private StringBuilder elementValue;

    public MenuModel getModel(){
        return this.menu;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elementValue = new StringBuilder();
        if (qName.equals("section")){
            currentSectionName = attributes.getValue("name");
            currentSectionList = new LinkedList<>();
        }
        else{
            if(qName.equals("dish")){
                currentDish = new Dish();
            }
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        TagsName tagName = TagsName.valueOf(qName.toUpperCase());
        switch (tagName){
            case SECTION:
                this.menu.AddSection(currentSectionName, currentSectionList);
                break;
            case DISH:
                this.currentSectionList.add(currentDish);
                break;
            case PHOTO:
                this.currentDish.setPhoto(elementValue.toString());
                break;
            case NAME:
                this.currentDish.setName(elementValue.toString());
                break;
            case DESC:
                this.currentDish.setDescription(elementValue.toString());
            break;
            case WEIGHT:
                this.currentDish.addWeight(elementValue.toString());
                break;
            case PRICE:
                this.currentDish.addPrice(elementValue.toString());
                break;
            case COUNT:
                this.currentDish.setCount(elementValue.toString());
                break;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException{
        elementValue.append(ch, start, length);
    }
}