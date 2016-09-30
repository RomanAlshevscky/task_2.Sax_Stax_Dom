package by.tc.les03.entity;

import java.util.List;

public class Document {

    private Element documentElement;

    public Document(Element element){
        this.documentElement = element;
    }

    //public List<Node> getElementsByTagName(String name){}

    public Element getDocumentElement(){
        return this.documentElement;
    }

    public boolean isEmpty(){
        if (this.documentElement == null)
            return true;
        else
            return false;
    }


}
