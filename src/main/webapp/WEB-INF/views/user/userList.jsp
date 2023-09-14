<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="kopo.poly.dto.UserInfoDTO" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Collections" %>
<%
    List<UserInfoDTO> rList = (List<UserInfoDTO>) request.getAttribute("rList");
%>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>유저 리스트</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript">

        //상세보기 이동
        function doDetail(userId) {
            location.href = "userInfo?userId=" + userId;
        }

    </script>
</head>
<body>
<h2>유저 리스트</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">순번</div>
            <div class="divTableHead">회원아이디</div>
            <div class="divTableHead">이름</div>
            <div class="divTableHead">이메일주소</div>
            <div class="divTableHead">주소</div>
            <div class="divTableHead">상세주소</div>
        </div>
    </div>
    <div class="divTableBody">
        <%
            for (UserInfoDTO dto : rList) {
        %>
        <div class="divTableRow">
            <div class="divTableCell"><%=(rList.size() - rList.indexOf(dto))%>
            </div>
            <div class="divTableCell" onclick="doDetail('<%=CmmUtil.nvl(dto.getUserId())%>')"><%=CmmUtil.nvl(dto.getUserId())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getUserName())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getEmail())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getAddr1())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getAddr2())%>
            </div>
        </div>
        <%
            }
        %>
    </div>
</div>
<a href="/notice/noticeReg">글쓰기</a>
</body>

</html>