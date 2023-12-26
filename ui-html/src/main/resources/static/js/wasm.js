document.getElementById('uploadButton').addEventListener('click', function() {
    document.getElementById('fileInput').click();
});

document.getElementById('fileInput').addEventListener('change', function() {
    var file = this.files[0];
    if (file) {
        var reader = new FileReader();
        reader.readAsText(file, "UTF-8");
        reader.onload = function(evt) {
            document.getElementById('editor').value = evt.target.result;
        }
        reader.onerror = function(evt) {
            alert("파일을 읽는 동안 오류가 발생했습니다.");
        }
    }
});