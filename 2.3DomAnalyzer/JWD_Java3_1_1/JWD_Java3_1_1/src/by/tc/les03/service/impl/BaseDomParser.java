package by.tc.les03.service.impl;

import by.tc.les03.entity.Document;
import by.tc.les03.entity.Element;
import by.tc.les03.entity.Tag;
import by.tc.les03.service.DomParser;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
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
        Pattern p = Pattern.compile("<\\w+[^ >]*(\\:\\w+(\\d+(\\w)*)*)*.+>");
        Matcher m = p.matcher(content);
        LinkedList<Tag> ll = new LinkedList<>();
        while (m.find()) {
            String s = m.group(1);
            Tag t = new Tag(s, m.start(1));
            ll.add(t);
        }
        return ll;
    }

    private boolean elementHasValue(String s){
        Pattern tagsPattern = Pattern.compile("<");
        Matcher m = tagsPattern.matcher(s);
        if (m.find())
            return false;
        else
            return true;
    }

    private String findElementValue(String s){
        StringBuilder sb = new StringBuilder(s);
        sb.delete(0, sb.indexOf(">"));
        return sb.toString();
    }

    private Element createElement(Tag openTag, Tag closeTag,String fileContent){
        if (openTag.closed)
            return new Element(openTag, closeTag, null);
        else{
            StringBuilder sb = new StringBuilder(fileContent.substring
                    (openTag.getPosition(), closeTag.getPosition()-1));
            if(elementHasValue(sb.toString())) {
                return new Element(openTag, closeTag, findElementValue(sb.toString()));
            }
            else{
                return new Element(openTag, closeTag, null);
            }
        }
    }

    private int getCloseTagIndex(List<Tag> tags, Tag openTag) throws Exception{
        for (Tag tag: tags) {
            if (tag.getFullName() == openTag.getFullName()){
                return tags.indexOf(tag);
            }
        }
        throw new Exception("No close tag.");
    }

    private void setChildElements(Element parent, List<Tag> tags, String fileContent, int borderIndex)
            throws Exception {
        for (Tag tag: tags) {
            if(tags.indexOf(tag) < borderIndex){
                int closeTagIndex = getCloseTagIndex(tags, tag);
                Element child = createElement(tag, tags.get(closeTagIndex), fileContent);
                if (!child.hasValue()){
                    setChildElements(child, tags, fileContent, closeTagIndex);
                }
                parent.addChildNode(child);
            }
        }
    }

    private Element createElementHierarchy(List<Tag> tags, String fileContent) throws Exception{
        Tag openTag = tags.get(0);
        tags.remove(openTag);
        int closeTagIndex = getCloseTagIndex(tags, openTag);
        Tag closeTag = tags.get(closeTagIndex);
        tags.remove(closeTagIndex);
        Element mainElement = createElement(openTag, closeTag, fileContent);

        for (Tag tag: tags){
            openTag = tag;
            tags.remove(tag);
            closeTagIndex = getCloseTagIndex(tags, openTag);
            Element childElement = createElement(openTag, tags.get(closeTagIndex), fileContent);
            if (!childElement.hasValue()){
                setChildElements(childElement, tags, fileContent, closeTagIndex);
            }
            tags.remove(closeTagIndex);
            mainElement.addChildNode(childElement);
        }

        return mainElement;
    }

    private Document readXml(String fileContent) throws Exception{
        if (validateHeader(fileContent)){
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
