package by.epam.training.Stax;

import by.epam.training.Entity.Dish;
import by.epam.training.Entity.MenuModel;
import by.epam.training.Entity.TagsName;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws XMLStreamException, IOException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream inputStream = new FileInputStream("menu.xml");
        XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);
        MenuModel menu = getMenuModel(reader);
        System.out.println(menu);
    }

    /**
     *
     * @param reader
     * @return
     * @throws XMLStreamException
     */
    private static MenuModel getMenuModel(XMLStreamReader reader) throws XMLStreamException{
        MenuModel menu = new MenuModel();
        TagsName currentElementName = TagsName.MENU;
        LinkedList<Dish> currentSectionDishes = new LinkedList<>();
        Dish currentDish = new Dish();
        String currentSectionName = "";
        while(reader.hasNext()){
            int readerType = reader.next();

            switch(readerType){
                case XMLStreamConstants.START_ELEMENT:
                    currentElementName = TagsName.valueOf(reader.getLocalName().toUpperCase());
                    switch(currentElementName){
                        case SECTION:
                            currentSectionName = reader.getAttributeValue("", "name");
                            currentSectionDishes = new LinkedList<>();
                            break;
                        case DISH:
                            currentDish = new Dish();
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    currentElementName = TagsName.valueOf(reader.getLocalName().toUpperCase());
                    switch(currentElementName){
                        case SECTION:
                            menu.addSection(currentSectionName, currentSectionDishes);
                            break;
                        case DISH:
                            currentSectionDishes.add(currentDish);
                            break;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String value = reader.getText().trim();
                    if (value.isEmpty()){ break; }
                    switch (currentElementName){
                        case PHOTO:
                            currentDish.setPhoto(value);
                            break;
                        case NAME:
                            currentDish.setName(value);
                            break;
                        case DESC:
                            currentDish.setDescription(value);
                            break;
                        case WEIGHT:
                            currentDish.setWeight(value);
                            break;
                        case COUNT:
                            currentDish.setCount(value);
                            break;
                        case PRICE:
                            currentDish.setPrice(value);
                            break;
                    }
                    break;
            }
        }
        return menu;
    }

}
