const maxDeg = 15;

elements = document.getElementsByClassName("card-img");
for (const element of elements) {
    element.addEventListener("mousemove", (event) => {
        let y = event.offsetY - element.offsetHeight/2;
        let x = event.offsetX - element.offsetWidth/2;
        
        let degX = -(y * maxDeg) / (element.offsetHeight/2);
        let degY = (x * maxDeg) / (element.offsetWidth/2);
        element.style.transform = "perspective(100em) rotateX(" + degX + "deg) rotateY(" + degY + "deg)";
    });
}

function onHover() {

}