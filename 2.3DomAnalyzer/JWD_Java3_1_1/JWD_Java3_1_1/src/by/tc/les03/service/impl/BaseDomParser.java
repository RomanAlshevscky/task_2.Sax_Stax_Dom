package by.tc.les03.service.impl;

import by.tc.les03.entity.Document;
import by.tc.les03.service.DomParser;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseDomParser implements DomParser {
	
	public Document parse(String fileName) throws Exception{
        Document doc = new Document();
        String fileContent = openFile(fileName);
        doc = read(fileContent);
        return doc;
    }

    private String openFile(String fileName) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(fileName));
        String s = new String(encoded, Charset.defaultCharset());
        return s;
    }

    private LinkedList<String> findTags(String content){
        Pattern p = Pattern.compile("<\\w+[^ >]*(\\:\\w+(\\d+(\\w)*)*)*.+>");
        Matcher m = p.matcher(content);
        LinkedList<String> ll = new LinkedList<>();
        while (m.find()) {
            String s = m.group(1);
            ll.add(s);
        }
        return ll;
    }

    private boolean checkCloseTag(String tag){
        Pattern p = Pattern.compile("(^<\\/)|(\\/>$)");
        Matcher m = p.matcher(tag);
        return m.find();
    }



    private Document read(String fileContent) throws Exception{
        if (validateHeader(fileContent)){
            throw new Exception();
        }

        LinkedList<String> tags = fin

        Document doc = new Document();
        StringBuilder sb = new StringBuilder(fileContent);


        return doc;
    }


    //Don't check encoding
    private boolean validateHeader(String fileContent){
        Pattern p = Pattern.compile("^<\\?xml +version *= *\".+\" +encoding *= *\".+\"\\?>");
        Matcher m = p.matcher(fileContent);
        return m.find();
    }

}
