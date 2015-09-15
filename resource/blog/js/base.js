 window.username=window.localStorage.getItem('username');
 window.password=window.localStorage.getItem('password');
function checkNull(_this){
    $(_this).find('.input-group').append('<em>不得为空</em>');
    $(_this).find('.input-group input').css('position','relative');
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
}

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
                if(response.return==200){
                    callback(response.data);
                }else if(response.return==404){
                    alert('找不到页面');
                }else if(response.return=403){
                    alert('操作有误');
                }
            },
            error:function(response){
                console.log(response);
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
            if(response.return==200){
                callback(response.data);
            }else if(response.return==404){
                alert('找不到页面');
            }else if(response.return=403){
                alert('操作有误');
            }
        },
        error:function(response){
            console.log(response);
        }
    });
}

//获取url中的数据
function getQueryString(name){
    var reg=new RegExp("(^|&)"+name+"=([^&]*)($|&)","i");
    var str=document.location.search.substr(1).match(reg);
    if(str) return str[2];
    return null;
}
 //转换时间
 function transformDate(time){
     var date=new Date(time);
     var year=date.getFullYear();
     var month=date.getMonth()+1;
     var day=date.getDate();
     return year+'-'+month+'-'+day;
 }

 /*导航条搜索*/
 $(function () {
     $('.search button').click(function(){
         var searchName=encodeURIComponent($('.search input').val());
         var data={
             username:searchName
         };
         ajaxRequest('/userExist',data,function(data){
             if(data.return){
                 location.href='index.html?name='+searchName;
             }else{
                 alert('该用户不存在');
             }

         });
     });
     $('.logout').click(function(){
         localStorage.clear();
         location.href='login.html';
     });
     if(window.username){
         $('.login').hide();
         $('.reg').hide();
         $('.menu').show();
     }else{
         $('.login').show();
         $('.reg').show();
         $('.menu').hide();
     }
 });
