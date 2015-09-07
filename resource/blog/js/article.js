$(function(){
    var data={
        id:getQueryString('id')
    };
    ajaxRequest('/getDocument',data,function(response){
        var date=transformDate(response.time.$date);
        $('.article-content h2 a').html(response.title);
        $('.author').html(response.author);
        $('.article-body').html(response.body);
        $('.date').html(date);
        $('.commentNum').html(response.reply.length);
        for(var i=0;i<response.reply.length;i++){
            $('.comments-list').append("<li class='list-group-item'>"+$('#comment-list').html()+"</li>");
            var commentDate=transformDate(response.reply[i].data.$date);
            var commentNo=$('.comments-list li').eq(i);
            commentNo.find('.observer a').html(response.reply[i].author);
            commentNo.find('.comment-date').html(commentDate);
            commentNo.find('.comment-content p').html(response.reply[i].reply);
        }
    });
    $('.myComment .submit').click(function(){
        var comment=$('.myComment textarea').val();
        console.log(comment);
        var data={
            id:getQueryString('id'),
            reply:comment
        }
        ajaxHeader('/reply',data,function(response){
            console.log(response);
        });
    });
});