package com.micronautics.aws;

import org.apache.commons.io.FileUtils;
import org.scribe.model.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public class BBDownloader implements Callable<File> {
    File tmpDir;
    Commit commit;
    String fileName;
    BitBucket bitBucket;

    public BBDownloader(File tmpDir, Commit commit, String fileName) throws IOException {
        this.tmpDir = tmpDir;
        this.commit = commit;
        this.fileName = fileName;
        bitBucket = new BitBucket();
    }

    public File call() {
        try {
            String urlStr = "https://bitbucket.org/" + commit.ownerName + "/" + commit.repoName + "/raw/master/" + fileName;
            Response response = bitBucket.request(urlStr);
            if (response.isSuccessful()) {
                File file = new File(tmpDir, fileName);
                FileUtils.copyInputStreamToFile(response.getStream(), file);
                return file;
            } else
                return null;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
