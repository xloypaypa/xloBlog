/**
 * Created by Administrator on 2015/9/10.
 */
$(function(){
    $('.nickName').html(decodeURIComponent(window.username));
    //我的关注
    $('.myFocus').click(function(){
        getMarkedList();
    });

    //消息部分
    $('.myMessages').click(function(){
        getMessageList();
        //全部标记为已读
        $('.readAll').click(function(){
             readAll();
        });
    });

    //标记已读
    $('.message-list').on('click','li',function(){
        var _this=$(this);
        readMessage(_this);
    });
});


//获取消息列表
function getMessageList(){
    $('.nav-tabs li').removeClass('active');
    $('.myMessages').parent().addClass('active');
    $('.Focus').hide();
    $('.Messages ul').empty();
    $('.Messages').show();
    ajaxHeader('/getMessageList',null,function(data){
        var messageNum=0;
        var array=[];
        var newData=rankByTime(data);
        for(var i=0;i<newData.length;i++){
            var content=decodeURIComponent(newData[i].preview);
            var author=decodeURIComponent(newData[i].author);
            var date=transformDate(newData[i].time.time);
            $('.message-list').prepend($('#message-template').html());
            var messageNo=$('.message-list li').eq(0);
            messageNo.find('.message-tag').html(newData[i].type+':');
            messageNo.find('.message-content').html(content);
            messageNo.find('.message-author').html('by'+author);
            messageNo.find('.message-date').html(date);
            messageNo.attr('messageId',newData[i].id);
            array.push(JSON.stringify({id:newData[i].id}));
            if(newData[i].read){
                messageNo.css('opacity','0.6');
            }else{
                messageNum+=1;
            }
            console.log(messageNum);
        }
        localStorage.setItem('readAllArray',array);
        $('.messageNum').html(messageNum);
    });
}

//获取关注列表
function getMarkedList(){
    $('.nav-tabs li').removeClass('active');
    $('.myFocus').parent().addClass('active');
    $('.Focus ul').empty();
    $('.Focus').show();
    $('.Messages').hide();
    ajaxHeader('/getMarkedList',null,function(response){
        $('.focusNum').html(response.length);
        for(var i=0;i<response.length;i++){
            $('.focus-list').append("<li class='list-group-item'>1</li>");
            var focusNo=$('.focus-list li').eq(i);
            focusNo.html('用户：'+response[i].to);
        }
    });
}

//所有标记为已读
function readAll() {
    var data = localStorage.getItem('readAllArray');
    $.ajax({
        url: '/readAllMessage',
        type: 'POST',
        dataType: 'json',
        data: '[' + data + ']',
        beforeSend: function (XML) {
            XML.setRequestHeader('username', window.username);
            XML.setRequestHeader('password', window.password);
        },
        success: function (data) {
            $('.message-list li').css('opacity', '0.6');
            $('.messageNum').html('0');
        },
        error: function (response) {
            console.log(response);
        }
    });
}
//读取消息
function readMessage(_this){
    var data={
        id:$(_this).attr('messageId')
    };
    ajaxHeader('/readMessage',data,function(data){
        var messageNum=$('.messageNum');
        $(_this).css('opacity','0.6');
        //messageNum.html(messageNum.html()-1);
    });
}