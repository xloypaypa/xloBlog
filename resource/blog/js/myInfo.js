/**
 * Created by Administrator on 2015/9/10.
 */
$(function(){
    ajaxHeader('/getMarkedList',null,function(response){
        $('.focusNum').html(response.length);
        for(var i=0;i<response.length;i++){
            $('.focus-list').append($('#focus-list').html());
            var focusNo=$('.focus-list li').eq(i);
            focusNo.html('用户：'+response[i].user);
        }

    });
    $('.nickName').html(decodeURIComponent(window.username));
});