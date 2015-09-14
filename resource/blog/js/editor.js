$(function(){
    $('.submit').click(function(){
        var data={
            title:encodeURIComponent($('.blog-title input').val()),
            body:encodeURIComponent($('.blog-content textarea').val())
        };
        ajaxHeader('/addDocument',data,function(response){
                location.href='index.html';
        });
    });
});