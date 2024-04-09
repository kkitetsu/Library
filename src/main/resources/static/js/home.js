const toggler = document.querySelector(".toggle");
// モーダルウィンドウとボタン、クローズアイコンの要素を取得


window.addEventListener("click", event => {
	if (event.target.className == "toggle") {
		document.body.classList.toggle("show-nav");
	} else {
		document.body.classList.remove("show-nav");
	}
});

