package com.micronautics.aws.bitBucket;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JSON {

    public static Commit parseCommit(String payload) {
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
                String fileAction = fileNode.path("type").getTextValue(); // Possible types are: added, modified, removed
                commit.files.put(fileName, fileAction);
            }
        }
        return commit;
    }

    /** Search payload for size of specified file.
     * <p><tt>raw</tt> URL with filename  just returns the file contents:<br/>
     * <tt>curl --user user:password https://api.bitbucket.org/1.0/repositories/$owner/$repo/raw/master/$file</tt></p>
     * <p><tt>src</tt> URL without filename returns directory metadata in JSON format:<br/>
     * <tt>curl --user user:password https://api.bitbucket.org/1.0/repositories/$owner/$repo/src/master/$dir</tt>
     * Returns:</p>
     * <pre>{
         "node": "b51cd557430b",
         "path": "dir1/",
         "directories": [
             "dir2",
             "dir3"
         ],
         "files": [
             {
                 "size": 68081,
                 "path": "dir1/blah blah blah.pdf",
                 "timestamp": "2012-05-27 09:19:01",
                 "utctimestamp": "2012-05-27 09:19:01+00:00",
                 "revision": "bbea1bb40bce"
             },
             {
                 "size": 12498,
                 "path": "dir1/blah.properties",
                 "timestamp": "2012-05-27 09:19:01",
                 "utctimestamp": "2012-05-27 09:19:01+00:00",
                 "revision": "bbea1bb40bce"
             }
         ]</pre>
     <p>Note that the directory is included as a prefix for each file</p> */
    public static int parseFileSize(String payload, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            rootNode = mapper.readValue(payload, JsonNode.class);
        } catch (IOException e) {
            return 0;
        }

        String path  = rootNode.path("path").getTextValue();
        JsonNode filesNode = rootNode.path("files");
        for (JsonNode fileNode : filesNode) {
            String filePath = fileNode.path("path").getTextValue();
            String name = filePath.substring(path.length());
            if (fileName.compareTo(name)==0)
                return fileNode.path("size").getIntValue();
        }
        return 0;
    }
}
