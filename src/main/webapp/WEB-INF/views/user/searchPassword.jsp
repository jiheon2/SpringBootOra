<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>
    <script type="text/javascript">

        $(document).ready(function () {

            // 로그인 화면 이동
            $("#btnLogin").on("click", function () {
                location.href = "/user/login";
            })

            $("#btnEmail").on("click", function () {
                authNumber(f)
            })

            // 비밀번호 찾기
            $("#btnSearchPassword").on("click", function () {
                let f = document.getElementById("f");

                if (f.userId.value === "") {
                    alert("아이디를 입력하세요.");
                    f.userId.focus();
                    return;
                }

                if (f.userName.value === "") {
                    alert("이름을 입력하세요.");
                    f.userName.focus();
                    return;
                }

                if (f.email.value === "") {
                    alert("이메일을 입력하세요.");
                    f.email.focus();
                    return;
                }

                if (f.authNumber.value === "") {
                    alert("인증번호를 입력하세요.");
                    f.email.focus();
                    return;
                }

                f.method = "post"; // PW 찾기 정보 전송 방식
                f.action = "/user/searchPasswordProc" // PW 찾기 URL

                f.submit(); // PW 찾기 정보 전송
            })

            function authNumber(f) {
                if(f.email.value === "") {
                    alert("이메일을 입력하세요");
                    f.email.focus();
                    return;
                }

                $.ajax({
                    url : "/user/getAuthNumber",
                    type : "post", // 전송방식
                    dataType : "JSON", // 전송결과 JSON으로 받기
                    data : $("#f").serialize(), // form태그 내 input타입 등 객체를 자동으로 전송할 형태로 변경
                    success: function (json) { // 호출성공 시

                        if(json.existsYn === "Y") {
                            alert("이메일로 인증번호가 발송되었습니다. \n받은 메일의 인증번호를 입력하기 바랍니다.");
                            emailAuthNumber = json.authNumber;
                        } else {
                            alert("존재하지 않는 이메일 입니다.")
                            f.email.focus();
                        }
                    }
                })

            }

            $("#btnCheckAuth").on("click", function () {
                if (f.authNumber.value == emailAuthNumber) {
                    alert("인증번호가 확인되었습니다.");
                } else {
                    alert("이메일 인증번호가 일치하지 않습니다.");
                    f.authNumber.focus();
                    return;
                }
            })

        })

    </script>
</head>
<body>
<h2>비밀번호 찾기</h2>
<hr/>
<br/>
<form id="f">
    <div class="divTable minimalistBlack">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCell">아이디</div>
                <div class="divTableCell">
                    <input type="text" name="userId" id="userId" style="width: 95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">이름</div>
                <div class="divTableCell">
                    <input type="text" name="userName" id="userName" style="width: 95%"/>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCell">이메일</div>
                <div class="divTableCell">
                    <input type="email" name="email" id="email" style="width: 80%"/>
                    <button id="btnEmail" type="button">인증번호 발송</button>
                </div>
            </div>
            <div class = "divTableRow">
                <div class = "divTableCell">인증번호
                </div>
                <div class = "divTableCell">
                    <input type="text" name="authNumber" style="width:30%" placeholder="인증번호"/>
                    <button id="btnCheckAuth" type="button">인증번호 확인</button>
                </div>
            </div>
        </div>
    </div>
    <div>
        <button id="btnSearchPassword" type="button">비밀번호 찾기</button>
        <button id="btnLogin" type="button">로그인</button>
    </div>
</form>
</body>
</html>