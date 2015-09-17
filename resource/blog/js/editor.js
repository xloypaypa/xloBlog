$(function(){
    $('.nickName').html(decodeURIComponent(window.username));
    $('.submit').click(function(){
        console.log(encodeURIComponent($('.blog-title input').val()));
        var value=$('.blog-content textarea').val();
        var preview=encodeURIComponent(value.substr(0,100));
        var data={
            title:encodeURIComponent($('.blog-title input').val()),
            body:encodeURIComponent(value),
            preview:preview.length
        };
        ajaxHeader('/addDocument',data,function(data){
                location.href='index.html';
        });
    });
});