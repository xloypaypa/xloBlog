$(function(){
    checkNull($('#login'));
    $('.submit').click(function(){
        if(check()){
            window.localStorage.setItem('username',encodeURIComponent($('input[name=username]').val()));
            window.localStorage.setItem('password',encodeURIComponent($('input[name=password]').val()));
            $.ajax({
                url:'/login',
                type:'POST',
                dataType:'json',
                beforeSend:function(XML){
                    XML.setRequestHeader('username',encodeURIComponent($('input[name=username]').val()));
                    XML.setRequestHeader('password',encodeURIComponent($('input[name=password]').val()));
                },
                success:function(response){
                    if(response.return==200){
                        location.href='index.html';
                    }else{
                        alert('用户账号或密码有错误');
                    }
                },
                error:function(response){
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
        if(!/^[\w\u4e00-\u9fa5]*$/.test($('input[name=username]').val())){
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
