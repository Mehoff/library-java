<%@ page import="step.library.utils.Db" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String confirm = request.getParameter("confirm");
%>

<html>
<head>
    <title></title>
</head>
<body>
<h1>Db is Empty</h1>
<%  if(confirm == null) { %>
<a href="?confirm=true">Install</a>
<% } else if (Db.getBookOrm().install()) { %>
<b>Created</b>
<script>
    setTimeout(() => {
        window.location.href = window.location.pathname
    }, 1000)
</script>
<% } else { %>
    <i>Create</i>
<% } %>
</body>

</html>
