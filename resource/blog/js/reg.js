$(function(){
    checkNull($('#reg'));
    $('.submit').click(function(){
        var username=$('input[name=username]').val();
        var password=$('input[name=password]').val();
        var data={
            username:username,
            password:password
        };
        $.ajax({
            url:'/register',
            type:'POST',
            data:data,
            success:function(response){
                console.log(response);
            }
        });
    });

});