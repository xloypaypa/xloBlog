$(function(){
    checkNull($('#reg'));
    $('.submit').click(function(){
        if(check()){
            var username=$('input[name=username]').val();
            var password=$('input[name=password]').val();
            var data={
                "username":username,
                "password":password
            };
            ajaxRequest('/register',data,function(response){

            });
        }else{
            alert('����ʽ����');
        }
    });

    function check(){
        if($('input[name=username]').val().test('/^[\w]*$/')){
            return false;
        }
    }
});