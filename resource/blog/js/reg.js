$(function(){
    checkNull($('#reg'));
    $('.submit').click(function(){
        console.log($('input[name=username]').val());
        console.log($('input[name=password]').val());
        var username=$('input[name=username]').val();
        var password=$('input[name=password]').val();
        var data={
            username:username,
            password:password
        };
        $.ajax({
            url:'/register',
            type:'POST',
            dataType:'json',
            data:data,
            success:function(response){
                console.log(response);
            }
        });
    });

});