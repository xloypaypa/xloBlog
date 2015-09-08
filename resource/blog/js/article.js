$(function(){
    var data={
        id:getQueryString('id')
    };
    ajaxRequest('/getDocument',data,function(response){
        var date=transformDate(response.time.$date);
        var commentLen=response.reply?response.reply.length:0;
        var title=decodeURIComponent(response.title);
        var body=decodeURIComponent(response.body);
        var author=decodeURIComponent(response.author);
        $('.article-content h2 a').html(title);
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
});