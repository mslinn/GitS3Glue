package com.micronautics.aws;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Callable;

public class BBDownloader implements Callable<File> {
    File tmpDir;
    Commit commit;
    String fileName;

    public BBDownloader(File tmpDir, Commit commit, String fileName) {
        this.tmpDir = tmpDir;
        this.commit = commit;
        this.fileName = fileName;
    }

    // todo log into BB account
    public File call() {
        try {
            String urlStr = "https://bitbucket.org/" + commit.ownerName + "/" + commit.repoName + "/raw/master/" + fileName;
            File file = new File(tmpDir, fileName);
            FileUtils.copyURLToFile(new URL(urlStr), file);
            return file;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
