let dialog;

document.addEventListener('DOMContentLoaded', function () {
    dialog = document.getElementById("dialog");
});

function openDialog() {
    dialog.showModal();
}

function closeDialog() {
    dialog.close();
}