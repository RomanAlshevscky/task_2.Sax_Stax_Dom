package by.tc.les03.service;

import by.tc.les03.entity.Document;

public interface DomParser {
    Document parse(String fileName) throws Exception;
}
