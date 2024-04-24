let categorySelect = document.getElementById("category");
let limitSwitch = document.getElementById("limitdate");
let limitDateInput = document.getElementById("limitDateInput");
let limitdateLabel = document.getElementById("limitdateLabel");

function toggleLimitDateInput() {
  let categoryType = categorySelect.value;

  if (categoryType === "貸出") {
    limitSwitch.style.display = "";
    limitDateInput.removeAttribute('hidden');
    limitDateInput.setAttribute('required', ''); // Make it required
  } else {
    limitSwitch.style.display = "none";
    limitDateInput.setAttribute('hidden', true);
    limitDateInput.removeAttribute('required'); // Not required
    limitDateInput.value = ""; // Clear value
  }
}

// Call this function on page load to set the initial state
toggleLimitDateInput();

// Set up the event listener for changes on the category select box
categorySelect.addEventListener('change', toggleLimitDateInput);




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





document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('onethird_size').addEventListener('click', function(event) {
    const title = document.querySelector('input[name="title"]').value.trim();
    const category = document.querySelector('select[name="category"]').value;
    const limitdate = document.querySelector('input[name="limitdate"]').value.trim();

    // タイトルのバリデーション
    if (!title) {
      alert('タイトルを入力してください。');
      event.preventDefault(); // フォーム送信を中断
      return false;
    }

    // 貸出期限のバリデーション
    if (category === '貸出' && !limitdate) {
      alert('貸出期限を入力してください。');
      event.preventDefault(); // フォーム送信を中断
      return false;
    }
  });
});



