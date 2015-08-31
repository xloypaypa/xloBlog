$(function(){
    var data={
        id:getQueryString('id')
    };
    ajaxRequest('/getDocument',data,function(response){
        $('.article-content h2 a').html(response.title);
        $('.author').html(response.author);
        $('.content').html(response.body);
        $('.date').html(response.time);
    });
});