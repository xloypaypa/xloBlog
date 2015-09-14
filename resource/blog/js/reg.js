$(function(){
    checkNull($('#reg'));
    $('.submit').click(function(){
        if(check()){
            var username=encodeURIComponent($('input[name=username]').val());
            var password=encodeURIComponent($('input[name=password]').val());
            var data={
                "username":username,
                "password":password
            };
            ajaxRequest('/register',data,function(response){
                location.href='login.html';
            });
        }
    });

    function check(){
        if(!$('input[name=username]').val()||!$('input[name=password]').val()||!$('input[name=confirmPass]').val()){
            alert('不得为空');
            return false;
        }
        if(!/^[\w\u4e00-\u9fa5]*$/.test($('input[name=username]').val())){
            alert('用户名格式不正确');
            return false;
        }
        if(!/^[\w]*$/.test($('input[name=password]').val())){
            alert('密码格式不正确');
            return false;
        }
        if(!/^[\w]*$/.test($('input[name=confirmPass]').val())){
            return false;
        }
        if($('input[name=confirmPass]').val()!=$('input[name=password]').val()){
            alert('密码不一致');
            return false;
        }
        return true;
    }
});