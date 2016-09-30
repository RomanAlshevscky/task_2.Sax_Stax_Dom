package by.tc.les03.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 30.09.2016.
 */
public class Element implements Node {

    private Tag openTag;
    private Tag closeTag;
    private LinkedList<Node> childNodes;
    private String value;

    public Element(Tag openTag, Tag closeTag, String value){
        this.openTag = openTag;
        this.closeTag = closeTag;
        this.value = value;
        childNodes = new LinkedList<>();
    }

    public String getAttribute(String name) throws Exception{
        return openTag.getAttributeValue(name);
    }

    public Map getAttributes(){
        return openTag.getAttributes();
    }

    public List<Node> getChildNodes(){
        return childNodes;
    }

    public void addChildNode(Node n){
        childNodes.add(n);
    }

    public String getNodeName(){
        return openTag.getName();
    }

    public String getNodeValue(){
        return this.value;
    }

    public void setNodeValue(String value) throws Exception{
        if (hasValue()){
            this.value = value;
        }
        else
            throw new Exception("Element can't get value.");
    }

    public boolean hasValue(){
        if (this.value == null)
            return false;
        else
            return true;
    }

    public boolean hasAttributes(){
       return !getAttributes().isEmpty();
    }

    public boolean hasChildNodes(){
        return !childNodes.isEmpty();
    }

}
