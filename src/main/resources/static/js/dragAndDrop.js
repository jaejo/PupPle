var input = document.getElementById("input");
var initLabel = document.getElementById("label");
let formData = new FormData();
let arr = new Array();

input.addEventListener("change", (event) => {
    const files = changeEvent(event);
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

function changeEvent(event){
    const { target } = event;
    return [...target.files];
};

function handleUpdate(fileList){
    const preview = document.getElementById("preview");
    if (preview.children.length + fileList.length <= 6) {
        fileList.forEach((file) => {
            const reader = new FileReader();
            reader.addEventListener("load", (event) => {
                const img = el("img", {
                    className: "embed-img",
                    src: event.target?.result,
                });
    //        const imgContainer = el("div", { className: "container-img" }, img);
            arr.push(file);
            //파일 이름으로 아이디 값 추가
            img.id = file.name;
            preview.append(img);
//            formData.append("file", file);
        });
        reader.readAsDataURL(file);
      });
    } else {
        alert("이미지 6개 이하로만 가능");
    }
};

function el(nodeName, attributes, ...children) {
  const node =
    nodeName === "fragment"
      ? document.createDocumentFragment()
      : document.createElement(nodeName);

  Object.entries(attributes).forEach(([key, value]) => {
    if (key === "events") {
      Object.entries(value).forEach(([type, listener]) => {
        node.addEventListener(type, listener);
      });
    } else if (key in node) {
      try {
        node[key] = value;
      } catch (err) {
        node.setAttribute(key, value);
      }
    } else {
      node.setAttribute(key, value);
    }
  });

  children.forEach((childNode) => {
    if (typeof childNode === "string") {
      node.appendChild(document.createTextNode(childNode));
    } else {
      node.appendChild(childNode);
    }
  });
  return node;
}

const updateImg = document.getElementById('updateImg');
updateImg.addEventListener('click', moveImg);
//const preview = document.getElementById('preview');
//preview.addEventListener('click', moveImg);

function moveImg() {
    /* createBoardForm */
    const draggables = document.querySelectorAll(".embed-img");
    const containers = document.querySelectorAll(".preview");

    draggables.forEach(draggable => {
        const parent = draggable.closest('.container-img');

        draggable.addEventListener("dragstart", () => {
            draggable.classList.add("dragging");
        });

        draggable.addEventListener("dragend", () => {
            draggable.classList.remove("dragging");
//            parent.remove();
        });
    });
    //더블 클릭 삭제 구현
    draggables.forEach(draggable => {
        draggable.addEventListener("dblclick", () => {
            if(confirm('해당 이미지를 삭제하시겠습니까?')) {
                draggable.remove();
                alert("삭제됐습니다.");
            }
        });
    });

    containers.forEach(container => {
      container.addEventListener("dragover", e => {
        e.preventDefault();
        const afterElement = getDragAfterElement(container, e.clientX);
        const draggable = document.querySelector(".dragging");
        if (afterElement === undefined) {
          container.appendChild(draggable);
        } else {
          container.insertBefore(draggable, afterElement);
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