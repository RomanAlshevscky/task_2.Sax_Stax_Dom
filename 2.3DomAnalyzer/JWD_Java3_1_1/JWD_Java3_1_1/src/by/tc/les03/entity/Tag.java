package by.tc.les03.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 30.09.2016.
 */
public class Tag {
    private String nameSpace;
    private String name;
    private int position;
    private HashMap<String, String> attributes;
    private boolean closed;

    public Tag(String tag, int pos) throws Exception{
        this.position = pos;
        parse(tag);
    }

    public String getName(){
        return this.name;
    }

    public Map getAttributes(){
        return attributes;
    }

    public String getAttributeValue(String name) throws Exception{
        if(attributes.isEmpty() || !attributes.containsKey(name))
            throw new Exception("No such attribute.");
        else
            return attributes.get(name);
    }

    private void parse(String tag) throws Exception{
        parseFullName(tag);
        parseAttributes(tag);
        isClosed(tag);
    }

    private void parseAttributes(String tag) throws Exception{
        this.attributes = new HashMap<String, String>();

        Pattern attrNamePattern = Pattern.compile(" [^=]+");
        Matcher attrNameMatcher =  attrNamePattern.matcher(tag);

        Pattern attrValuePattern = Pattern.compile("\".+\"");
        Matcher attrValueMatcher = attrValuePattern.matcher(tag);

        if (attrNameMatcher.groupCount() != attrValueMatcher.groupCount())
            throw new Exception("Doesn't match the number of attributes and it's values");

        while(attrNameMatcher.find() && attrValueMatcher.find()){
            this.attributes.put(attrNameMatcher.group(1),attrValueMatcher.group(1));
        }
    }

    private void isClosed(String tag){
            Pattern p = Pattern.compile("(^<\\/)|(\\/>$)");
            Matcher m = p.matcher(tag);
            this.closed = m.find();
    }

    private void parseFullName(String tag){
        Pattern p = Pattern.compile("^<\\w+[^ >]*");
        Matcher m = p.matcher(tag);
        StringBuilder sb = new StringBuilder(m.group(1));
        sb.delete(0,1);
        int separatorIndex = sb.indexOf(":");
        if(separatorIndex != -1){
            this.nameSpace = sb.substring(0, separatorIndex);
            this.name = sb.substring(separatorIndex+1, sb.length());
        }
        else{
            this.nameSpace = "";
            this.name = sb.toString();
        }

    }
}
