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


//document.addEventListener('DOMContentLoaded', function() {
//	document.getElementById('small_size').addEventListener('click', function() {
//		const author = encodeURI(document.getElementById('book_name').value);
//		if (author) {
//			fetchBooks(author);
//		}
//	});
//});
//console.log("Status Code:");
//
//function fetchBooks(author) {
//	const request = new XMLHttpRequest();
//	const appId = '1061318932140893161'; // 実際のアプリケーションIDに置き換えてください
//	const testurl = `https://app.rakuten.co.jp/services/api/BooksBook/Search/20170404?format=json&author=${author}&applicationId=${appId}`;
//
//	request.open('GET', testurl, true);
//	request.addEventListener('load', (event) => {
//		if (event.target.status === 200) {
//			const data = JSON.parse(event.target.responseText);
//			console.log(data);
//			displayResults(data.Items);
//		} else {
//			console.error('Error:', event.target.status, event.target.statusText);
//		}
//	});
//	request.send();
//}
//
//function displayResults(items) {
//	const resultsContainer = document.getElementById('results');
//	resultsContainer.innerHTML = ''; // 結果表示エリアのクリア
//
//	items.forEach(item => {
//		const book = item.Item;
//		const element = document.createElement('div');
//		element.innerHTML = `
//            <h2>${book.title}</h2>
//            <img src="${book.mediumImageUrl}" alt="${book.title}">
//            <p>${book.author}</p>
//            <p>${book.price}円</p>
//        `;
//		resultsContainer.appendChild(element);
//	});
//}