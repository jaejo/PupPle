function base64ToFile(base64, filename) {
    const arr = base64.split(',');
    const mime = arr[0].match(/:(.*?);/)[1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);

    while(n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }

    return new File([u8arr], filename, { type: mime });
}

function getImageExtension(base64) {
  const match = base64.match(/^data:image\/([a-zA-Z0-9+]+);base64,/);

  if (match && match[1]) {
    return match[1].toLowerCase(); // ex: "png", "jpeg", "webp"
  }
  return null;
}

async function extractImagesFromQuill() {
    const delta = quill.getContents();
    const images = [];

    let imageIndex = 0;
    //await don't work in forEach
    for(const op of delta.ops) {
        if(op.insert && op.insert.image) {
            if(op.insert.image.startsWith("data:image")) {
                const ext = getImageExtension(op.insert.image);
                const filename = `image_${imageIndex++}` + `.` + ext;
                const file = base64ToFile(op.insert.image, filename);
                images.push(file);
            } else if(op.insert.image.startsWith("/")) {
                const ext = op.insert.image.split(".")[1];
                const filename = `image_${imageIndex++}` + `.` + ext;
                const file = await relativeUrlToFile(op.insert.image, filename);
                images.push(file);
            }
        }
    }
    return images;
}

function getFirstImageFormDelta(delta) {
    for(const op of delta.ops) {
        if(op.insert && op.insert.image) {
            return op.insert.image;
        }
    }
    return null;
}

async function base64OrRelativeUrlToFile(src) {
    const filename = `thumbnail.${getImageExtension(src)}`;
    if(src.startsWith('data:')) return base64ToFile(src, filename);
    else if(src.startsWith('/')) return await relativeUrlToFile(src, filename);
}

async function getThumbnail() {
    const thumbnail = document.getElementById('thumbnail');
    if(thumbnail) {
        return base64OrRelativeUrlToFile(thumbnail.src);
    } else {
        const firstImage = getFirstImageFormDelta(quill.getContents());

        //not only writing
        if(firstImage != null) {
            return base64OrRelativeUrlToFile(firstImage);
        } else {
            return null;
        }
    }
}

//relative address to absolute address
async function relativeUrlToFile(relativeUrl, filename) {
    const absoluteUrl = new URL(relativeUrl, window.location.origin).href;

    const response = await fetch(absoluteUrl);
    const blob = await response.blob();

    return new File([blob], filename, { type: blob.type });
}

const save_element = document.getElementById('save-delta');

if(save_element) {
    document.getElementById('save-delta').addEventListener('click', async function() {
//        const quillContent = quill.getContents();
//        const content = quillContent.ops.length === 1 && quillContent.ops[0].insert.trim() === '\n';
        const title = titleInput.value.trim();
        const content = quill.getText().trim();
        const hasImage = quill.getContents().ops.some(op => op.insert && op.insert.image);

        if(!title || (!content && !hasImage)) {
            Swal.fire({
                icon: 'warning',
                title: '주의',
                text: '제목 또는 내용이 비어 있습니다!',
              });
            if(!title) titleInput.classList.add('empty');
            else titleInput.classList.remove('empty');

            if(!content && !hasImage) editor.classList.add('empty');
            else editor.classList.remove('empty');
            return;
        } else {
            titleInput.classList.remove('empty');
            editor.classList.remove('empty');
        }
        addImageStyle(quill);
        //both of all return promise, so formData put promise not File
        const images = await extractImagesFromQuill();
        const thumbnailFile = await getThumbnail();

        images.forEach(file => formData.append('file', file));
        if(thumbnailFile != null) formData.append('thumbnail', thumbnailFile);

        var data = {
            title : $("#title").val(),
            delta : quill.getContents(),
            boardType : $("#boardType").val()
        }

        formData.append("info", new Blob([JSON.stringify(data)], {type: "application/json"}));

        fetch('/newBoardV2', {
            method: 'POST',
            body: formData,
        })
        .then(response => response.json())
        .then(data => {
            console.log('저장완료:', data);
            location.replace("/boards?boardType=" + data.boardType);
        })
        .catch(error => {
            console.log('저장중 오류 발생:', error);
        });
    });
}

const update_element = document.getElementById('update-delta');

if(update_element) {
    document.getElementById('update-delta').addEventListener('click', async function() {
        addImageStyle(quill);
        //both of all return Promise, so formData put promise not File
        //To use Promise result always need await in async function
        //use for...of or Promise.all
        const images = await extractImagesFromQuill();
        const thumbnailFile = await getThumbnail();

        images.forEach(file => formData.append('file', file));

        console.log(formData);

        formData.append('thumbnail', thumbnailFile);

        var data = {
            no : $("#no").val(),
            title : $("#title").val(),
            delta : quill.getContents(),
            boardType : $("#boardType").val()
        }

        formData.append("info", new Blob([JSON.stringify(data)], {type: "application/json"}));

        fetch('/updateQuill', {
            method: 'POST',
            body: formData,
        })
        .then(response => response.json())
        .then(data => {
            console.log('수정완료:', data);
            location.replace(`/detailV2?boardType=${data.boardType}&no=${data.no}`);
        })
        .catch(error => {
            console.log('저장중 오류 발생:', error);
        });
    });
}