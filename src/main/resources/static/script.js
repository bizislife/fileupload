const dropArea = document.getElementById('drop-area');
const fileElem = document.getElementById('fileElem');
const uploadStatus = document.getElementById('upload-status');
const uploadedFilesList = document.getElementById('uploaded-files');

function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
}

['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
    dropArea.addEventListener(eventName, preventDefaults, false);
});

dropArea.addEventListener('dragover', () => dropArea.classList.add('dragover'));
dropArea.addEventListener('dragleave', () => dropArea.classList.remove('dragover'));
dropArea.addEventListener('drop', (e) => {
    dropArea.classList.remove('dragover');
    handleFiles(e.dataTransfer.files);
});

dropArea.addEventListener('click', () => fileElem.click());
fileElem.addEventListener('change', () => handleFiles(fileElem.files));

function handleFiles(files) {
    [...files].forEach(uploadFile);
}

function uploadFile(file) {
    const statusBar = document.createElement('div');
    statusBar.className = 'status-bar';
    statusBar.innerHTML = `
        <span class="file-name">${file.name}</span>
        <div class="bar"><div class="bar-inner"></div></div>
        <span class="percent">0%</span>
    `;
    uploadStatus.appendChild(statusBar);

    const formData = new FormData();
    formData.append('files', file);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/files/upload');

    xhr.upload.addEventListener('progress', (e) => {
        if (e.lengthComputable) {
            const percent = Math.round((e.loaded / e.total) * 100);
            statusBar.querySelector('.bar-inner').style.width = percent + '%';
            statusBar.querySelector('.percent').textContent = percent + '%';
        }
    });

    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                setTimeout(() => {
                    uploadStatus.removeChild(statusBar);
                    refreshUploadedFiles();
                }, 500);
            } else {
                statusBar.querySelector('.percent').textContent = 'Error';
                statusBar.querySelector('.bar-inner').style.background = '#dc3545';
            }
        }
    };

    xhr.send(formData);
}

function refreshUploadedFiles() {
    fetch('/api/files')
        .then(res => res.json())
        .then(files => {
            uploadedFilesList.innerHTML = '';
            files.forEach(file => {
                const li = document.createElement('li');
                li.innerHTML = `
                    <span class="file-name">${file.fileName}</span>
                    <button class="remove-btn" data-id="${file.id}">Remove</button>
                `;
                li.querySelector('.remove-btn').addEventListener('click', () => removeFile(file.id));
                uploadedFilesList.appendChild(li);
            });
        });
}

function removeFile(id) {
    fetch(`/api/files/${id}`, { method: 'DELETE' })
        .then(res => {
            if (res.ok) refreshUploadedFiles();
        });
}

// Initial load
refreshUploadedFiles();
