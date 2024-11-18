function logout() {
        $.ajax({
            url: "/logout",
            type: "post",
            success: function () {
                alert("로그아웃");
                $("#logout").hide();
//                $("#showId").hide();
                $("#login").show();
            },
            error: function () {
                alert("서버 요청 실패");
            }
        })
    }