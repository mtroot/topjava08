<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
    <style>
        .exceed {
            color: red;
        }
        .norm {
            color: green;
        }
    </style>
</head>
<body>
<section>
    <h2>Meal list</h2>
    <table border="1">
        <thead>
        <tr>
            <th>DateTime</th>
            <th>Description</th>
            <th>Calories</th>
            <th>exceeded</th>
        </tr>
        </thead>
        <c:forEach items="${ml}" var="meal">
            <jsp:useBean id="meal" class="ru.javawebinar.topjava.model.MealWithExceed"/>
            <tr class=${meal.exceed ? "exceed" : "norm"}>
                <td><%DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");%>
                    <%=meal.getDateTime().format(f)%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>${meal.exceed}</td>
            </tr>
        </c:forEach>
    </table>
</section>

</body>
</html>
