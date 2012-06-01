package com.micronautics.aws;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JSON {
    public static String parse(String payload) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readValue(payload, JsonNode.class);
        } catch (IOException e) {
            return e.getMessage();
        }
        JsonNode commitsNode = rootNode.path("commits");
        JsonNode filesNode = commitsNode.get(0);
        String fileName   = filesNode.get(0).path("file").getTextValue();
        String fileAction = filesNode.get(0).path("type").getTextValue();
        String result = fileName + ": " + fileAction;
        return result;
    }
}
