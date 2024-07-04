document.addEventListener("DOMContentLoaded", function () {
    const element1 = document.getElementById("my-text1");
    const element2 = document.getElementById("my-text2");
    const element3 = document.getElementById("my-text3");
    const element4 = document.getElementById("my-text4");
    const element5 = document.getElementById("my-text5");
    const element6 = document.getElementById("my-text6");
    const element7 = document.getElementById("my-text7");
    const element8 = document.getElementById("my-text8");

    const elements = [element1, element2, element3, element4, element5, element6, element7, element8];

    elements.forEach(element => {
    const annotation = RoughNotation.annotate(element, {
        type: 'underline',
        color: 'blue',
        padding: -3,
    });

    element.addEventListener("mouseenter", function () {
        annotation.show();
    });

    element.addEventListener("mouseleave", function () {
        annotation.hide();
    });
});
})