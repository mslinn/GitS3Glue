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

        String result = "";
        JsonNode commitsNode = rootNode.path("commits");
        for (JsonNode commitNode : commitsNode) {
            JsonNode filesNode = commitNode.path("files");
            for (JsonNode fileNode : filesNode) {
                String fileName   = fileNode.path("file").getTextValue();
                String fileAction = filesNode.get(0).path("type").getTextValue();
                result += fileName + ": " + fileAction + "\n";
            }
        }
        return result;
    }
}
