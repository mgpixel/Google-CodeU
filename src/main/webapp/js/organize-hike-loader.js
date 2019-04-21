// Get ?user=XYZ parameter value to tie hike to user.
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/*
 * Restricts input for the given textbox to the given inputFilter.
 * From stackoverflow: https://stackoverflow.com/questions/469357/html-text-input-allow-only-numeric-input
 * Works pretty well for our purposes.
 */
function setInputFilter(textbox, inputFilter) {
  ["input", "keydown", "keyup", "mousedown", "mouseup", "select", "contextmenu", "drop"].forEach(function (event) {
    textbox.addEventListener(event, function () {
      if (inputFilter(this.value)) {
        this.oldValue = this.value;
        this.oldSelectionStart = this.selectionStart;
        this.oldSelectionEnd = this.selectionEnd;
      } else if (this.hasOwnProperty("oldValue")) {
        this.value = this.oldValue;
        this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
      }
    });
  });
}

/**
 * Load the organizeHike.html UI.
 */
function buildOrganizeUI() {
  checkCorrectUser(parameterUsername);
  addLoginOrLogoutLinkToNavigation();
  setInputFilter(document.getElementById('numInput'), function (val) {
    return /^\d*$/.test(val) && (val === "" || parseInt(val) <= 100);
  })
}