package by.tc.les03.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 30.09.2016.
 */
public interface Node {

    Map<String, String> getAttributes();
    List<Node> getChildNodes();
    String getNodeName();
    String getNodeValue();
    boolean hasAttributes();
    boolean hasChildNodes();

}
