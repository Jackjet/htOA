//star
$(document).ready(function(){
    var stepW = 24;
    var description = new Array("非常不满意","不满意","一般","满意","非常满意");
    var stars = $("#star > li");
    var descriptionTemp;
    $("#showb").css("width",0);
    stars.each(function(i){
        $(stars[i]).click(function(e){
            var n = i+1;
            $("#showb").css({"width":stepW*n});
            descriptionTemp = description[i];
            $(this).find('a').blur();
            return stopDefault(e);
            return descriptionTemp;
        });
    });
    stars.each(function(i){
        $(stars[i]).hover(
            function(){
                $(".description").text(description[i]);
            },
            function(){
                if(descriptionTemp != null){
                    $(".description").text("当前您的评价为："+descriptionTemp);
                    var nameScore = 0;
                    if(descriptionTemp == "非常不满意"){
                    	nameScore = 1;
                    }
                    if(descriptionTemp == "不满意"){
                    	nameScore = 2;
                    }
                    if(descriptionTemp == "一般"){
                    	nameScore = 3;
                    }
                    if(descriptionTemp == "满意"){
                    	nameScore = 4;
                    }
                    if(descriptionTemp == "非常满意"){
                    	nameScore = 5;
                    }
                    $("#nameScore").val(nameScore);
                    $("#nameScore").focus("");
                    $("#nameScore").blur("");
                }else{ 
                    $(".description").text(" ");
                    $("#nameScore").val("");
                    $("#nameScore").focus("");
                    $("#nameScore").blur("");
                }
            }
        );
    });
});
function stopDefault(e){
    if(e && e.preventDefault)
           e.preventDefault();
    else
           window.event.returnValue = false;
    return false;
};
