$(function(){
    $('.submit').click(function(){
        console.log($('.blog-content textarea').val());
        var data={
            title:$('.blog-title input').val(),
            body:$('.blog-content textarea').val()
        };
        ajaxHeader('/addDocument',data,function(response){

        });
    });
});