let formData = new FormData();
let arr = new Array();

document.addEventListener("DOMContentLoaded", function() {
    initMouseEvents();
    initDragAndDrop();
});

function initMouseEvents() {
    const input = document.getElementById("input");
    const initLabel = document.getElementById("label");


    input.addEventListener("change", (event) => {
        const files = [...event.target.files];
        handleUpdate(files);
    });

    initLabel.addEventListener("mouseover", (event) => {
        event.preventDefault();
        const label = document.getElementById("label");
        label?.classList.add("label--hover");
    });

    initLabel.addEventListener("mouseout", (event) => {
        event.preventDefault();
        const label = document.getElementById("label");
        label?.classList.remove("label--hover");
    });

    document.addEventListener("dragenter", (event) => {
        event.preventDefault();
        if (event.target.className === "inner") {
            event.target.style.background = "#616161";
        }
    });

    document.addEventListener("dragover", (event) => {
        event.preventDefault();
    });

    document.addEventListener("dragleave", (event) => {
        event.preventDefault();
        if (event.target.className === "inner") {
            event.target.style.background = "#3a3a3a";
        }
    });

    document.addEventListener("drop", (event) => {
        event.preventDefault();
        if (event.target.className === "inner") {
            const files = event.dataTransfer?.files;
            event.target.style.background = "#3a3a3a";
            handleUpdate([...files]);
        }
    });

//    function changeEvent(event){
//        const { target } = event;
//        return [...target.files];
//    };

    function handleUpdate(fileList){
        const preview = document.getElementById("preview");
        if (preview.children.length + fileList.length > 6) {
            alert("이미지는 6장까지 가능합니다.");
            return;
        }
        fileList.forEach((file) => {
            const reader = new FileReader();
            reader.addEventListener("load", (event) => {
                const img = createImageElement(event.target.result, file.name);
                arr.push(file);
                preview.append(img);
                bindImageEvents(img);
            });
            reader.readAsDataURL(file);
        });
    }
    //이미지 생성
    function createImageElement(src, id) {
        const img = document.createElement("img");
        img.className = "embed-img";
        img.src = src;
        img.id = id;
        return img;
    }

    function bindImageEvents(draggable) {
        if(draggable.dataset.bound === "true") return;

        draggable.addEventListener("dragstart", () => {
            draggable.classList.add("dragging");
        });

        draggable.addEventListener("dragend", () => {
            draggable.classList.remove("dragging");
        });

        draggable.addEventListener("dblclick", () => {
            if(confirm('해당 이미지를 삭제하시겠습니까?')) {
                draggable.remove();
                alert("삭제됐습니다.");
            }
        });

        draggable.dataset.bound = "true";
    }

//    function el(nodeName, attributes, ...children) {
//      const node =
//        nodeName === "fragment"
//          ? document.createDocumentFragment()
//          : document.createElement(nodeName);
//
//      Object.entries(attributes).forEach(([key, value]) => {
//        if (key === "events") {
//          Object.entries(value).forEach(([type, listener]) => {
//            node.addEventListener(type, listener);
//          });
//        } else if (key in node) {
//          try {
//            node[key] = value;
//          } catch (err) {
//            node.setAttribute(key, value);
//          }
//        } else {
//          node.setAttribute(key, value);
//        }
//      });
//
//      children.forEach((childNode) => {
//        if (typeof childNode === "string") {
//          node.appendChild(document.createTextNode(childNode));
//        } else {
//          node.appendChild(childNode);
//        }
//      });
//      return node;
//    }
}

//const updateImg = document.getElementById('updateImg');
//updateImg.addEventListener('click', moveImg);
//const preview = document.getElementById('preview');
//preview.addEventListener('click', moveImg);

function initDragAndDrop() {
    const draggables = document.querySelectorAll(".embed-img");

    draggables.forEach(draggable => {
//        draggable.addEventListener("dragstart", () => {
//            if(e.target.classList.contains("embed-img")) draggable.classList.add("dragging");
//        });
//
//        draggable.addEventListener("dragend", () => {
//            if(e.target.classList.contains("embed-img")) draggable.classList.remove("dragging");
//        });
//
//        draggable.addEventListener("dblclick", ()=> {
//            if(confirm('해당 이미지를 삭제하시겠습니까?')) {
//                draggable.remove();
//                alert("삭제됐습니다.");
//            }
//        });
        draggable.addEventListener("dragstart", (e) => {
            if(e.target.classList.contains("embed-img")) {
                e.target.classList.add("dragging");
            }
        });

        draggable.addEventListener("dragend", (e) => {
            if(e.target.classList.contains("embed-img")) {
                e.target.classList.remove("dragging");
            }
        });

        draggable.addEventListener("dblclick", (e) => {
            if(e.target.classList.contains("embed-img")) {
                if(confirm('해당 이미지를 삭제하시겠습니까?')) {
                    e.target.remove();
                    alert("삭제됐습니다.");
                }
            }
        });
    });

    const containers = document.querySelectorAll(".preview");

    containers.forEach(container => {
        container.addEventListener("dragover", e => {
            e.preventDefault();

            const draggable = document.querySelector(".dragging");

            const isInternalMove = container.contains(draggable);

            const imgCount = container.querySelectorAll(".embed-img").length;

            if(!isInternalMove && imgCount >= 6) {
                alert('더이상 이미지를 추가할 수 없습니다.');
                return;
            }

            const afterElement = getDragAfterElement(container, e.clientX);
            if (afterElement) {
                container.insertBefore(draggable, afterElement);
            } else {
                container.appendChild(draggable);
            }
        });
    });
}

function getDragAfterElement(container, x) {
    const draggableElements = [
    ...container.querySelectorAll(".embed-img:not(.dragging)"),
    ];
    return draggableElements.reduce(
        (closest, child) => {
            const box = child.getBoundingClientRect();

            const offset = x - box.left - box.width / 2;
//            const offsetY = y - box.top - box.height / 2;
//            console.log(offset);
            if (offset < 0 && offset > closest.offset) {
                return { offset: offset, element: child };
            } else {
                return closest;
            }
        },
        { offset: Number.NEGATIVE_INFINITY },
    ).element;
}