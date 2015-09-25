$(function(){

    markdown();
    $('.nickName').html(decodeURIComponent(window.username));
    var oMyForm = new FormData();
    oMyForm.append("username", "Groucho");
    console.log(oMyForm);
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
    var utilbar=$(editor.getElement('wrapper')).find('#epiceditor-utilbar');
    $(utilbar).append('<button title="bold" class="epiceditor-bold-btn">B</button>');
    //$(utilbar).find('epiceditor-bold-btn').css('background-image');
    $('.submit').click(function () {
        var sendVal=editor.getElement('previewer').body.innerHTML;
        var preview=encodeURIComponent(sendVal.slice(0,99));
        var title=encodeURIComponent($('.blog-title input').val());
        addDocument(title,encodeURIComponent(sendVal),preview);
        console.log(document.selection);
    });
    $(utilbar).find('.epiceditor-bold-btn').click(function(){

    });
    uploadImage($('.upload'));
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

function uploadImage(target){
    target.change(function(event){
        var file=this.files[0],
            _parent=$(this).parent(),
            _self=$(this);
        console.log(file);
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
        upload(_parent,file);
    });
}
function upload(which, file) {
    console.log(file);
    var formData = new FormData($('form')[0]);
    formData.append('img', file);
    console.log(formData);
    $.ajax({
        url:'/uploadImage',
        type:'POST',
        dataType:'json',
        data:formData,
        beforeSend:function(XML){
            XML.setRequestHeader('username',window.username);
            XML.setRequestHeader('password',window.password);
        },
        success:function(response){
            if(response.return==200){

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
function checkUploadAccess(){
    ajaxHeader('/checkUploadAccess',null,function(data){
    });
    return true;
}
function getImage(fileName){
    var data={
        fileName:fileName
    };
    ajaxRequest('/getImage',data,function(data){

    });
}