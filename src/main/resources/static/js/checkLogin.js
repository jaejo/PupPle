//function checkLogin() {
//    fetch("/auth/me", { credentials: "include" })
//    .then(res => res.json())
//    .then(data => {
//        if (data.loggedIn) {
//            console.log('로그인 됨: ' + data.username);
//            document.getElementById('login').style.display = 'none';
//        } else {
//            document.getElementById('logout').style.display = 'none';
//        }
//    });
//}
var loginUser;

async function checkLogin() {
    try {
        const res = await fetch("/auth/me", { credentials: "include" });
        const data = await res.json();
        if (data.loggedIn) {
            console.log('로그인 됨: ' + data.username);
            document.getElementById('login').style.display = 'none';
            loginUser = data.username;
        } else {
            console.log('로그인 되지 않음');
            document.getElementById('logout').style.display = 'none';
        }
    } catch (error) {
        console.error("로그인 체크 에러:", error);
    }
}
