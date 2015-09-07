$(function(){
    console.log(getQueryString('id'));
    var data={
        id:getQueryString('id')
    };
    ajaxRequest('/getDocument',data,function(response){
        var date=new Date(response.time.$date);
        var year=date.getFullYear();
        var month=date.getMonth()+1;
        var day=date.getDate();
        $('.article-content h2 a').html(response.title);
        $('.author').html(response.author);
        $('.article-body').html(response.body);
        $('.date').html(year+'-'+month+'-'+day);
    });
});