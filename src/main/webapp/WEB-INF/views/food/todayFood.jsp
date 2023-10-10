<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page import="kopo.poly.dto.FoodDTO" %>
<%@ page import="java.util.List" %>
<%
    List<FoodDTO> rList = (List<FoodDTO>) request.getAttribute("rList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>서울강서캠퍼스 식단</title>
    <link rel="stylesheet" href="/css/table.css"/>
</head>
<body>
<h2>이번주 한국폴리텍대학 서울강서캠퍼스 점심식사 메뉴는?</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">요일</div>
            <div class="divTableHead">메뉴</div>
        </div>
    </div>
    <div class="divTableBody">
        <%
            for (FoodDTO rDTO :rList) {
        %>
        <div class="divTableRow">
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getDay())%></div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getFood())%></div>
        </div>
        <%
            }
        %>
    </div>
</div>
</body>
</html>