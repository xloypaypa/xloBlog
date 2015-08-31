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
            alert('����Ϊ��');
            return false;
        }
        if(!/^[\w]*$/.test($('input[name=username]').val())){
            alert('�û�����ʽ����ȷ');
            return false;
        }
        if(!/^[\w]*$/.test($('input[name=password]').val())){
            alert('�����ʽ����ȷ');
            return false;
        }
        return true;
    }
});
