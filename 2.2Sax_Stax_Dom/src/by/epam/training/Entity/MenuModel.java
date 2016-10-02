package by.epam.training.Entity;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by User on 30.09.2016.
 */
public class MenuModel {
    private HashMap<String, LinkedList<Dish>> menu = new HashMap<>();

    public void addSections(HashMap<String, LinkedList<Dish>> sectionsMap){
        menu.putAll(sectionsMap);
    }


    public void addSection(String name, LinkedList<Dish> dishes){
        this.menu.put(name, dishes);
    }

    public LinkedList<Dish> getDishes(String sectionName) throws Exception{
        if(!menu.containsKey(sectionName))
            throw new Exception("No such section");
        return menu.get(sectionName);
    }

    public Dish getDish(String sectionName, String dishName) throws Exception{
        LinkedList<Dish> ll = getDishes(sectionName);
        for (Dish dish: ll) {
            if (dish.getName() == dishName)
                return dish;
        }
        throw new Exception("No such dish.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String key: menu.keySet()) {
            sb.append(key+"\n");
            for (Dish d: menu.get(key)) {
                sb.append("Название: " +d.getName()+"\n");
                if (d.getDescription() != "")
                    sb.append("Описание: " +d.getDescription()+"\n");
                if(d.getWeight() != "")
                  sb.append("Порция(грамм): " +d.getWeight()+"\n");
                if(d.getCount() != "")
                    sb.append("Порция(шт.): " +d.getCount()+"\n");
                if(d.getPrice() != "")
                    sb.append("Цена: " +d.getPrice()+"\n");
                sb.append("\n");
            }
            sb.append("_____________________________\n");
        }

        return sb.toString();
    }
}
