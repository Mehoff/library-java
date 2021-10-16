<%@ page import="step.library.utils.Db" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="step.library.models.Book" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<% Book[] books = Db.getBookOrm().getList(); %>

<html>
<head>
    <title>Books</title>
</head>
<body>
    <h1>Books list:</h1>

    <ul>
        <% for(int i = 0; i < books.length; i+=1) { %>
        <li>

           <div class="book">
               <h1><%=books[i].getTitle()%></h1>
               <h3><%=books[i].getAuthor()%></h3>
               <h4><%=books[i].getDescription()%></h4>
               <img src="<%=books[i].getCover()%>"/>
           </div>

        </li>
        <% } %>
    </ul>


</body>
</html>
