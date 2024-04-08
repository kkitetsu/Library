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