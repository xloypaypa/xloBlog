/**
 * Created by Administrator on 2015/9/10.
 */
$(function(){
    var receiver=getQueryString('receiver');
    $('.receiver').html(decodeURIComponent(receiver));

    //·¢ËÍÏûÏ¢
    $('.submit').click(function(){
        var message=encodeURIComponent($('.chatContent textarea').val());
        console.log(receiver);
        console.log(message);
        var data={
            aim:receiver,
            message:message
        };
        ajaxHeader('/sendMessage',data,function(response){

        });
    });
});
