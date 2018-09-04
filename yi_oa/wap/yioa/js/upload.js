$(function () {
    window.bindUpFile = function () {
        $('#uploadFile').fileupload({
            dataType: 'json',
            disableImageResize: /Android(?!.*Chrome)|Opera/.test(window.navigator && navigator.userAgent),
            imageMaxWidth: 800,
            imageMaxHeight: 800,
            imageCrop: true, // Force cropped images
            done: function (e, data) {
                console.log("done!!!");
                var tDom = $('<li class="weui-uploader__file yioa-up-file" style="background-image:url(../up/' + data.result.path + '"></li>');
                tDom[0].style.backgroundImage = "url('http://192.144.133.55/b/up/" + data.result.path + "')";
                // $(".up-container").prepend('<li class="weui-uploader__file yioa-up-file" style="background-image:url(../up/' + data.result.path + '"></li>');
                $(".up-container").prepend(tDom);
                $(".ready-up-ele").css("display", "none");
                $(".ready-up").text('0%');

            },
            fail: function (e, data) {
                console.log(data);
                console.log(e);
            },
            progressall: function (e, data) {
                console.log("doing........");
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $(".ready-up-ele").css("display", "inline");
                $(".ready-up").text(progress + '%');
            }
        });
    };
    bindUpFile();


    $(".up-container").on("click", "li.weui-uploader__file", function () {
        var imgSrc = $(this).css("background-image");
        $(".del-photo .weui-gallery__img").css("background-image", imgSrc).data("tag", $(this));
        $(".del-photo").css("display", "block");

    });

    $(".del-photo").on("click", "i.weui-icon-delete", function () {
        var ele = $(".del-photo .weui-gallery__img")
        var tag = ele.data("tag");
        tag.remove();
        ele.css("background-image", "");
        $(".del-photo").css("display", "none");
    }).on("click", ".weui-gallery__img", function () {
        $(".del-photo").css("display", "none");
    });

    window.getUpFiles = function () {
        var str = $(".up-container li.yioa-up-file").map(function () {
            return $(this).css("background-image");
        }).join(",");
        return str;
    };
});

var appendAttachFile = function (attachFile) {
    if(!attachFile){
        return;
    }
    var arr = attachFile.split(",");
    var container = $(".up-container")
    $.each(arr, function (index, ele) {
        $('<li class="weui-uploader__file " ></li>').css("background-image", ele).appendTo(container);
    });
};
