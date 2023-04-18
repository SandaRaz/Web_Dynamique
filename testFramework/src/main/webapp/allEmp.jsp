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
    <h1>Affichage de tous les employes</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>NOM</th>
            <th>SALAIRE</th>
        </tr>
        <%
            for(Emp emp : list){
        %>
                <tr>
                    <td><%= emp.id %></td>
                    <td><%= emp.nom %></td>
                    <td><%= emp.salaire %></td>
                </tr>
        <%
            }
        %>
    </table>
    <a href="index.jsp"><-----</a>
</body>
</html>
