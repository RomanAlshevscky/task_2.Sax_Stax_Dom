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


    /**
     *
     * @return
     */
    public Tag getCloseTag(){
        return closeTag;
    };

    /**
     *
     * @param openTag
     * @param closeTag
     * @param value
     */
    public Element(Tag openTag, Tag closeTag, String value){
        this.openTag = openTag;
        this.closeTag = closeTag;
        this.value = value;
        childNodes = new LinkedList<>();
    }

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    public String getAttribute(String name) throws Exception{
        return openTag.getAttributeValue(name);
    }

    /**
     *
     * @return
     */
    public Map getAttributes(){
        return openTag.getAttributes();
    }

    /**
     *
     * @return
     */
    public List<Node> getChildNodes(){
        return childNodes;
    }

    /**
     *
     * @param n
     */
    public void addChildNode(Node n){
        childNodes.add(n);
    }

    /**
     *
     * @return
     */
    public String getNodeName(){
        return openTag.getName();
    }

    /**
     *
     * @return
     */
    public String getNodeValue(){
        return this.value;
    }

    /**
     *
     * @param value
     * @throws Exception
     */
    public void setNodeValue(String value) throws Exception{
        if (hasValue()){
            this.value = value;
        }
        else
            throw new Exception("Element can't get value.");
    }

    /**
     *
     * @return
     */
    public boolean hasValue(){
        if (this.value == null)
            return false;
        else
            return true;
    }

    /**
     *
     * @return
     */
    public boolean hasAttributes(){
       return !getAttributes().isEmpty();
    }

    /**
     *
     * @return
     */
    public boolean hasChildNodes(){
        return !childNodes.isEmpty();
    }

    /**
     *
     * @return
     */

}

