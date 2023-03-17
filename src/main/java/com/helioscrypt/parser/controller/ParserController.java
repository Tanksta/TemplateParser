package com.helioscrypt.parser.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.apache.commons.io.FilenameUtils;

import liqp.Template;

@RestController
@RequestMapping("api/v1")
public class ParserController {

    @PostMapping(path = "/render", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public String renderTemplate(@RequestParam("input") MultipartFile input,
            @RequestParam("template") MultipartFile templateFile) {

        String extension = FilenameUtils.getExtension(input.getOriginalFilename());

        String inputString;
        String rendered = "";

        try {

            switch (extension) {
                case "xml":
                    inputString = convertXMLtoJSON(input.getBytes());
                    break;
                case "json":
                    inputString = new String(input.getBytes());
                    break;
                default:
                    inputString = "";
            }

            Template template = Template.parse(new String(templateFile.getBytes()));
            rendered = template.render(inputString);

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
