$(function(){
    $('.submit').click(function(){
        var data={
            title:encodeURIComponent($('.blog-title input').val()),
            body:encodeURIComponent($('.blog-content textarea').val())
        };
        ajaxHeader('/addDocument',data,function(response){
            if(response.return==200){
                location.href='index.html';
            }else{
                alert('系统出错：'+response.return);
            }
        });
    });
});