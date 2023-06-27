<%@ page import="test.Emp" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: MY PC
  Date: 31/03/2023
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Emp> list = (List<Emp>) request.getAttribute("lst");
%>

<html>
<head>
    <title>Test</title>
</head>
<body>
    <h1>Liste des employées</h1>
    
    <%
        for(Emp emp : list){
    %>
            <p><%= emp.nom %> - <a href="emp-detail?id=<%= emp.id %>&surnom=Surname"><b>Details</b></a> <p>
    <%
        }
    %>
    <a href="index.jsp"><-----</a>
</body>
</html>
