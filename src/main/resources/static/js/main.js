function showAlert() {
    alert("The button was clicked!");
}

$(document).ready(function() {
    $(".alert").fadeTo(2000, 0, function() { $(this).remove(); });
  });