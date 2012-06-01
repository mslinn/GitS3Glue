<%@page import="org.apache.commons.io.FileUtils, java.io.File, java.util.Enumeration" %>
<html>
<body>
<%
    // captures all parameters and writes to temporary file, which is deleted when webapp restarts
    File tmpDir = (File)pageContext.getAttribute("javax.servlet.context.tmpdir");
    File bitBucketPost = new File(tmpDir, "bitBucketPost.txt");
    Enumeration paramNames = request.getParameterNames();
    String result = "";
    while (paramNames.hasMoreElements()) {
        String pname = (String)paramNames.nextElement();
        String pvalue = request.getParameter(pname);
        result += pname + "=" + pvalue + "\n";
    }
    FileUtils.writeStringToFile(bitBucketPost, result);
%>
</body>
</html>
