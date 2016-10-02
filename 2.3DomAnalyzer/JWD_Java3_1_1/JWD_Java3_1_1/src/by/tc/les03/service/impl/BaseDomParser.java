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


	public Document parse(String fileName) throws Exception{
        Document doc = new Document(null);
        String fileContent = openFile(fileName);
        doc = readXml(fileContent);
        return doc;
    }

    private String openFile(String fileName) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(fileName));
        String s = new String(encoded, Charset.defaultCharset());
        return s;
    }

    private LinkedList<Tag> parseTags(String content) throws Exception{
        //Pattern p = Pattern.compile("<\\/*\\w+[^ >]*(\\:\\w+(\\d+(\\w)*)*)*.+>");
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

    private boolean elementHasValue(String s){
        Pattern tagsPattern = Pattern.compile("^>.*<$");
        Matcher m = tagsPattern.matcher(s);
        if (m.find())
            return true;
        else
            return false;
    }

    private String findElementValue(String s){
        StringBuilder sb = new StringBuilder(s);
        sb.delete(0, 1);

        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }

    private Element createElement(Tag openTag, Tag closeTag,String fileContent){
        if (openTag.closed)
            return new Element(openTag, closeTag, "");
        else{
            StringBuilder sb = new StringBuilder(fileContent.substring
                    (openTag.getPosition()+openTag.getFullTag().length()-1, closeTag.getPosition()+1));
            if(elementHasValue(sb.toString())) {
                return new Element(openTag, closeTag, findElementValue(sb.toString()));
            }
            else{
                return new Element(openTag, closeTag, null);
            }
        }
    }

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

    private Element createElementHierarchy(List<Tag> tags, String fileContent) throws Exception{
        Tag openTag = tags.get(0);
        tags.remove(openTag);
        int closeTagIndex = getCloseTagIndex(tags, openTag);
        Tag closeTag = tags.get(closeTagIndex);
        tags.remove(closeTagIndex);
        Element mainElement = createElement(openTag, closeTag, fileContent);
        Stack<Element> parentEl = new Stack<>();
        parentEl.push(mainElement);
        while (!tags.isEmpty()){
            openTag = tags.get(0);
            tags.remove(0);
            if (parentEl.peek().getCloseTag() == openTag){
                parentEl.pop();
            }
            else {
                if (!openTag.closed) {
                    closeTagIndex = getCloseTagIndex(tags, openTag);
                    closeTag = tags.get(closeTagIndex);
                } else
                    closeTag = openTag;
                Element childElement = createElement(openTag, closeTag, fileContent);
                parentEl.peek().addChildNode(childElement);
                if (!childElement.hasValue()) {
                    parentEl.push(childElement);
                } else {
                    tags.remove(closeTag);
                }
            }
        }

        return mainElement;
    }

    private Document readXml(String fileContent) throws Exception{
        if (!validateHeader(fileContent)){
           throw new Exception();
        }

        LinkedList<Tag> tags = parseTags(fileContent);
        Element documentElement = createElementHierarchy(tags, fileContent);

        Document doc = new Document(documentElement);
        return doc;
    }

    //Doesn't check xml version and encoding
    private boolean validateHeader(String fileContent){
        Pattern p = Pattern.compile("^<\\?xml +version *= *\".+\" +encoding *= *\".+\"\\?>");
        Matcher m = p.matcher(fileContent);
        return m.find();
    }

}
