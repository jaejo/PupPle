//Quill.register('modules/imageResize', window.ImageResize);

const quill = new Quill('#editor', {
    theme: 'snow',
    placeholder: '여기에 글을 작성해주세요! 드롭 혹은 툴바를 통해 이미지를 삽입하세요.',
    modules: {
        toolbar: '#toolbar',
        imageResize: {
            modules: ['Resize', 'DisplaySize', 'Toolbar']
        }
    }
});

const editorContainer = document.querySelector('#editor');

function addImageStyle(quill) {
    const images = quill.root.querySelectorAll('img');

    images.forEach(img => {
        const style = img.style;
        let align = null;

        if(style.float === 'left') align = 'left';
        else if(style.float === 'right') align = 'right';
        else if(style.display === 'block' && style.margin === 'auto') align = 'center';

        console.log(align);

        if (align) {
            const blot = Quill.find(img);               // 이미지 blot 객체 찾기
            const index = quill.getIndex(blot);         // 위치 추출
            quill.formatLine(index, 1, { align });      // Delta에 스타일 반영
        }
    });
}



editorContainer.addEventListener('drop', function (e) {
  e.preventDefault();
  if (!e.dataTransfer || !e.dataTransfer.files || e.dataTransfer.files.length === 0) return;

  const file = e.dataTransfer.files[0];
  if (!file.type.startsWith('image/')) return;

  const reader = new FileReader();
  reader.onload = function (event) {
    const range = quill.getSelection();
    const base64 = event.target.result;
    quill.insertEmbed(range ? range.index : 0, 'image', base64);
  };
  reader.readAsDataURL(file);
});

let previousImages = [];

//preview quill 이미지 동기화
function syncPreviewWithEditor() {
    const currentContents = quill.getContents();
    const currentImages = [];

    //에디터 내의 이미지 추출
    currentContents.ops.forEach(op => {
        if(op.insert && op.insert.image) {
            currentImages.push(op.insert.image);
        }
    });

    const preview = document.getElementById("preview");

    const newImages = currentImages.filter(img => !previousImages.includes(img));
    newImages.forEach(src =>{
        const img = document.createElement("img");
        img.src = src;
        img.id = "thumbnail";
        img.setAttribute("data-src", src);
        img.classList.add("embed-img");

//        img.addEventListener("dblclick", () => {
//            removeImageFromEditor(src);
//        });

        preview.appendChild(img);
    });

    const removedImages = previousImages.filter(src => !currentImages.includes(src));
        removedImages.forEach(src => {
            const img = preview.querySelector(`img[data-src="${src}"]`);
            if (img) preview.removeChild(img);
    });
    initDragAndDrop();

    previousImages = currentImages;
}

//function removeImageFromEditor(srcToRemove) {
//    const Delta = quill.constructor.import("delta");
//    const delta = quill.getContents();
//    let newDelta = new Delta();
//
//    delta.ops.forEach(op => {
//        if(op.insert && op.insert.image === srcToRemove) {
//        } else {
//            newDelta.insert(op.insert, op.attributes || {});
//        }
//    });
//
//    quill.setContents(newDelta);
//}

quill.on("text-change", (delta, oldDelta, source) => {
    const currentImages = extractImageSources(quill.getContents());

    const isChanged = JSON.stringify(currentImages) !== JSON.stringify(previousImages);

    if(isChanged) {
        syncPreviewWithEditor(currentImages);
        previousImages = currentImages;
    }
});

function extractImageSources(delta) {
    const sources = [];

    delta.ops.forEach(op => {
        if(op.insert && op.insert.image) {
            sources.push(op.insert.image);
        }
    });

    return sources;
}

document.getElementById('clear-editor').addEventListener('click', function() {
    if(confirm('정말 내용을 전부 삭제하시겠습니까?')) {
        quill.deleteText(0, quill.getLength());
        quill.setSelection(0);
    }
});

//document.getElementById('save-html').addEventListener('click', function() {
//    const html = quill.root.innerHTML;
//    document.getElementById('output').innerText = html;
//    console.log('HTML: ', html);
//});

//document.getElementById('save-delta').addEventListener('click', function() {
//    fetch('/newBoardV2', {
//        method: 'POST',
//        headers: {
//            'Content-Type': 'application/json',
//        },
//        body: JSON.stringify({
//            html: quill.root.innerHTML,
//            delta: quill.getContents(),
//            boardType: $("#boardType").val()
//        }),
//    })
//    .then(response => response.json())
//    .then(data => {
//        console.log('저장완료:', data);
//        location.replace("/boards?boardType=" + data.boardType);
//    })
//    .catch(error => {
//        console.log('저장중 오류 발생:', error);
//    });
//});

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
    return match[1].toLowerCase(); // 예: "png", "jpeg", "webp"
  }
  return null;
}

function extractImagesFromQuill() {
    const delta = quill.getContents();
    const images = [];

    let imageIndex = 0;
    delta.ops.forEach(op => {
        if(op.insert && op.insert.image && op.insert.image.startsWith("data:image")) {
            const ext = getImageExtension(op.insert.image);
            const filename = `image_${imageIndex++}` + `.` + ext;
            const file = base64ToFile(op.insert.image, filename);
            images.push(file);
        }
    });

    return images;
}

function getThumbnail() {
    const thumbnail = document.getElementById('thumbnail');
    const src = thumbnail.src;
    const ext = getImageExtension(src);
    const filename = `thumbnail` + `.` + ext;
    const file = base64ToFile(src, filename);

    return file;
}

//delta 확인용
//document.getElementById('save-delta').addEventListener('click', function () {
//    addImageStyle(quill);
//    const delta = quill.getContents();
//    document.getElementById('output').innerText = JSON.stringify(delta, null, 2);
//    console.log('Delta:', delta);
//});

document.getElementById('save-delta').addEventListener('click', function() {
    addImageStyle(quill);
    const images = extractImagesFromQuill();

    images.forEach((file, i) => {
        formData.append('file', file);
    });

    formData.append('thumbnail', getThumbnail());

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