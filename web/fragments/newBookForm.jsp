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
