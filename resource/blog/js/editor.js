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
        //autogrow: false,

    };
    var editor = new EpicEditor(opts).load();

    $('.submit').click(function () {
        var sendVal=editor.getElement('previewer').body.innerHTML;
        var preview=encodeURIComponent(sendVal.slice(0,99));
        var title=encodeURIComponent($('.blog-title input').val());
        addDocument(title,encodeURIComponent(sendVal),preview);
        editor.focus();
    });
    $('.su-tool-code').click(function(){
        editor.focus();
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
