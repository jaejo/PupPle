function logout() {
    fetch('/logout', {
      method: 'POST',
      credentials: "include",
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(response => response.json())
      .then(data => {
        alert(data.msg);
        window.location.href = "/";
      })
      .catch(error => {
        console.error('로그아웃 요청 실패:', error);
      });
}