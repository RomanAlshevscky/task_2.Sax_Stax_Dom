package by.tc.les03.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Document {

    private Element documentElement;
    private String xmlEncoding;
    private String xmlVersion;
    private String xmlStandalone;
    /**
     *
     * @param element
     */
    public Document(Element element){
        this.documentElement = element;
        xmlEncoding = "UTF-8";
        xmlVersion = "1.0";
        xmlStandalone = "no";
    }

    public List<Node> getElementsByTagName(String name) {
        List<Node> result = new LinkedList<>();
        if (documentElement.getNodeName() == name)
            result.add(documentElement);

        //To remember elements with child nodes to avoid recursion
        Stack<Node> elWithChildNodes = new Stack<>();
        elWithChildNodes.push(documentElement);
        while (!elWithChildNodes.isEmpty()) {
            List<Node> childNodes = elWithChildNodes.pop().getChildNodes();
            while (childNodes != null) {
                Node currentEl = childNodes.get(0);
                childNodes.remove(0);
                if (currentEl.getNodeName() == name)
                    result.add(currentEl);
                if (currentEl.hasChildNodes())
                    elWithChildNodes.push(currentEl);
            }
        }
        return result;
    }

    /**
     *
     * @return
     */
    public Element getDocumentElement(){
        return this.documentElement;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty(){
        if (this.documentElement == null)
            return true;
        else
            return false;
    }

    public void setXmlEncoding(String value){
        if (value != null)
        xmlEncoding = value;
    }

    public String getXmlEncoding(){
        return xmlEncoding;
    }

    public void setXmlVersion(String value){
        if (value != null)
            xmlVersion = value;
    }

    public String getXmlVersion(){
        return xmlVersion;
    }

    public void setXmlStandalone(String value){
        if (value != null)
            xmlStandalone = value;
    }

    public String getXmlStandalone(){
        return xmlStandalone;
    }

}
