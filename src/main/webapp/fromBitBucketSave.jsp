<%@page import="com.micronautics.aws.bitBucket.Commit, com.micronautics.aws.bitBucket.JSON, com.micronautics.aws.bitBucket.BBDownloader, org.apache.commons.io.FileUtils, java.io.File" %>
<html>
<body>
<%
 /* Captures payload parameter and writes to temporary file, which is deleted when webapp restarts
    Payload looks like this:
    {
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
         "files": [{"type": "added", "file": "store/robots.txt"}],
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
    }
    */

    File tmpDir = (File)pageContext.getAttribute("javax.servlet.context.tmpdir");
    File bitBucketPost = new File(tmpDir, "bitBucketPost.txt");
    String payload = request.getParameter("payload");
    Commit commit = JSON.parseCommit(payload);
    String result = commit.repoName + "\n";
    for (String fileName : commit.files.keySet()) {
        result += fileName + ": " + commit.files.get(fileName) + "\n";
        new BBDownloader(tmpDir, commit, fileName).call();
    }
    FileUtils.writeStringToFile(bitBucketPost, result);
%>
</body>
</html>