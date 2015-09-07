$(function(){
    var data={
        author:getQueryString('name')
    };
    ajaxRequest('/getDocumentListByAuthor',data,function(response){
        $('.article-list').empty();
        for(var i=0;i<response.length;i++){
            $('.article-list').append("<div class='article'>"+$('#article').html()+"</div>");
            var articleNo=$('.article').eq(i);
            var date=new Date(response[i].time.time);
            var year=date.getFullYear();
            var month=date.getMonth()+1;
            var day=date.getDate();
            articleNo.find('h2 a').attr('href','article.html?id='+response[i].id).html(response[i].title);
            articleNo.find('.readMore').attr('href','article.html?id='+response[i].id);
            articleNo.find('.author').html(response[i].author);
            articleNo.find('.article-body').html(response[i].preview);
            articleNo.find('.date').html(year+'-'+month+'-'+day);
        }
    });
});