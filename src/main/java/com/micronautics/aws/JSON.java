package com.micronautics.aws;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JSON {

    /** @return  */
    public static Commit parse(String payload) {
        Commit commit = new Commit();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readValue(payload, JsonNode.class);
        } catch (IOException e) {
            return commit;
        }

        JsonNode repositoryNode = rootNode.path("repository");
        commit.repoName  = repositoryNode.path("name") .getTextValue();
        commit.ownerName = repositoryNode.path("owner").getTextValue();

        JsonNode commitsNode = rootNode.path("commits");
        for (JsonNode commitNode : commitsNode) {
            JsonNode filesNode = commitNode.path("files");
            for (JsonNode fileNode : filesNode) {
                String fileName   = fileNode.path("file").getTextValue();
                String fileAction = fileNode.path("type").getTextValue();
                commit.files.put(fileName, fileAction);
            }
        }
        return commit;
    }
}
