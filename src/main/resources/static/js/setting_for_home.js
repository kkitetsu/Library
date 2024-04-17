/**
 * @author Lee
 * home.html用 JavaScript
 * 本リストの余白を左右に同じように自動に設定する
 */
 function adjustpadding(){
	 var container=document.querySelector('.main_content_box_home');
	 var bookbox=document.querySelector('.book_content_box'); 
	 var containersize=container.offsetWidth-20;
	 var bookboxsize=bookbox.offsetWidth+60; //360px 
//	 var bookboxsize=360; //360px
	 var padding_size=(containersize%bookboxsize)/2;
	 
	 console.log('abcdef');
	 console.log(containersize);
	 console.log(bookboxsize);
	 console.log(padding_size);
	 container.style.paddingLeft=padding_size + 'px';	 
 	 container.style.paddingRight=padding_size + 'px';	 
 }

 window.onload=function(){
	 adjustpadding();
	 window.addEventListener('resize', adjustpadding)
 }
 
 
 		var modal = document.getElementById("myModal");
		var btn = document.getElementById("openModal");
		var span = document.getElementById("closeModal");
		// ボタンがクリックされた時にモーダルを表示
		btn.onclick = function () {
			modal.style.display = "block";
		}

		// ×（クローズアイコン）がクリックされた時にモーダルを非表示
		span.onclick = function () {
			modal.style.display = "none";
		}

		// モーダルの外側をクリックした時にモーダルを非表示
		window.onclick = function (event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		}