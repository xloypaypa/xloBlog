$(function(){
    checkNull($('#login'));
});
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
        'display':'display'
    });
    $(_this).find('input[type=text]').blur(function(){
        if($(this).val()){

        }else{

        }
    });
};