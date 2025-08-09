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
const editor = quill.root;
const editorContainer = document.querySelector('#editor');

const titleInput = document.getElementById('title');

titleInput.addEventListener('input', () => {
    if(titleInput.value.trim()) titleInput.classList.remove('empty');
    else titleInput.classList.add('empty');
});

function addImageStyle(quill) {
    const images = quill.root.querySelectorAll('img');

    images.forEach(img => {
        const style = img.style;
        let align = null;

        if(style.float === 'left') align = 'left';
        else if(style.float === 'right') align = 'right';
        else if(style.display === 'block' && style.margin === 'auto') align = 'center';

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

        const previewChildren = preview.children.length;
        if(previewChildren >= 6) {
            console.log('저장할 수 있는 이미지 용량을 초과했습니다.');
            return;
        }
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

quill.on("text-change", (delta, oldDelta, source) => {
    const text = quill.getText().trim();
    const hasImage = quill.getContents().ops.some(op => op.insert && op.insert.image);
    if (text || hasImage) editor.classList.remove('empty');
    else editor.classList.add('empty');

//    if (text) {
//        editor.classList.remove('empty');
//    } else {
//        if(!hasImage) editor.classList.add('empty');
//    }

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
