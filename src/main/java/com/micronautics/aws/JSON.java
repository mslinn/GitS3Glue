package com.micronautics.aws;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.TreeMap;

public class JSON {

    /** @return sorted map of file path to action, where action is one of: "added", "deleted".
     *          map is sorted by key (file path) */
    public static TreeMap<String, String> parse(String payload) {
        TreeMap<String, String> result = new TreeMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readValue(payload, JsonNode.class);
        } catch (IOException e) {
            return result;
        }

        JsonNode commitsNode = rootNode.path("commits");
        for (JsonNode commitNode : commitsNode) {
            JsonNode filesNode = commitNode.path("files");
            for (JsonNode fileNode : filesNode) {
                String fileName   = fileNode.path("file").getTextValue();
                String fileAction = fileNode.path("type").getTextValue();
                result.put(fileName, fileAction);
            }
        }
        return result;
    }
}
