<%@ page import="step.library.utils.Db" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<a style="margin-bottom: 10vh" href="index.jsp" >< Back</a>
<form method="post" enctype="multipart/form-data">
    <label>Author
        <input type="text" name="author">
        <br/>
    </label>
    <label>Title
        <input type="text" name="title">
        <br/>
    </label>
    <label>Description
        <input type="text" name="description">
        <br/>
    </label>
    <input type="file" name="cover"/>
    <br/>
    <input type="submit" value="Send"/> <br/>
</form>

<%--<script>--%>
<%--    async function addBook(author, title, description, cover){--%>
<%--        const book = {--%>
<%--            author,--%>
<%--            title,--%>
<%--            description,--%>
<%--            cover--%>
<%--        }--%>

<%--        await fetch('http://localhost:8081/Library_war_exploded/books-servlet', {--%>
<%--            method: 'POST',--%>
<%--            body: book--%>
<%--        });--%>

<%--    }--%>

<%--    const add = document.querySelector("#addToDb");--%>
<%--    add.addEventListener('click', (e) => {--%>
<%--        const author = document.querySelector()--%>
<%--    })--%>
<%--</script>--%>