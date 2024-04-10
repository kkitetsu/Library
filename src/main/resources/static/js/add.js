let categorySelect = document.getElementById("category");
let limitSwitch = document.getElementById("limitdate");

categorySelect.addEventListener('change', function() {

	let categoryType = this.value;

	if (categoryType === "貸出") {
		limitSwitch.style.visibility = "visible";

	} else {
		limitSwitch.style.visibility = "hidden";
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
