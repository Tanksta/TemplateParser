package com.helioscrypt.parser.controller;

import java.io.Console;
import java.io.File;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import liqp.Template;

@RestController
@RequestMapping("api/v1")
public class ParserController {

    @PostMapping(path = "/parseXML", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String handleFileUpload(@RequestParam("xml") MultipartFile xml,
            @RequestParam("template") MultipartFile templateFile) {

        String rendered = "";

        try {
            String xmlString = convertXMLtoJSON(xml.getBytes());

            Template template = Template.parse(new String(templateFile.getBytes()));
            rendered = template.render(xmlString);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return rendered;
    }

    private String convertXMLtoJSON(byte[] bytes) {
        XmlMapper xmlMapper = new XmlMapper();
        ObjectMapper jsonMapper = new ObjectMapper();

        String json = "";
        try {
            JsonNode node = xmlMapper.readTree(bytes);
            json = jsonMapper.writeValueAsString(node);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }
}
