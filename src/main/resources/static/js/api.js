var apiModal = document.getElementById("apiModal");
var imgPreview = document.getElementById("previewImage");
var apiPreview = document.getElementById("apiPreview");
var btn = document.getElementById("openApiModal");
var span = document.getElementById("closeApiModal");

// ボタンがクリックされた時にモーダルを表示
btn.onclick = function() {
	const apiKeyword = document.getElementById("titleApiModal");
	var pageNum = 0;
	var prevWord = "";
	pageNum = pageNum + 1
	var keyWord = $("#js-search-word").val();
	apiKeyword.textContent = "キーワード ： "+keyWord;
	if (prevWord !== keyWord) {
		pageNum = 1;
		$(".lists").empty();
		prevWord = keyWord;
		$.ajax({
			type: "GET",
			url: "https://app.rakuten.co.jp/services/api/BooksTotal/Search/20170404",
			data: {
				applicationId: "1053309271580885189",
				keyword: keyWord,
				format: "json",
				bookGenreId: "001",
				page: pageNum,
				hits: 30,
			}
		}).done(function(data) {
			if (data.count > 0) {
				$(".comment").remove();
				var list = "";
				for (var i = 0; i < data.Items.length; i++) {
					list += `<li class=lists__item>` +
						`<div class=lists__item__inner>` +
						`<a href=${data.Items[i].Item.itemUrl} id=lists__item__link target=_blank>` +
						`<img src=${data.Items[i].Item.largeImageUrl} id=lists__item__img${i} alt>` + `</a><br>` 
						+`<table　width="600">`
							+`<tr>`
								+`<td><label>作品名：</label></td>`
								+`<td><span id=lists__title__detail${i}>${data.Items[i].Item.title}</span></td><br>`
							+`</tr>`
							+`<tr>`
								+`<td><label>作者：</label></td>`
								+`<td><span id=lists__author__detail${i}>${data.Items[i].Item.author}</span></td><br>`
							+`</tr>`
							+`<tr>`
								+`<td><label>出版社：</label></td>`
								+`<td><span id=lists__publisher__detail${i}>${data.Items[i].Item.publisherName}</span><br>` 
								+`</td>`
							+`</tr>`
						+`</table>`+
						`<button class="api_btn" id=lists__option__detail${i} name=${i}>選択</button>` +
						`</div>` + `</li>`;
				};
				$(".lists").prepend(list);
			} else {
				$(".comment").remove();
				$(".lists").before("<div class='comment'></div>");
				$(".comment").html("<p class='message'>検索結果が見つかりませんでした。<br>別のキーワードで検索して下さい。</p>");
			};
		});
		apiModal.style.display = "block";
	}
}

const listRootElm = document.getElementById('apiModalItems');
listRootElm.addEventListener('click', (e) => {
	const clickElement = e.target;
	const name = clickElement.getAttribute("name");
	const content = document.getElementById("lists__title__detail"+ name);
	const message = document.getElementById("uploadMessage");
	const url = document.getElementById("lists__item__img"+ name);
	const title = document.getElementById("title");	
	const apiPreview = document.getElementById("apiPreview");
	const preview = document.getElementById("previewImage");
	const apiURL = document.getElementById("apiUrl");
	if (name != null) {
		title.value = content.textContent;
		apiPreview.src = url.getAttribute("src");
		preview.src = "";
		apiModal.style.display = "none";
		message.style.display = "none";
		imgPreview.style.display = "none";
		apiPreview.style.display ="block";
		apiURL.value = url.getAttribute("src");
	}
});

// ×（クローズアイコン）がクリックされた時にモーダルを非表示
span.onclick = function() {
	apiModal.style.display = "none";
}

// モーダルの外側をクリックした時にモーダルを非表示
window.onclick = function(event) {
	if (event.target == modal) {
		apiModal.style.display = "none";
	}
}