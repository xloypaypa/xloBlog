$(function(){
    if(getQueryString('name')){
        var author=getQueryString('name');
    }else if(window.username){
        author=window.username;
        $('.btn-others').hide();
    }else {
        location.href='login.html';
    }
    $('.nickName').html(decodeURIComponent(author));
    var data={
        author:author
    };
    ajaxRequest('/getDocumentListByAuthor',data,function(response){
        $('.article-list').empty();
        for(var i=0;i<response.length;i++){
            $('.article-list').append("<div class='article'>"+$('#article').html()+"</div>");
            var articleNo=$('.article').eq(i);
            var date=transformDate(response[i].time.time);
            var title=decodeURIComponent(response[i].title);
            var preview=decodeURIComponent(response[i].preview);
            var author=decodeURIComponent(response[i].author);
            if(getQueryString('name')){
                articleNo.find('h2 a').html(title).attr('href','article.html?id='+response[i].id+'&name='+author);
                articleNo.find('.readMore').attr('href','article.html?id='+response[i].id+'&name='+author);
            }else{
                articleNo.find('h2 a').html(title).attr('href','article.html?id='+response[i].id);
                articleNo.find('.readMore').attr('href','article.html?id='+response[i].id);
            }
            articleNo.find('.author').html(author);
            articleNo.find('.article-body').html(preview);
            articleNo.find('.date').html(date);
        }
    });

});
