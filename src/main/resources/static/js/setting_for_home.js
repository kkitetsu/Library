/**
 * @author Lee
 * home.html用 JavaScript
 * 本リストの余白を左右に同じように自動に設定する
 */
function adjustpadding() {
	var container = document.querySelector('.main_content_box_home');
	var bookbox = document.querySelector('.book_content_box');
	var containersize = container.offsetWidth - 20;
	var bookboxsize = bookbox.offsetWidth + 60; //360px 
	//	 var bookboxsize=360; //360px
	var padding_size = (containersize % bookboxsize) / 2;

	console.log('abcdef');
	console.log(containersize);
	console.log(bookboxsize);
	console.log(padding_size);
	container.style.paddingLeft = padding_size + 'px';
	container.style.paddingRight = padding_size + 'px';
}

window.onload = function() {
	adjustpadding();
	window.addEventListener('resize', adjustpadding)
}


var modal = document.getElementById("myModal");
var btn = document.getElementById("openModal");
var span = document.getElementById("closeModal");

// ボタンがクリックされた時にモーダルを表示
btn.onclick = function() {
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
						`<a href=${data.Items[i].Item.itemUrl} class=lists__item__link target=_blank>` +
						`<img src=${data.Items[i].Item.largeImageUrl} class=lists__item__img${i} alt>` + `</a>` +
						`<p class=lists__item__detail${i}>作品名：　${data.Items[i].Item.title}</p>` +
						`<p class=lists__item__detail${i}>作者　：　${data.Items[i].Item.author}</p>` +
						`<p class=lists__item__detail${i}>出版社：　${data.Items[i].Item.publisherName}</p>` + 
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
