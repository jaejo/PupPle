//function includeHtml() {
//    var allElements = $(".include_wrap");
//    Array.prototype.forEach.call(allElements, function(el) {
//        var includePath = el.dataset.includePath;
//
//        if (includePath) {
//            var xhttp = new XMLHttpRequest();
//            xhttp.onreadystatechange = function () {
//                if (this.readyState == 4 && this.status == 200) {
//                    el.outerHTML = this.responseText;
//                }
//            };
//            xhttp.open('GET', includePath, false);
//            xhttp.send();
//        }
//    });
//}
//
//$(document).ready(function() {
//    includeHtml();
//});

//async function includeHtml() {
//  document.querySelectorAll(".include_wrap").forEach(el => {
//    const includePath = el.dataset.includePath;
//    if (includePath) {
//      fetch(includePath)
//        .then(res => res.text())
//        .then(data => {
//          el.innerHTML = data; // el.outerHTML 보단 안전
//        })
//        .catch(err => console.error("include error:", err));
//    }
//  });
//}

async function includeHtml() {
    const allElements = document.querySelectorAll(".include_wrap");
    for (const el of allElements) {
        const includePath = el.dataset.includePath;
        if (includePath) {
            try {
                const res = await fetch(includePath);
                const data = await res.text();
                el.innerHTML = data;
            } catch (err) {
                console.error("include error:", err);
            }
        }
    }
}

document.addEventListener("DOMContentLoaded", async () => {
    await includeHtml();
    await checkLogin();
    if (typeof hideBtn === "function") {
        hideBtn();
    }
});

