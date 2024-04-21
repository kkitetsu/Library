var modal = document.getElementById("apiModal");
var imgPreview = document.getElementById("imagePreview");
var apiPreview = document.getElementById("apiPreview");
var btn = document.getElementById("openApiModal");
var span = document.getElementById("closeApiModal");

// ボタンがクリックされた時にモーダルを表示
btn.onclick = function() {
	alert("hello");
	var pageNum = 0;
	var prevWord = "";
	pageNum = pageNum + 1
	var keyWord = $("#js-search-word").val();
	
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
				hits: 20,
			}
		}).done(function(data) {
			if (data.count > 0) {
				$(".comment").remove();
				var list = "";
				for (var i = 0; i < data.Items.length; i++) {
					list += `<li class=lists__item>` +
						`<div class=lists__item__inner>` +
						`<a href=${data.Items[i].Item.itemUrl} id=lists__item__link target=_blank>` +
						`<img src=${data.Items[i].Item.largeImageUrl} id=lists__item__img${i} alt>` + `</a>` +
						`<label>作品名：　</label><p id=lists__title__detail${i}>${data.Items[i].Item.title}</p>` +
						`<label>作者：　</label><p id=lists__author__detail${i}>${data.Items[i].Item.author}</p>` +
						`<label>出版社：　</label><p id=lists__publisher__detail${i}>${data.Items[i].Item.publisherName}</p>` +
						`<button id=lists__option__detail${i} name=${i}>選択</button>` +
						`</div>` + `</li>`;
				};
				$(".lists").prepend(list);
			} else {
				$(".comment").remove();
				$(".lists").before("<div class='comment'></div>");
				$(".comment").html("<p class='message'>検索結果が見つかりませんでした。<br>別のキーワードで検索して下さい。</p>");
			};
		});
		modal.style.display = "block";
	}
}

const listRootElm = document.getElementById('apiModalItems');
listRootElm.addEventListener('click', (e) => {
	const clickElement = e.target;
	const name = clickElement.getAttribute("name");
	const content = document.getElementById("lists__title__detail"+ name);
	const url = document.getElementById("lists__item__img"+ name);
	
	const title = document.getElementById("title");
	const preview = document.getElementById("apiPreview");
	if (name != null) {
		title.value = content.textContent;
		preview.src = url.getAttribute("src");
		modal.style.display = "none";
		imgPreview.style.display = "none";
		apiPreview.style.display ="block";
	}
});

// ×（クローズアイコン）がクリックされた時にモーダルを非表示
span.onclick = function() {
	modal.style.display = "none";
}

// モーダルの外側をクリックした時にモーダルを非表示
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
	}
}