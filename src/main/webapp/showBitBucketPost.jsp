<%@page import="java.io.File, org.apache.commons.io.FileUtils" %>
<html>
<body>
<%
    File tmpDir = (File)pageContext.getAttribute("javax.servlet.context.tmpdir");
    File bitBucketPost = new File(tmpDir, "bitBucketPost.txt");
    try {
%><pre><%= FileUtils.readFileToString(bitBucketPost) %></pre><%
    } catch (Exception ex) {
      %><%=ex.getMessage()%><%
    }
%>
</body>
</html>
