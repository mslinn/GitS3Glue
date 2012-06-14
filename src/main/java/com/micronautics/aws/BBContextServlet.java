package com.micronautics.aws;

import com.micronautics.aws.bitBucket.Commit;
import com.micronautics.aws.bitBucket.JSON;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;

@WebServlet(urlPatterns={"/bb"}, loadOnStartup=1)
public class BBContextServlet extends HttpServlet {
    public BBContext bbContext;

    public void init(ServletConfig config) throws ServletException {
        bbContext = new BBContext(config);
    }

     /** Captures payload parameter and writes to temporary file, which is deleted when the webapp restarts.
    The payload looks like this:
    <code>{
      "repository": {
        "website": "http://www.asdf.com/",
        "fork": false,
        "repoName": "asdf.www",
        "scm": "git",
        "absolute_url": "/mslinn/asdf.www/",
        "owner": "mslinn",
        "slug": "asdf.www",
        "is_private": true
      },
      "commits": [
        {"node": "b51cd557430b",
         "filesToActions": [{"type": "added", "file": "store/robots.txt"}],
         "branch": "master",
         "utctimestamp": "2012-05-31 05:01:58+00:00",
         "author": "mslinn",
         "timestamp": "2012-05-31 07:01:58",
         "raw_node": "babcdef7430bae0d3c729744ac5572c9dbaabe48", // 12 significant chars
         "parents": ["bbabdef40bce"],
         "raw_author": "Mike Slinn ",
         "message": "asdf\n",
         "size": -1,
         "revision": null}
      ],
      "canon_url": "https://bitbucket.org",
      "user": "mslinn"
    }</code> */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        ServletOutputStream out = response.getOutputStream();
        String payload = request.getParameter("payload");
        Commit commit = JSON.parseCommit(payload);
        String result = commit.repoName + "\n";
        try {
            for (String fileName : commit.filesToActions.keySet()) {
                String fileMetaJson = bbContext.bitBucketBasicAuth.urlStrSrc(commit.ownerName, commit.repoName, fileName);
                int fileSize = JSON.parseFileSize(fileMetaJson, fileName);
                String rawFileUrl = bbContext.bitBucketBasicAuth.urlStrRaw(commit.ownerName, commit.repoName, fileName);
                String action = commit.filesToActions.get(fileName); // todo figure out how to handle
                result += "  " + fileName + ": " + commit.filesToActions.get(fileName) + " " + fileSize + "bytes from " + rawFileUrl + "\n";
                bbContext.s3.uploadStream(commit.repoName, "key", bbContext.bitBucketBasicAuth.getInputStream(rawFileUrl), fileSize);
            }
        } catch (Exception e) {
            result += "\nError: " + e.getMessage();
        }
        FileUtils.writeStringToFile(bbContext.bitBucketPost, result);

        Runtime runtime = Runtime.getRuntime();
        NumberFormat numberFormat = NumberFormat.getInstance();
        String msg = result +
                "\nBBContextServlet has " + numberFormat.format(bbContext.tmpDir.getUsableSpace()) +
                  " bytes of free disk space\n" +
                runtime.availableProcessors() + " available logical processors (hardware threads)\n" +
                Thread.activeCount() + " active threads of 200 available\n" +
                "Memory: " + numberFormat.format(runtime.freeMemory()) + " bytes free, of " +
                  numberFormat.format(runtime.totalMemory()) + " bytes";
        out.write(msg.getBytes());
        out.flush();
        out.close();
    }
}
