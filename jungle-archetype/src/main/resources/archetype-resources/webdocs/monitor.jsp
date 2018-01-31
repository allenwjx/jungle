<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>

<%@ page language="java" pageEncoding="utf-8"%>
<%@page session="false"%>
<%@ page isELIgnored="false" %> 
<%
	/*****************************************
	**										**
	** 监控页面 判断web程序是否运行不正常或down机	**
	**										**
	******************************************/
	int i=1+2; //判断jsp是否还有计算能力
	pageContext.setAttribute("status",(i==3)?1:0);
	
%>
<%-- 返回1 则表示程序正常 --%>
${status}
