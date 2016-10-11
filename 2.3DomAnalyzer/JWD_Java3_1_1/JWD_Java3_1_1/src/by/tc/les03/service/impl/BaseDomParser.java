package by.tc.les03.service.impl;

import by.tc.les03.entity.Document;
import by.tc.les03.entity.Element;
import by.tc.les03.entity.Tag;
import by.tc.les03.service.DomParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseDomParser implements DomParser {

    /**
     *
     * @param fileName
     * @return
     * @throws Exception
     */
	public Document parse(String fileName) throws Exception{
        Document doc = new Document(null);
        String fileContent = openFile(fileName);
        doc = readXml(fileContent);
        return doc;
    }

    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private String openFile(String fileName) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(fileName));
        String s = new String(encoded, Charset.defaultCharset());
        return s;
    }

    /**
     * Create list of 'Tag' elements
     * @param content
     * @return
     * @throws Exception
     */
    private LinkedList<Tag> parseTags(String content) throws Exception{
        Pattern p = Pattern.compile("<(\\/ *)*\\w+[^ >]*(\\:\\w+(\\d+(\\w)*)*)*[^<]+>");
        Matcher m = p.matcher(content);
        LinkedList<Tag> ll = new LinkedList<>();
        while (m.find()) {
            String s = m.group(0);
            Tag t = new Tag(s, m.start(0));
            ll.add(t);
        }
        return ll;
    }

    /**
     * Check if there some text value between tags
     * <p>In case no text value return false</p>
     * @param s
     * @return
     */
    private boolean elementHasValue(String s){
        Pattern tagsPattern = Pattern.compile("^>.*<$");
        Matcher m = tagsPattern.matcher(s);
        if (m.find())
            return true;
        else
            return false;
    }

    /**
     * Input string '>.+<'(result from 'findElementValue')
     *  <p>Return string without first and last symbols(Help function for 'findElementValue')</p>
     * @param s
     * @return
     */
    private String getCleanValue(String s){
        StringBuilder sb = new StringBuilder(s);
        sb.delete(0, 1);

        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }

    /**
     * Find text value between tags.
     * <p>Return null in case no text value</p>
     * @return
     */
    private String findElementValue(Tag openTag, Tag closeTag,String fileContent){
        String s = new String(fileContent.substring
                (openTag.getPosition()+openTag.getFullTag().length()-1, closeTag.getPosition()+1));
        //String 's' like '>.+<' to check for text value
        if(elementHasValue(s))
            return getCleanValue(s);
        else
            return null;
    }


    /**
     *  Create Element item.
     * @param openTag
     * @param closeTag
     * @param fileContent
     * @return
     */
    private Element createElement(Tag openTag, Tag closeTag,String fileContent){
        if (openTag.closed)
            return new Element(openTag, closeTag, "");
        else{
            return new Element(openTag, closeTag, findElementValue(openTag, closeTag, fileContent));
        }
    }

    /**
     * Get close tag position index in source doc
     * @param tags
     * @param openTag
     * @return
     * @throws Exception
     */
    private int getCloseTagIndex(List<Tag> tags, Tag openTag) throws Exception{
        if (openTag.closed)
            return tags.indexOf(openTag);
        for (Tag tag: tags) {
            if (openTag.getFullName().equals(tag.getFullName()) &&
                    openTag.getPosition() < tag.getPosition()){
                return tags.indexOf(tag);
            }
        }
        throw new Exception("No close tag.");
    }

    /**
     *
     * @param tags
     * @param fileContent
     * @return
     * @throws Exception
     */
    private Element createElementHierarchy(List<Tag> tags, String fileContent) throws Exception{
        //Open tag of main el
        Tag openTag = tags.get(0);
        tags.remove(openTag);
        //Close tag of main el
        int closeTagIndex = getCloseTagIndex(tags, openTag);
        Tag closeTag = tags.get(closeTagIndex);
        tags.remove(closeTagIndex);

        Element mainElement = createElement(openTag, closeTag, fileContent);
        //Using stack for creating hierarchy instead of recursion
        //Parent element always on top
        Stack<Element> parentsElStack = new Stack<>();
        parentsElStack.push(mainElement);

        while (!tags.isEmpty()){
            openTag = tags.get(0);
            tags.remove(0);
            //if top tag from 'tags' is closeTag for top element on stack
            //than top element is finished
            //else - it's open tag for new child element
            if (parentsElStack.peek().getCloseTag() == openTag){
                parentsElStack.pop();
            }
            else {
                if (!openTag.closed) {
                    closeTagIndex = getCloseTagIndex(tags, openTag);
                    closeTag = tags.get(closeTagIndex);
                } else
                    closeTag = openTag;
                //Create el and add to child list of parent el
                Element childElement = createElement(openTag, closeTag, fileContent);
                parentsElStack.peek().addChildNode(childElement);
                //If no text value, than this element has child elements
                if (!childElement.hasValue()) {
                    parentsElStack.push(childElement);
                } else {
                    tags.remove(closeTag);
                }
            }
        }

        return mainElement;
    }

    /**
     *
     * @param fileContent
     * @return
     * @throws Exception
     */
    private Document readXml(String fileContent) throws Exception{
        String header = getXmlHeader(fileContent);

        LinkedList<Tag> tags = parseTags(fileContent);
        Element documentElement = createElementHierarchy(tags, fileContent);

        Document doc = new Document(documentElement);
        doc.setXmlEncoding(getXmlEncoding(header));
        doc.setXmlVersion(getXmlVersion(header));
        doc.setXmlStandalone(getXmlStandalone(header));
        return doc;
    }

    /**
     * Validate header
     * @param fileContent
     * @return
     */
    private String getXmlHeader(String fileContent)throws Exception  {
        Pattern p = Pattern.compile("^<\\?xml +version *= *\".+\" +encoding *= *\".+\"\\?>");
        Matcher m = p.matcher(fileContent);
        if (m.find())
            return m.group(0);
        else
            throw new Exception("Can't find xml header.");
    }

    private String getXmlEncoding(String header){
        Pattern p = Pattern.compile("version *= *\".*\"");
        Matcher m = p.matcher(header);
        if (m.find()){
            String s = m.group(0);
            return s.substring(s.indexOf('"'), s.length()-1);
        }
        else
            return null;
    }

    private String getXmlVersion(String header){
        Pattern p = Pattern.compile("encoding *= *\".*\"");
        Matcher m = p.matcher(header);
        if (m.find()) {
            String s = m.group(0);
            return s.substring(s.indexOf('"'), s.length() - 1);
        }
        else
            return null;
    }

    private String getXmlStandalone(String header){
        Pattern p = Pattern.compile("standalon *= *\".*\"");
        Matcher m = p.matcher(header);
        if (m.find()){
            String s = m.group(0);
            return s.substring(s.indexOf('"'), s.length()-1);
        }
        else
            return null;
    }

}
