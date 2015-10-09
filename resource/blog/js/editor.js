$(function(){

    markdown();
    $('.nickName').html(decodeURIComponent(window.username));
});
function markdown(){
    var opts = {
        container: 'epiceditor',
        textarea: null,
        basePath: 'epiceditor',
        clientSideStorage: true,
        localStorageName: 'epiceditor',
        useNativeFullscreen: true,
        parser: marked,
        file: {
            name: 'epiceditor',
            defaultContent: '',
            autoSave: 100
        },
        theme: {
            base: '../../../libs/EpicEditor/themes/base/epiceditor.css',
            preview: '../../../libs/EpicEditor/themes/preview/github.css',
            editor: '../../../libs/EpicEditor/themes/editor/epic-light.css'
        },
        button: {
            preview: true,
            fullscreen: true,
            bar: "auto"
        },
        focusOnLoad: false,
        shortcut: {
            modifier: 18,
            fullscreen: 70,
            preview: 80
        },
        string: {
            togglePreview: 'Toggle Preview Mode',
            toggleEdit: 'Toggle Edit Mode',
            toggleFullscreen: 'Enter Fullscreen'
        },
        autogrow:{
            minHeight:100,
            maxHeight:400,
            scroll:true
        }
    };
    var editor = new EpicEditor(opts).load();
    var getEditor=editor.getElement('editor').body;
    //var utilbar=$(editor.getElement('wrapper')).find('#epiceditor-utilbar');
    //$(utilbar).append('<button title="bold" class="epiceditor-bold-btn">B</button>');
    //$(utilbar).find('epiceditor-bold-btn').css('background-image');
    $('.submit').click(function () {
        var sendVal=editor.getElement('previewer').body.innerHTML;
        var preview=encodeURIComponent(sendVal.slice(0,99));
        var title=encodeURIComponent($('.blog-title input').val());
        addDocument(title,encodeURIComponent(sendVal),preview);
    });
    /*$(utilbar).find('.epiceditor-bold-btn').click(function(){

    });*/

    boldText(getEditor);
    italicText(getEditor);
    headerText(getEditor);
    addLink(getEditor);
    uploadImage($('.upload'),getEditor);
    addCode(getEditor);
}

//粗体
function boldText(getEditor){
    $('.su-tool-bold').click(function(){
        $(getEditor).append('****');
    });
}

//斜体
function italicText(getEditor){
    $('.su-tool-italic').click(function(){
        $(getEditor).append('**');
    });
}

//标题
function headerText(getEditor){
    $('.su-tool-head').click(function(){
        $(getEditor).append('###');
    });
}

//添加链接
function addLink(getEditor){
    $('.confirm').click(function(){
        var linkAddress=$('.linkAddress').val();
        var linkFont=$('.linkFont').val();
        $(getEditor).append('['+linkFont+'](http://'+linkAddress+')');
    });
}

//添加代码
function addCode(getEditor){
    $('.su-tool-code').click(function(){
        $(getEditor).append('``');
    });
}

function addDocument(title,body,preview){
    var data={
        title:title,
        body:body,
        preview:preview.length
    };
    ajaxHeader('/addDocument',data,function(data){
        //location.href='index.html';
    });
}


//上传图片
function uploadImage(target,getEditor){
    target.change(function(event){
        var file=this.files[0];
        if(!file)  return null;
        var rFilter=/^(image\/bmp|image\/gif|image\/jpeg|image\/png|image\/tiff)$/i;
        if(!checkUploadAccess()){
            alert('没有上传图片权限');
            return null;
        }else{
            if(!rFilter.test(file.type)){
                alert('请选择图片');
                return null;
            }
        }
        uploadImg(file,getEditor);
    });
}

function uploadImg(file,getEditor) {
    //var formData = new FormData($('form')[0]);
    //formData.append('img', file);
    var xhr=false;
    var fileType=(file.type).toString().slice(6);
    try {
        xhr = new XMLHttpRequest();
    } catch(e) {
        try {
            xhr = new ActiveXObject('Msxml2.XMLHTTP');
        } catch (e) {
            xhr = new ActiveXObject('Microsoft.XMLHTTP');
        }
    }
    xhr.open('POST', '/uploadImage');
    xhr.setRequestHeader('username',window.username);
    xhr.setRequestHeader('password',window.password);
    xhr.setRequestHeader('File-Type',fileType);
    xhr.send(file);
    xhr.onreadystatechange = function(e) {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var data=JSON.parse(e.target.response);
            var hostName=window.location.hostname;
            var fileName=data.data.return;
            //$(getEditor).append('<p><img src="http://'+hostName+':8001/'+fileName+'"></p>');
            $(getEditor).append('![Alt text](http://'+hostName+':8001/'+fileName+')');
        }
    };
}
function checkUploadAccess(){
    ajaxHeader('/checkUploadAccess',null,function(data){
    });
    return true;
}


/*
function getImage(fileName){
    console.log(fileName);
    var data={
        fileName:"http://0.0.0.0:8001/"+fileName
    };
    ajaxRequest('/getImage',data,function(data){

    });
}*/
