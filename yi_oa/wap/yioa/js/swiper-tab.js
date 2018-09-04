$(document).on("click", '.swiper-title .swiper-slide', function () {
    $(this).parents('.swiper-title').data('swiper').slideTo($(this).index(), 500, false);
    slideOk($(this).parents('.swiper-title').parent(), $(this).index());
});

function slideOk($parent, index){
    var $swiperTitle = $parent.find('.swiper-title');
    var $swiperContent = $parent.find('.swiper-content');
    $swiperTitle.find(".swiper-slide").removeClass("selected").eq(index).addClass("selected");
    var onshow = $swiperContent.find('.swiper-slide').eq(index).attr('onshow');
    window.scrollTo(0,0);
    updateUrl({'swiper_tab': index});
    if(onshow) {
        window[onshow] && window[onshow](index, $swiperContent.find('.swiper-slide').eq(index));
    }else{
        window.queryFunc && queryFunc(false, false, $swiperContent.find('.swiper-slide').eq(index));
    }
}

$(function(){
    $('.swiper-content').each(function(idx, ele) {
        var swiper = new Swiper(ele, {
            direction: 'horizontal',
            width: getDocument().body.clientWidth,
            autoHeight: true,
            loop: false,
            onSlideChangeEnd: function (swiper) {
                slideOk($(swiper.container).parent(), swiper.activeIndex);
            }
        });
        var $parent = $(ele).parent();
        $parent.find('.swiper-title').data('swiper', swiper);
        slideOk($parent, getUrlParameter('swiper_tab') || 0);
        swiper.slideTo(getUrlParameter('swiper_tab') || 0, 500, false);
    });
});