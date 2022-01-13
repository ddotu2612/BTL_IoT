<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:if test="${pageContext.request.userPrincipal.name != null}">
		<a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a>
		| &nbsp;
		<a href="${pageContext.request.contextPath}/profile">Profile</a>
		| &nbsp;
		<a href="${pageContext.request.contextPath}/admin">Admin</a>
     	| &nbsp;
     	<a href="${pageContext.request.contextPath}/logout">Logout</a>
	</c:if>
	<c:if test="${pageContext.request.userPrincipal.name == null}">
		<a href="${pageContext.request.contextPath}/login">Login</a>
		<a href="${pageContext.request.contextPath}/register">Register</a>
	</c:if>
</body>
</html>