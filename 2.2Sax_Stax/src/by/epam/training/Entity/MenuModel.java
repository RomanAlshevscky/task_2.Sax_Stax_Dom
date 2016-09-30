package by.epam.training.Entity;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by User on 30.09.2016.
 */
public class MenuModel {
    private HashMap<String, LinkedList<Dish>> menu = new HashMap<>();

    public void AddSection(String name, LinkedList<Dish> dishes){
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
}
