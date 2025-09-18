fetch("/auth/me", {
    method: "GET",
    credentials: "include"
})
.then(res => res.json())
.then(data => {
    if (data.loggedIn) {
        document.getElementById('login').style.display = 'none';
        console.log("로그인됨:", data.username);
    } else {
        document.getElementById('logout').style.display = 'none';
        console.log("로그인 안됨");
    }
});

