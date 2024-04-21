let categorySelect = document.getElementById("category");
let limitSwitch    = document.getElementById("limitdate");
let limitDateInput = document.getElementById("limitDateInput");
let limitdateLabel = document.getElementById("limitdateLabel");

categorySelect.addEventListener('change', function() {

	let categoryType = this.value;

	if (categoryType === "貸出") {
		// limitSwitch.style.visibility = "visible";
		// limitSwitch.removeAttribute('hidden');
		// limitDateInput.removeAttribute('hidden');
		limitdateLabel.classList.remove('hidden-input');
		limitDateInput.classList.remove('hidden-input');
		limitDateInput.setAttribute('required', 'true');

	} else {
		// limitSwitch.style.visibility = "hidden";
		// limitSwitch.setAttribute('hidden', true);
		// limitDateInput.setAttribute('hidden', true);
		limitDateInput.classList.add('hidden-input');
		limitdateLabel.classList.add('hidden-input');
		limitDateInput.removeAttribute('required');
		limitDateInput.valueAsDate = new Date("2999-12-31");
	}
})


document.getElementById('imgUpload').addEventListener('change', function() {
	const file = this.files[0];
	console.log(file);
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
