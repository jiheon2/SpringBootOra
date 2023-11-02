<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page import="kopo.poly.dto.OcrDTO" %>
<%
    List<OcrDTO> rList = (List<OcrDTO>) request.getAttribute("rList");
%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>OCR 리스트</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript">

        //상세보기 이동
        function doDetail(seq) {
            location.href = "/ocr/ocrInfo?seq=" + seq;
        }

    </script>
</head>
<body>
<h2>OCR 리스트</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">순번</div>
            <div class="divTableHead">파일이름</div>
            <div class="divTableHead">파일경로</div>
            <div class="divTableHead">확장자</div>
        </div>
    </div>
    <div class="divTableBody">
        <%
            for (OcrDTO dto : rList) {
        %>
        <div class="divTableRow">
            <div class="divTableCell" onclick="doDetail('<%=CmmUtil.nvl(dto.getSeq())%>')"><%=CmmUtil.nvl(dto.getSeq())%></div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getSaveFileName())%></div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getSaveFilePath())%></div>
            <div class="divTableCell"><%=CmmUtil.nvl(dto.getExt())%></div>
        </div>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
