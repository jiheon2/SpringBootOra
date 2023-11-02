<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.dto.OcrDTO" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    // OcrController 함수에서 model 객체에 저장된 값 불러오기
    OcrDTO rDTO = (OcrDTO) request.getAttribute("rDTO");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>OCR 상세보기</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script>
        // HTML로딩이 완료되고, 실행됨
        $(document).ready(function () {
            // 버튼 클릭했을때, 발생되는 이벤트 생성함(onclick 이벤트와 동일함)
            $("#btnDownLoad").on("click", function () {
                let filename = "<%=CmmUtil.nvl(rDTO.getSaveFileName())%>";
                location.href="/ocr/download?filename=" + filename;
            })
            $("#btnList").on("click", function () {
                location.href="/ocr/ocrList";
            })
        })
    </script>
</head>
<body>
<h2>OCR 상세보기</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell">저장된 파일명
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getSaveFileName())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">저장 경로
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getSaveFilePath())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">원본파일
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getOrgFileName())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">확장자
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getExt())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">내용
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getOcrText())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">작성자
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getRegId())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">작성일
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getRegDt())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">수정자
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getChgId())%>
            </div>
        </div>
        <div class="divTableRow">
            <div class="divTableCell">수정일
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getChgDt())%>
            </div>
        </div>
    </div>
</div>
<div>
    <button id="btnDelete" type="button">삭제</button>
    <button id="btnList" type="button">목록</button>
    <button id="btnDownLoad" type="button">다운로드</button>
</div>
</body>
</html>

