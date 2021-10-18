<%@ page import="step.library.utils.Db" %>
<%@ page import="step.library.models.Book" %>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit book</title>
</head>
<body>
<div>
    <label>Author
        <input type="text" name="author" id="author">
        <br/>
    </label>
    <label>Title
        <input type="text" name="title" id="title">
        <br/>
    </label>
    <label>Description
        <input type="text" name="description" id="description">
        <br/>
    </label>
    <br/>
    <button id="editButton">Редактировать</button> <br/>
</div>
</body>

<script>
    let prev;
    const author = document.querySelector("#author");
    const title = document.querySelector("#title");
    const description = document.querySelector("#description");

    document.addEventListener('DOMContentLoaded', (event) => {

        const editId = localStorage.getItem("editId");

        if(!editId){
            alert("Error happened :c");
            window.location = "index.jsp";
        }

        fetch("./books-api?id=" + editId, {
            method: "GET"
        })
            .then(res => res.json())
            .then(data => {
                console.log(data);

                prev = data;
                delete prev.id;
                delete prev.cover;

                author.value = data.author;
                title.value = data.title;
                description.value = data.description;
            })
    })


    document.querySelector('#editButton').addEventListener('click', e => {
        const payload = {
            id: localStorage.getItem("editId"),
            author: author.value.trim(),
            title: title.value.trim(),
            description: description.value.trim()
        };

        console.log(prev)
        console.log(payload)

        if(
            prev.author === payload.author &&
            prev.title === payload.title &&
            prev.description === payload.description
        ){
            alert("Данные не требуют перезаписи в базу данных")
            return;
        } else {
            fetch("./books-servlet", {
                method: "PUT",
                body: JSON.stringify(payload)
            }).then(res => res.json()).then(console.log)
        }
    })

</script>
</html>
