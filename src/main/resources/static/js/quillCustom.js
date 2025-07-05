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

document.getElementById('save-delta').addEventListener('click', function() {
    fetch('/newBoardV2', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            html: quill.root.innerHTML,
            delta: quill.getContents(),
            boardType: $("#boardType").val()
        }),
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