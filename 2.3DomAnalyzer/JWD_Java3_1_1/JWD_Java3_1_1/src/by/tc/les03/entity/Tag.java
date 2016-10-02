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
    public boolean closed;

    public Tag(String tag, int pos) throws Exception{
        this.position = pos;
        parse(tag);
    }

    public int getPosition(){
        return this.position;
    }

    public String getFullName(){
        if (this.nameSpace != "")
            return this.nameSpace + ":" + this.name;
        else
            return this.name;
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
    }

    private void parseAttributes(String tag) throws Exception{
        this.attributes = new HashMap<String, String>();

        Pattern attrNamePattern = Pattern.compile("[^ =]+ *=");
        Matcher attrNameMatcher =  attrNamePattern.matcher(tag);

        Pattern attrValuePattern = Pattern.compile("\"[^\"]*\"");
        Matcher attrValueMatcher = attrValuePattern.matcher(tag);
        boolean hasName = attrNameMatcher.find();
        boolean hasValue = attrValueMatcher.find();
        while(hasName && hasValue){
            String name = attrNameMatcher.group(0);
            this.attributes.put(name.substring(0, name.length()-1),attrValueMatcher.group(0));
            hasName = attrNameMatcher.find();
            hasValue = attrValueMatcher.find();
        }
        if (hasName != hasValue)
            throw new Exception("Doesn't match the number of attributes and it's values");

    }

    private void parseFullName(String tag) throws Exception{
        Pattern p = Pattern.compile("^<\\/*\\w+[^ >]*");
        Matcher m = p.matcher(tag);
        m.find();
        String name = m.group(0);
        if (name.charAt(1) == '/'){
            name = name.substring(1,name.length());
            closed = true;
        }
        StringBuilder sb = new StringBuilder(name);
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
