package com.helioscrypt.parser;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.helioscrypt.parser.controller.ParserController;

@WebMvcTest(ParserController.class)
public class ParserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void renderXMLInputShouldReturnJSONObject() throws Exception {
        final String expectedResponseContent = new String(
                Files.readAllBytes(Paths.get("./fixtures/expected-result.json")));

        String inputFilePath = "./fixtures/input.xml";
        String templateFilePath = "./fixtures/xml2json.liquid";

        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFilePath));
        byte[] templateBytes = Files.readAllBytes(Paths.get(templateFilePath));

        MockMultipartFile inputFile = new MockMultipartFile("input", "input.xml", null, inputBytes);
        MockMultipartFile templateFile = new MockMultipartFile("template", "xml2json", null, templateBytes);

        this.mockMvc.perform(
                multipart("/api/v1/render")
                        .file(inputFile)
                        .file(templateFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseContent, true));
    }

    @Test
    public void renderJSONInputShouldReturnJSONObject() throws Exception {
        final String expectedResponseContent = new String(
                Files.readAllBytes(Paths.get("./fixtures/expected-result.json")));

        String inputFilePath = "./fixtures/input.json";
        String templateFilePath = "./fixtures/xml2json.liquid";

        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFilePath));
        byte[] templateBytes = Files.readAllBytes(Paths.get(templateFilePath));

        MockMultipartFile inputFile = new MockMultipartFile("input", "input.json", null, inputBytes);
        MockMultipartFile templateFile = new MockMultipartFile("template", "xml2json", null, templateBytes);

        this.mockMvc.perform(
                multipart("/api/v1/render")
                        .file(inputFile)
                        .file(templateFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseContent, true));
    }
}
