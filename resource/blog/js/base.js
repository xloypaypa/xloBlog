 window.username=window.sessionStorage.getItem('username');
 window.password=window.sessionStorage.getItem('password');
function checkNull(_this){
    $(_this).find('.input-group').append('<em>不得为空</em>');
    $(_this).find('.input-group input').css('position','relative')
    $(_this).find('em').css({
        'position':'absolute',
        'top':'5px',
        'right':'5px',
        'color':'red',
        'font-style':'normal',
        'z-index':99,
        'display':'none'
    });
    $(_this).find('.input-group input').blur(function(){
        if($(this).val()){
            $(this).next().hide();
        }else{
            $(this).next().show();
        }
    });
};

//头部需要加username的ajax
function ajaxHeader(url,data,callback){
    $.ajax({
        url:url,
        type:'POST',
        dataType:'json',
        data:JSON.stringify(data),
        beforeSend:function(XML){
            XML.setRequestHeader('username',window.username);
            XML.setRequestHeader('password',window.password);
        },
        success:function(response){
            console.log(arguments);
            callback(response);
        }
    });
}

//不需要加头部的ajax
function ajaxRequest(url,data,callback){
    $.ajax({
        url:url,
        type:'POST',
        dataType:'json',
        data:JSON.stringify(data),
        success:function(response){
            console.log(response);
            callback(response);
        }
    });
}

//获取url中的数据
function getQueryString(name){
    var str=document.location.search.substr(1).match(reg);
    var reg=new RegExp("(^|&)"+name+"=([^&]*)($|&)","i");
    if(!str) return unescape(str[2]);
    return null;
}