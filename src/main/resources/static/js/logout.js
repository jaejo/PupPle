//function logout() {
//        $.ajax({
//            url: "/logout",
//            type: "post",
//            success: function () {
//                alert("로그아웃");
//                $("#logout").hide();
//                $("#login").show();
//                location.reload(true);
//            },
//            error: function () {
//                alert("서버 요청 실패");
//            }
//        })
//    }

function logout() {
    fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: "include"
    })
    .then(response => response.json())
    .then(data => {
        alert("로그아웃");
        $("#logout").hide();
        $("#login").show();
    })
    .catch(error => console.error('에러 발생:', error));
}