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