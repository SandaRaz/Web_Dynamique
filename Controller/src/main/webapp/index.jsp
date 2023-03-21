<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<style>
    .click{
        width: 15%;
        height: 30px;

        border-style: solid;
        border-color: darkgray;
        border-radius: 5px;
        background: linear-gradient(to right, rgba(255,255,255,0.25), rgba(255,255,255,0.36));

        font-family: "Century Gothic";
        font-size: large;
        color: black;

        display: flex;
        justify-content: center;
        align-items: center;

        margin-bottom: 2%;
    }

    a{
        text-decoration: none;
    }
</style>
<body>
    <form action="FrontServlet/" method="POST">
        <input type="text" name="test">
        <input type="submit" value="Tester">
    </form>
    <br>
    <div>
        <a href="FrontServlet?param=1"><div class="click">lien1</div></a>
        <a href="FrontServlet?param=2"><div class="click">lien2</div></a>
    </div>
</body>
</html>