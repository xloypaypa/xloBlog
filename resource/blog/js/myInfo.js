/**
 * Created by Administrator on 2015/9/10.
 */
$(function(){
    $('.nickName').html(decodeURIComponent(window.username));

    //标记已读
    $('.message-list').on('click','li',function(){
        $(this).find('input').attr('checked','true').css('opacity','0.8');
        var data={
            id:$(this).attr('messageId')
        };
        ajaxHeader('/readMessage',data,function(response){

        });
    });

    //我的关注
    $('.myFocus').click(function(){
        getMarkedList();
    });

    $('.myMessages').click(function(){
        getMessageList();
        //全部标记为已读
        readAll();
    });
});


//获取消息列表
function getMessageList(){
    $('.Focus').hide();
    $('.Messages ul').empty();
    $('.Messages').show();
    ajaxHeader('/getMessageList',null,function(response){

        var messageNum=0;
        for(var i=0;i<response.length;i++){
            var content=decodeURIComponent(response[i].message);
            var author=decodeURIComponent(response[i].author);
            var date=transformDate(response[i].time.time);
            $('.message-list').append($('#message-template').html());
            var messageNo=$('.message-list li').eq(i);
            messageNo.find('.message-content').html(content);
            messageNo.find('.message-author').html('by'+author);
            messageNo.find('.message-date').html(date);
            messageNo.attr('messageId',response[i].id);
            if(response[i].read){
                messageNo.find('input').attr('checked','false');
                messageNo.css('opacity','0.5');
            }else{
                messageNum+=1;
            }
        }
        $('.messageNum').html(messageNum);
    });
}

//获取关注列表
function getMarkedList(){
    $('.Focus ul').empty();
    $('.Focus').show();
    $('.Messages').hide();
    ajaxHeader('/getMarkedList',null,function(response){
        $('.focusNum').html(response.length);
        for(var i=0;i<response.length;i++){
            $('.focus-list').append("<li class='list-group-item'>1</li>");
            var focusNo=$('.focus-list li').eq(i);
            focusNo.html('用户：'+response[i].user);
        }
    });
}

function readAll(){
    console.log($('.message-list li'));
    $('.readAll').click(function(){
        $('.message-list input').attr('checked','true');

    });
}