package by.tc.les03.Test;

import by.tc.les03.entity.Document;
import by.tc.les03.service.impl.BaseDomParser;

/**
 * Created by User on 02.10.2016.
 */
    public class Main {

    public static void main(String[] args) throws Exception {
        BaseDomParser d = new BaseDomParser();
        Document dt = d.parse("menu.xml");

    }
}
