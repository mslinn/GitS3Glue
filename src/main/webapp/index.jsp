<%@page import="com.micronautics.aws.S3" %>
<html>
<body>
<h2>Hello AWS from Heroku!</h2><%
S3 s3 = new S3();
if (s3.exception!=null) {
  %>Exception: <%=s3.exception.getMessage()%><br/><%
} else { %>
  Your buckets are: <%=s3.listBuckets()%><br/><%
} %></body>
</html>
