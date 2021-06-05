package com.gmail.yauheniylebedzeu.web.validator;

import com.gmail.yauheniylebedzeu.web.controller.exception.ItemFileException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

@Component
@Log4j2
public class XmlValidator {

    public void validateItemXMLFile(MultipartFile multipartFile){
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            File schemaFile = ResourceUtils.getFile("classpath:item-schema.xsd");
            Schema schema = factory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            Source fileSource = new StreamSource(multipartFile.getInputStream());
            validator.validate(fileSource);
        } catch (IOException | SAXException e) {
            log.error(e.getMessage(), e);
            throw new ItemFileException(String.format("File named \"%s\" failed verification (%s)",
                    multipartFile.getOriginalFilename(), e.getMessage()));
        }
    }
}