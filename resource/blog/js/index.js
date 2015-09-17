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

    //获取分页数
    articleSize(author);

    //分页
    $('.pagination').on('click','a',function(){
        var pageNo=parseInt($(this).attr('page'));
        var pageNum=parseInt($('.pagination a:last').attr('page'));
        console.log(pageNo);
        page(pageNum,pageNo);
    });

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
function getDocumentList(page){
    var data={
        author:$('.nickName').html(),
        page:page?page:1
    };
    ajaxRequest('/getDocumentListByAuthor',data,function(data){
        if(data&&data.length!=0){
            var newData=rankByTime(data);
            for(var i=0;i<newData.length;i++){
                $('.article-list').prepend($('#article').html());
                var articleNo=$('.article').eq(0);
                var date=transformDate(newData[i].time.time);
                var title=decodeURIComponent(newData[i].title);
                var preview=decodeURIComponent(newData[i].preview);
                var author=decodeURIComponent(newData[i].author);
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
        }else{
            $('.pagination').empty();
            $('.article-list').html('<h1>空空如也</h1>')
        }
    });
}

//判断是否关注
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

//获取文章页数
function articleSize(author){
    var data={
        author:author
    };
    ajaxRequest('/getDocumentListByAuthorSize',data,function(data){
        page(data.return,1);
    });
}

//分页
function page(pageNum,pageNo){
    if(pageNum==1){
        $('.pagination').empty();
    }else{
        $('.pagination').empty()
            .append("<li><a href='#' page='1'>首页</a></li>" +
            "<li class='active'><a href='#'  page='"+pageNo+"'>"+pageNo+"</a></li>" +
            "<li><a href='#' page='"+pageNum+"'>尾页</a></li>");
        if(pageNo!=1)
            $('.pagination li:first').after("<li><a href='#' class='prev' page='"+(pageNo-1)+"'>上一页</a></li>");
        if(pageNo!=pageNum)
            $('.pagination li:last').before("<li><a href='#' class='next' page='"+(pageNo+1)+"'>下一页</a></li>");
        if(pageNo>4){
            $('.active').before("<li><a href='#'>...</a></li>")
                .before("<li><a href='#' page='"+(pageNo-3)+"'>"+(pageNo-3)+"</a></li>")
                .before("<li><a href='#' page='"+(pageNo-2)+"'>"+(pageNo-2)+"</a></li>")
                .before("<li><a href='#' page='"+(pageNo-1)+"'>"+(pageNo-1)+"</a></li>");
        }else{
            for(var i=1;i<pageNo;i++){
                $('.active').before("<li><a href='#' page='"+i+"'>"+i+"</a></li>");
            }
        }
        if(pageNum-pageNo>3){
            $('.active').after("<li><a href='#'>...</a></li>")
                .after("<li><a href='#' page='"+(pageNum)+"'>"+(pageNum)+"</a></li>")
                .after("<li><a href='#' page='"+(pageNum-1)+"'>"+(pageNum-1)+"</a></li>")
                .after("<li><a href='#' page='"+(pageNum-2)+"'>"+(pageNum-2)+"</a></li>");
        }else{
            for(i=pageNum;i>pageNo;i--){
                $('.active').after("<li><a href='#' page='"+i+"'>"+i+"</a></li>");
            }
        }
    }
    getDocumentList(pageNo);
}
