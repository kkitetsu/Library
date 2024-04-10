const toggler = document.querySelector(".user_icon_trigger");

window.addEventListener("click", event => {
    if (event.target.classList.contains("user_icon_trigger")) {
        document.body.classList.toggle("show-nav");
    } else {
        document.body.classList.remove("show-nav");
    }
});

document.addEventListener('DOMContentLoaded', function() {
	document.getElementById('small_size').addEventListener('click', function() {
		const author = encodeURI(document.getElementById('book_name').value);
		if (author) {
			fetchBooks(author);
		}
	});
});
function fetchBooks(author) {
	//	const request = new XMLHttpRequest();
	const appId = '1061318932140893161'; // 実際のアプリケーションIDに置き換えてください
	const testurl = `https://app.rakuten.co.jp/services/api/BooksBook/Search/20170404?format=json&author=${author}&applicationId=${appId}`;
	$.ajax({
		url: testurl,
		dataType: "json"
	}).done(function(data) {
		//		displayResults(data.Items);
		const imgURL = document.getElementById('book_img');
		imgURL.src = data.Items[0].Item.largeImageUrl;
	});
}

function displayResults(items) {
	const resultsContainer = document.getElementById('results');
	resultsContainer.innerHTML = ''; // 結果表示エリアのクリア

	items.forEach(item => {
		const book = item.Item;
		const element = document.createElement('div');
		element.innerHTML = `
            <h2>${book.title}</h2>
            <img src="${book.mediumImageUrl}" alt="${book.title}">
            <p>${book.author}</p>
            <p>${book.price}円</p>
        `;
		resultsContainer.appendChild(element);
	});
}