function ViewPortWidth() {
    var width = 0;

    if ((document.documentElement) && (document.documentElement.clientWidth)) {
        width = document.documentElement.clientWidth;
    } else if ((document.body) && (document.body.clientWidth)) {
        width = document.body.clientWidth;
    } else if (window.innerWidth) {
        width = window.innerWidth;
    }
    return width;
}

// Returns height of viewable area in the browser
function ViewPortHeight() {
    var height = 0;
    if (window.innerHeight) {
        height = window.innerHeight;}
    else if ((document.documentElement) && (document.documentElement.clientHeight)) {
        height = document.documentElement.clientHeight;
    }
    return height;
}

$(function() {

    var browser = navigator.appName;
    var heightAdjust = 23;
    var widthAdjust = 7+20;

    if (browser != "Microsoft Internet Explorer") {
        heightAdjust = 18;
        widthAdjust = 9+20;
    }


    $(window).resize(function(){
        $("#grant_iframe").width((ViewPortWidth() - widthAdjust));
        $('#grant_iframe').height((ViewPortHeight() - $('#tabs_ul').height() - heightAdjust));
    });
    $('#grant_iframe').height((ViewPortHeight() - $('#tabs_ul').height() - heightAdjust));

});