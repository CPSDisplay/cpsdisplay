const maxDeg = 5;

elements = document.getElementsByClassName("card-img");
for (const element of elements) {
    element.addEventListener("mousemove", (event) => {
        let y = event.offsetY - element.offsetHeight/2;
        let x = event.offsetX - element.offsetWidth/2;
        
        let degX = -(y * maxDeg) / (element.offsetHeight/2);
        let degY = (x * maxDeg) / (element.offsetWidth/2);
        element.style.transform = "perspective(100em) rotateX(" + degX + "deg) rotateY(" + degY + "deg)";
    });
    element.addEventListener("mouseout", (event) => {
        var an = element.animate({ transform: "perspective(100em) rotateX(0deg) rotateY(0deg)" },
            200, "ease-in", function() {console.log("ended");}
        )
        an.onfinish = function(event) {
            element.style.transform = "perspective(100em) rotateX(0deg) rotateY(0deg)";
        }
    });
}