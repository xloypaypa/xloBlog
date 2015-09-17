/**
 * Created by Administrator on 2015/9/10.
 */
$(function(){
    var receiver=getQueryString('receiver');
    $('.receiver').html(decodeURIComponent(receiver));

    //发送消息
    $('.submit').click(function(){
        var value=$('.chatContent textarea').val();
        var preview=encodeURIComponent(value.substr(0,50));
        var message=encodeURIComponent(value);
        var data={
            aim:receiver,
            message:message,
            preview:preview.length
        };
        ajaxHeader('/sendMessage',data,function(response){
            alert('发送成功');
        });
    });
});
