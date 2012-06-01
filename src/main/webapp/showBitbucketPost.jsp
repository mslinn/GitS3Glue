<%@page import="java.io.File, org.apache.commons.io.FileUtils, java.util.Date" %>
<html>
<body>
<%
    File tmpDir = (File)pageContext.getAttribute("javax.servlet.context.tmpdir");
    File bitBucketPost = new File(tmpDir, "bitBucketPost.txt");
    try {
        String contents = FileUtils.readFileToString(bitBucketPost);
        %><p>Posted <%= new Date(bitBucketPost.lastModified()).toString() %></p>
        <pre><%= contents %></pre>
        <hr/><p><%=tmpDir.getFreeSpace()%> bytes free on disk.</p><%
    } catch (Exception ex) {
      %><%=ex.getMessage()%><%
    }
%>
</body>
</html>
