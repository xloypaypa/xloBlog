$(function(){
    var data={
        author:getQueryString('name')
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
            articleNo.find('h2 a').attr('href','article.html?id='+response[i].id).html(title);
            articleNo.find('.readMore').attr('href','article.html?id='+response[i].id);
            articleNo.find('.author').html(author);
            articleNo.find('.article-body').html(preview);
            articleNo.find('.date').html(date);
        }
    });
});