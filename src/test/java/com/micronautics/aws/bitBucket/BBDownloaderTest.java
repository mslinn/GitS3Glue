package com.micronautics.aws.bitBucket;

import com.micronautics.aws.bitBucket.BBDownloader;
import com.micronautics.aws.bitBucket.Commit;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class BBDownloaderTest {

    @BeforeClass
    public static void setUpClass() { }

    @AfterClass
    public static void tearDownClass() { }

    @Test
    public void downloadBbTest() throws IOException {
        Commit commit = new Commit();
        commit.ownerName = "mslinn";
        commit.repoName = "slinnbooks.www";
        commit.files.put("index.jsp", "added"); // Possible types are: added, modified, removed
        commit.files.put("presentations.jsp", "added");
        for (String file : commit.files.keySet())
            new BBDownloader(FileUtils.getTempDirectory(), commit, file).call();
    }
}