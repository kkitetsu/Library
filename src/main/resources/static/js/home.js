const toggler = document.querySelector(".user_icon_trigger");

window.addEventListener("click", event => {
	if (event.target.classList.contains("user_icon_trigger")) {
		document.body.classList.toggle("show-nav");
	} else {
		document.body.classList.remove("show-nav");
	}
});

// Added by kk in order to handle approve/reject extension of the return date request
document.addEventListener('DOMContentLoaded', function() {
    const approveBtns = document.querySelectorAll('.approve-btn');
    const denyBtns = document.querySelectorAll('.deny-btn');

    approveBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            handleInnerFormSubmission(btn, 'approve');
        });
    });

    denyBtns.forEach(function(btn) {
        btn.addEventListener('click', function() {
            handleInnerFormSubmission(btn, 'deny');
        });
    });
});

function handleInnerFormSubmission(btn, action) {
    const notificationBlock = btn.closest('.notification');
    const innerForm = document.getElementById('inner-form');

    const bookId = notificationBlock.querySelector('[name="bookId"]').value;
    const bookTitle = notificationBlock.querySelector('[name="bookTitle"]').value;
    const newDateRequested = notificationBlock.querySelector('[name="newDateRequested"]').value;
    const borrowerName = notificationBlock.querySelector('[name="borrowerName"]').value;
    const transId = notificationBlock.querySelector('[name="transId"]').value;

    document.getElementById('inner-book-id').value = bookId;
    document.getElementById('inner-book-title').value = bookTitle;
    document.getElementById('inner-new-date-requested').value = newDateRequested;
    document.getElementById('inner-borrower-name').value = borrowerName;
    document.getElementById('inner-book-transId').value = transId;
    document.getElementById('inner-request-response').value = action;

    innerForm.submit();
}
// until here added by kk


//added by Aru below
// 編集完了モーダル
document.addEventListener('DOMContentLoaded', function() {
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has('editSuccess')) {
    openModal();
    var bookTitle = urlParams.get('bookTitle'); // 本のタイトルを取得
    var message = `${bookTitle}の編集が完了しました`;
    document.getElementById('editNotification').textContent = message;
  }

  function openModal() {
    document.getElementById('editModal').style.display = 'block';
  }

  function closeModal() {
    document.getElementById('editModal').style.display = 'none';
  }

  // 確認ボタン
  var close = document.getElementById("closeEdit");
  if (close) {
    close.addEventListener('click', closeModal);
  }

  // モーダルの外側をクリックした場合
  window.onclick = function(event) {
    var modal = document.getElementById('editModal');
    if (event.target == modal) {
      closeModal();
    }
  };
});



//削除完了モーダル
document.addEventListener('DOMContentLoaded', function() {
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has('deleteSuccess')) {
    openModal();
    var bookTitle = urlParams.get('bookTitle'); 
    var message = `${bookTitle}の削除が完了しました`;
    document.getElementById('deleteNotification').textContent = message;
  }

  function openModal() {
    document.getElementById('deleteModal').style.display = 'block';
  }

  function closeModal() {
    document.getElementById('deleteModal').style.display = 'none';
  }

  var close = document.getElementById("closeDelete");
  if (close) {
    close.addEventListener('click', closeModal);
  }

  window.onclick = function(event) {
    var modal = document.getElementById('deleteModal');
    if (event.target == modal) {
      closeModal();
    }
  };
});




//出品完了モーダル
document.addEventListener('DOMContentLoaded', function() {
  var urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has('exhibitSuccess')) {
    openModal();
    var bookTitle = urlParams.get('bookTitle'); 
    var message = `${bookTitle}の出品が完了しました`;
    document.getElementById('exhibitNotification').textContent = message;
  }

  function openModal() {
    document.getElementById('exhibitModal').style.display = 'block';
  }

  function closeModal() {
    document.getElementById('exhibitModal').style.display = 'none';
  }

  var close = document.getElementById("closeExhibit");
  if (close) {
    close.addEventListener('click', closeModal);
  }

  window.onclick = function(event) {
    var modal = document.getElementById('exhibitModal');
    if (event.target == modal) {
      closeModal();
    }
  };
});


//ハイフン表示
window.addEventListener('DOMContentLoaded', (event) => {
  var limitDateContent = document.getElementById('limitdateContent');
  if (!limitDateContent.textContent.trim()) {
    limitDateContent.textContent = '-';
  }
});
