$(function(){
    checkNull($('#login'));
    $('.submit').click(function(){
        if(check()){
            window.localStorage.setItem('username',$('input[name=username]').val());
            window.localStorage.setItem('password',$('input[name=password]').val());
            $.ajax({
                url:'/login',
                type:'POST',
                beforeSend:function(XML){
                    XML.setRequestHeader('username',$('input[name=username]').val());
                    XML.setRequestHeader('password',$('input[name=password]').val());
                },
                success:function(response){
                    console.log(response);
                }
            });
        }
    });

    function check(){
        if(!$('input[name=username]').val()||!$('input[name=password]').val()){
            alert('不得为空');
            return false;
        }
        if(!/^[\w]*$/.test($('input[name=username]').val())){
            alert('用户名格式不正确');
            return false;
        }
        if(!/^[\w]*$/.test($('input[name=password]').val())){
            alert('密码格式不正确');
            return false;
        }
        return true;
    }
});
