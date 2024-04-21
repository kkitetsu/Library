const toggler = document.querySelector(".user_icon_trigger");

window.addEventListener("click", event => {
	if (event.target.classList.contains("user_icon_trigger")) {
		document.body.classList.toggle("show-nav");
	} else {
		document.body.classList.remove("show-nav");
	}
});

$(function() {
	var pageNum = 0;
	var prevWord = "";
	$("#js-search-button").on("click", function() {
		pageNum = pageNum + 1
		var keyWord = $("#js-search-word").val();
		if (prevWord !== keyWord) {
			pageNum = 1;
			$(".lists").empty();
			prevWord = keyWord;
		};
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
					list += '<li class=lists__item>' +
						'<div class=lists__item__inner>' +

						'<img src=${data.Items[i].Item.largeImageUrl} class=lists__item__img${i} alt>' +

						'<p class=lists__title__detail${i}>作品名：　${data.Items[i].Item.title}</p>' +
						'<p class=lists__author__detail${i}>作者　：　${data.Items[i].Item.author}</p>' +
						'<p class=lists__publish__detail${i}>出版社：　${data.Items[i].Item.publisherName}</p>' +
						'<button class=lists__option__detail${i}>選択</button>' +
						'</div>' + '</li>';
				};
				$(".lists").prepend(list);
			} else {
				$(".comment").remove();
				$(".lists").before("<div class='comment'></div>");
				$(".comment").html("<p class='message'>検索結果が見つかりませんでした。<br>別のキーワードで検索して下さい。</p>");
			};
		});
	});
});


