$(function(){
    if(getQueryString('name')!=window.username&&getQueryString('name')){
        var author=getQueryString('name');
        //是否关注
        isMarked(author);
    }else if(window.username){
        author=window.username;
        $('.btn-others').hide();
    }else {
        location.href='login.html';
    }
    $('.nickName').html(decodeURIComponent(author));

    //获取文章列表
    getDocumentList(author,1);

    //关注
    $('.focus').click(function(){
        var data={
            aimUser:author
        };
        if($(this).html()=='加关注'){
            ajaxHeader('/mark',data,function(data){
                $('.focus').html('已关注');
            });
        }else{
            ajaxHeader('/unMark',data,function(data){
                $('.focus').html('加关注');
            });
        }
    });

    //私信
    $('.chat').click(function(){
        location.href='letters.html?receiver='+author;
    });
});

//获取文章列表
function getDocumentList(author,page){
    var data={
        author:author,
        page:page?page:1
    };
    ajaxRequest('/getDocumentListByAuthor',data,function(data){
        $('.article-list').empty();
        var newData=rankByTime(data);
        for(var i=0;i<newData.length;i++){
            $('.article-list').append("<div class='article'>"+$('#article').html()+"</div>");
            var articleNo=$('.article').eq(i);
            var date=transformDate(newData[i].time.time);
            var title=decodeURIComponent(newData[i].title);
            var preview=decodeURIComponent(newData[i].preview);
            var author=decodeURIComponent(newData[i].author);
            console.log(title);
            if(getQueryString('name')){
                articleNo.find('h2 a').html(title).attr('href','article.html?id='+newData[i].id+'&name='+author);
                articleNo.find('.readMore').attr('href','article.html?id='+newData[i].id+'&name='+author);
            }else{
                articleNo.find('h2 a').html(title).attr('href','article.html?id='+newData[i].id);
                articleNo.find('.readMore').attr('href','article.html?id='+newData[i].id);
            }
            articleNo.find('.author').html(author);
            articleNo.find('.article-body').html(preview);
            articleNo.find('.date').html(date);
            articleNo.find('.readerNum').html(newData[i].reader);
        }
    });
}

function isMarked(author){
    console.log(author);
    var data={
        aimUser:author
    };
    ajaxHeader('/isMarked',data,function(response){
        if(response.return){
            $('.focus').html('已关注');
        }else{
            $('.focus').html('加关注');
        }
    });
}
