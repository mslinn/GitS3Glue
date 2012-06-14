package com.micronautics.aws;

import com.micronautics.aws.bitBucket.BitBucketBasicAuth;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.File;

public class BBContext {
    File bitBucketPost;
    File tmpDir;
    S3 s3 = new S3();
    ServletConfig servletConfig;
    ServletContext servletContext;
    BitBucketBasicAuth bitBucketBasicAuth;

    public BBContext(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
        servletContext = servletConfig.getServletContext();
        tmpDir = (File)servletContext.getAttribute("javax.servlet.context.tempdir");
        bitBucketPost = new File(tmpDir, "bitBucketPost.txt");
        bitBucketBasicAuth = new BitBucketBasicAuth(s3);
    }
}
