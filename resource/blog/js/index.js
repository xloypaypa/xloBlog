$(function(){
    console.log(window.username);
    var data={
        author:window.username
    };
    ajaxRequest('/getDocumentListByAuthor',data,function(response){
        console.log(response.length);
        console.log(response[0]);
        for(var i=0;i<response.length;i++){
            $('.main').prepend("<div class='article'>"+$('#article').html()+"</div>");
            console.log(response[i].id);
            $('.article').eq(i).find('h2 a').attr(href,'article.html?id='+response[i].id);
        }
    });
});
