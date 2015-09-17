$(function(){
    $('.nickName').html(decodeURIComponent(window.username));
    $('.submit').click(function(){
        console.log(encodeURIComponent($('.blog-title input').val()));
        var data={
            title:encodeURIComponent($('.blog-title input').val()),
            body:encodeURIComponent($('.blog-content textarea').val())
        };
        ajaxHeader('/addDocument',data,function(data){
                location.href='index.html';
        });
    });
});