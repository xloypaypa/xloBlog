$(function(){
    if(getQueryString('name')){
        var author=getQueryString('name');
        $('.myInfo .buttons').show();
    }else if(window.username){
        author=window.username;
        $('.myInfo .buttons').hide();
    }else {
        location.href='login.html';
    }
    $('.nickName').html(decodeURIComponent(author));

    //获取文章信息
    getDocument();

    //是否关注
    //isMarked();

    //评论
    $('.myComment .submit').click(function(){
        var comment=encodeURIComponent($('.myComment textarea').val());
        if(comment){
            var data={
                id:getQueryString('id'),
                reply:comment
            };
            ajaxHeader('/reply',data,function(response){
                if(response.return==200){
                    location.reload();
                }else{
                    alert('评论失败');
                }
            });
        }else{
            alert('不得为空');
        }
    });

    //关注
    $('.focus').click(function(){
        var data={
            aimUser:author
        };
        if($(this).html()=='加关注'){
            ajaxHeader('/mark',data,function(response){
                $(this).html('已关注');
            });
        }else{
            ajaxHeader('/unmark',data,function(response){
                $(this).html('加关注');
            });
        }
    });

    //私信
    $('.chat').click(function(){
        location.href='letters.html?receiver='+author;
    });
});
function getDocument(){
    var data={
        id:getQueryString('id')
    };
    ajaxRequest('/getDocument',data,function(response){
        var date=transformDate(response.time.$date);
        var commentLen=response.reply?response.reply.length:0;
        var title=decodeURIComponent(response.title);
        var body=decodeURIComponent(response.body);
        var author=decodeURIComponent(response.author);
        $('.article-content h2').html(title);
        $('.author').html(author);
        $('.article-body').html(body);
        $('.date').html(date);
        $('.commentNum').html(commentLen);
        for(var i=0;i<commentLen;i++){
            $('.comments-list').append("<li class='list-group-item'>"+$('#comment-list').html()+"</li>");
            var commentNo=$('.comments-list li').eq(i);
            var replyNo=response.reply[i];
            var commentDate=transformDate(replyNo.data.$date);
            var observer=decodeURIComponent(replyNo.author);
            var commentContent=decodeURIComponent(replyNo.reply);
            commentNo.find('.observer a').html(observer);
            commentNo.find('.comment-date').html(commentDate);
            commentNo.find('.comment-content p').html(commentContent);
        }
    });
}
function isMarked(){
    console.log(author);
    var data={
        aimUser:author
    };
    ajaxHeader('/isMarked',data,function(response){

    });
}