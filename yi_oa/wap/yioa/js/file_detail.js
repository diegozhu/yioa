window.showAttachFile = function (attachFile) {
    if(!attachFile){
        return;
    }
    var arr = attachFile.split(",");
    var container = $(".show-up-container")
    $.each(arr, function (index, ele) {
        $('<li class="weui-uploader__file " ></li>').css("background-image", ele).appendTo(container);
    });
};

window.showFileFun = function () {
    $(".show-up-container").on("click", "li.weui-uploader__file", function () {
        var imgSrc = $(this).css("background-image");
        $(".show-photo .weui-gallery__img").css("background-image", imgSrc).data("tag", $(this));
        $(".show-photo").css("display", "block");
    });

    $(".show-up-container li.weui-uploader__file").on("click", function () {
        var imgSrc = $(this).css("background-image");
        // alert(imgSrc);
        // var fullPath = imgSrc.replace('url("',"").replace('")',"");
        // alert(fullPath);
        // alert(fullPath.substr(fullPath.indexOf("("),fullPath.length-1));
        // window.open(fullPath);
        $(".show-photo .weui-gallery__img").css("background-image", imgSrc).data("tag", $(this));
        $(".show-photo").css("display", "block");
    });

    $(".show-photo").on("click", ".weui-gallery__img", function () {
        $(".show-photo").css("display", "none");
    });
}
$(function () {
    showFileFun();
});

