<%@ page import="step.library.utils.Db" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="step.library.models.Book" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<% Book[] books = Db.getBookOrm().getList(); %>


<html>
<head>
    <link rel="stylesheet" href="css/styles.css"/>
    <title>Books</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<main>
    <a style="margin-top: 2vh; margin-bottom: 2vh;" href="index.jsp">Back</a>
    <h1>Books list:</h1>
    <ul style="list-style: none">
        <% for(int i = 0; i < books.length; i+=1) { %>
        <li>
           <div id="<%=books[i].getId()%>" class="book">
               <div>
                   <h1><%=books[i].getTitle()%></h1>
                   <h3><%=books[i].getAuthor()%></h3>
                   <h4><%=books[i].getDescription()%></h4>
               </div>
               <%
                   if(books[i].getCover() == null) {
               %>
               <center>
                   <h2>No Cover</h2>
               </center>
               <% } else { %>
                <img src="uploads/<%=books[i].getCover()%>"/>
               <% } %>
               <ul>
                   <li>
                       <button id="<%=books[i].getId()%>" class="book-edit">Редактировать</button>
                   </li>
                   <li>
                       <button id="<%=books[i].getId()%>" class="book-delete">Удалить</button>
                   </li>
               </ul>
           </div>
        </li>
        <% } %>
    </ul>
</main>
<%--  <jsp:include page="fragments/footer.jsp"/>--%>
</body>
<script>
    for(const button of document.querySelectorAll(".book-edit")){
        button.addEventListener('click', (event) => {
            if(!event.target.id)
                return;

            localStorage.setItem('editId', event.target.id);
            window.location = 'editBook.jsp';
        })
    }

    for(const button of document.querySelectorAll(".book-delete")){
        button.addEventListener('click', (event) => {

            if(!event.target.id)
                return;

            if(confirm(`Вы уверены что хотите удалить книгу?`)){
                fetch("./books-servlet",
                    {
                        method: "DELETE",
                        body: JSON.stringify({id: event.target.id})
                    })
                    .then(res => res.json())
                    .then(json => {
                        if(json.status[0] === 'OK'){
                            setTimeout(() => {
                                window.location.href = window.location.pathname
                            }, 1000)
                        }
                    })
                    .catch(err => console.error(err));
            }

        })
    }
</script>


</html>
