<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="zzc.spring.web.beans.Person" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        // 1、从application域对象中得到IOC容器的实例
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
        // 2、从IOC容器中得到bean
        Person person = ctx.getBean(Person.class);
        // 3、使用bean
        person.hello();
    %>
</body>
</html>
