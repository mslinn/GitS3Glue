package com.micronautics.aws;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BBDownloaderTest {

    @BeforeClass
    public static void setUpClass() { }

    @AfterClass
    public static void tearDownClass() { }

    @Test
    public void downloadBbTest() {
        Commit commit = new Commit();
        commit.ownerName = "mslinn";
        commit.repoName = "slinnbooks.www";
        commit.files.put("index.jsp", "added");
        commit.files.put("presentations.jsp", "added");
        for (String file : commit.files.keySet())
            new BBDownloader(FileUtils.getTempDirectory(), commit, file).call();
    }
}