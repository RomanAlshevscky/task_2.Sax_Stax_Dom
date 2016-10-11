package by.tc.les03.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 30.09.2016.
 */
public class Tag {

    private String tag; //source string to parse
    private String nameSpace;
    private String name;
    private int position;//position in xml doc
    private HashMap<String, String> attributes;//name, value
    public boolean closed;

    /**
     *
     * @param tag
     * @param pos
     * @throws Exception
     */
    public Tag(String tag, int pos) throws Exception{
        this.position = pos;
        this.tag = tag;
        parse(tag);
    }

    /**
     * Get posinion in xml doc
     * @return
     */
    public int getPosition(){
        return this.position;
    }

    /**
     * Get namespace:name
     * @return
     */
    public String getFullName(){
        if (this.nameSpace != "")
            return this.nameSpace + ":" + this.name;
        else
            return this.name;
    }

    /**
     * Get name without namespace
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     *
     * @return
     */
    public Map getAttributes(){
        return attributes;
    }

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    public String getAttributeValue(String name) throws Exception{
        if(attributes.isEmpty() || !attributes.containsKey(name))
            throw new Exception("No such attribute.");
        else
            return attributes.get(name);
    }

    /**
     * get source string with tag
     * @return
     */
    public String getFullTag(){
        return tag;
    }

    /**
     *
     * @param tag
     * @throws Exception
     */
    private void parse(String tag) throws Exception{
        parseFullName(tag);
        parseAttributes(tag);
    }

    /**
     *
     * @param tag
     * @throws Exception
     */
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

    /**
     *
     * @param tag
     * @return
     */
    private boolean isClosed(String tag){
        return tag.charAt(1) == '/' || tag.charAt(tag.length()-2) == '/';
    }

    /**
     *
     * @param tag
     * @throws Exception
     */
    private void parseFullName(String tag) throws Exception{
        Pattern p = Pattern.compile("^<\\/*\\w+[^ >]*");
        Matcher m = p.matcher(tag);
        m.find();
        String name = m.group(0);
        // Find tag name in two steps, using two reg's
        Pattern namePattern = Pattern.compile("\\w+(:\\w+){0,1}");
        Matcher nameMatcher = namePattern.matcher(name);
        nameMatcher.find();
        name = nameMatcher.group(0);

        if (isClosed(tag)){
            closed = true;
        }
        //Check for namespace
        StringBuilder sb = new StringBuilder(name);
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
