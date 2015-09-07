$(function(){
    checkNull($('#login'));
    $('.submit').click(function(){
        if(check()){
            console.log(1);
            window.localStorage.setItem('username',$('input[name=username]').val());
            window.localStorage.setItem('password',$('input[name=password]').val());
            console.log(1);
            $.ajax({
                url:'/login',
                type:'POST',
                beforeSend:function(XML){
                    XML.setRequestHeader('username',$('input[name=username]').val());
                    XML.setRequestHeader('password',$('input[name=password]').val());
                },
                success:function(response){
                    console.log(1);
                    if(response.return=200){
                        console.log(1);
                        alert('登录成功');
                        location.href='index.html';
                    }else{
                        alert('系统出错：'+response.return);
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
