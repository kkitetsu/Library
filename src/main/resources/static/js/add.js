let categorySelect = document.getElementById("category");
let limitSwitch    = document.getElementById("limitdate");
let limitDateInput = document.getElementById("limitDateInput");
let limitdateLabel = document.getElementById("limitdateLabel");


categorySelect.addEventListener('change', function() {

	let categoryType = this.value;

	if (categoryType === "貸出") {
//		 limitSwitch.style.visibility = "visible";
//		 limitSwitch.removeAttribute('hidden');
//		 limitDateInput.removeAttribute('hidden');
		limitdateLabel.classList.remove('hidden-input');
		limitDateInput.classList.remove('hidden-input');

	} else {
		// limitSwitch.style.visibility = "hidden";
		// limitSwitch.setAttribute('hidden', true);
		// limitDateInput.setAttribute('hidden', true);
		limitDateInput.classList.add('hidden-input');
		limitdateLabel.classList.add('hidden-input');
		limitDateInput.valueAsDate = new Date("2999-12-31");
	}
})



document.getElementById('imgUpload').addEventListener('change', function() {
	const file = this.files[0];
	var imgPreview = document.getElementById("previewImage");
	
	//楽天API(added by shunsukekuzawa)
	const apiPreview = document.getElementById("apiPreview");
	const apiTitle = document.getElementById("js-search-word");
	const apiURL = document.getElementById("apiUrl");
	
	if (file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			//楽天API(added by shunsukekuzawa)
			apiTitle.value = "";
			previewImage.src = e.target.result;
			imgPreview.style.display = 'block';
			apiPreview.src = "";
			apiURL.value = "";
			apiPreview.style.display = 'none';

			const placeholder = document.querySelector('.image-upload-placeholder');
			if (placeholder) {
				placeholder.style.display = 'none';
			}
		};
		reader.readAsDataURL(file);
	}
});



document.getElementById('imgUpload').addEventListener('change', function(event) {
  	if(event.target.files.length > 0) {
    	document.getElementById('existingImage').style.display = 'none';
  	}
  	
});




document.addEventListener('DOMContentLoaded', function() {
  // アップロードされた画像を表示するためのイベントリスナーを設定します。
  document.getElementById('imgUpload').addEventListener('change', function(event) {
    if (event.target.files.length > 0) {
      var file = event.target.files[0]; // アップロードされたファイルを取得します。
      var reader = new FileReader(); // FileReaderのインスタンスを作成します。

      // ファイルの読み込みが終了したら実行されるイベントハンドラーを定義します。
      reader.onload = function(e) {
        var previewImage = document.getElementById('previewImage'); // 表示するimg要素を取得します。
        previewImage.src = e.target.result; // 読み込んだ画像データをsrc属性に設定します。
        previewImage.style.display = 'block'; // img要素を表示状態にします。
      };

      // ファイルの読み込みを実行します。
      reader.readAsDataURL(file);
    }
  });
});
