package com.micronautics.aws.bitBucket;

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
        commit.filesToActions.put("index.jsp", "added"); // Possible types are: added, modified, removed
        commit.filesToActions.put("presentations.jsp", "added");
        for (String file : commit.filesToActions.keySet())
            new BBDownloader(FileUtils.getTempDirectory(), commit, file).call();
    }
}