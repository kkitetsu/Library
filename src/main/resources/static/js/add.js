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
	if (file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			const previewImage = document.getElementById('previewImage');
			previewImage.src = e.target.result;
			previewImage.style.display = 'block';

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


document.getElementById('imgUpload').addEventListener('change', function(event) {
  if (event.target.files.length > 0) {
    const reader = new FileReader();
    reader.onload = function(e) {
      const previewImage = document.getElementById('previewImage');
      previewImage.src = e.target.result;
      previewImage.style.maxWidth = '100%';
      previewImage.style.maxHeight = '100%';
      previewImage.style.width = '100%';
      previewImage.style.height = '100%';
      previewImage.style.top = '0';
      previewImage.style.left = '0';
      previewImage.style.objectFit = 'contain';
      previewImage.style.position = 'relative';
      previewImage.style.zIndex = '5';
      previewImage.style.display = 'block'; 
    };
    reader.readAsDataURL(event.target.files[0]);
  }
});