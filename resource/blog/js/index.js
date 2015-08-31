$(function(){
    var data={
        author:window.username
    };
    ajaxRequest('/getDocumentListByAuthor',data,function(response){
        //for(var i=0;i<response.length;i++){
            console.log(response[0].id);
            var data={
                id:response[0].id
            };
            ajaxRequest('/getDocument',data,function(response){
                $('.article-content h2 a').html(response.title);
                $('.author').html(response.author);
                $('.content').html(response.body);
                $('.date').html(response.time);
            });
        //}
    });
});
