package com.example.xml2db.util;

import com.example.xml2db.model.DatabaseDef;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;

public class XmlParser {
    public static DatabaseDef parse(String xmlContent) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(DatabaseDef.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (DatabaseDef) unmarshaller.unmarshal(new StringReader(xmlContent));
    }
}
