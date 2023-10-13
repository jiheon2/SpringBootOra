<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page import="kopo.poly.dto.WeatherDTO" %>
<%
    WeatherDTO rDTO = (WeatherDTO) request.getAttribute("rDTO");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>오늘의 날씨</title>
    <link rel="stylesheet" href="/css/table.css"/>
</head>
<body>
<h2>날씨</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">날씨</div>
            <div class="divTableHead">온도</div>
        </div>
    </div>
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getWeather())%></div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getDegree())%></div>
        </div>
    </div>
</div>
</body>
</html>