$(function(){

    markdown();
    $('.nickName').html(decodeURIComponent(window.username));
    $('.submit').click(function(){
        var value=$('.blog-content textarea').val();
        var preview=encodeURIComponent(value.substr(0,100));
        var data={
            title:encodeURIComponent($('.blog-title input').val()),
            body:encodeURIComponent(value),
            preview:preview.length
        };
        ajaxHeader('/addDocument',data,function(data){
                location.href='index.html';
        });
    });
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
        autogrow: false
    };
    var editor = new EpicEditor(opts).load();

    $('.submit').click(function () {
        console.log(editor.getElement('editor').body.innerHTML); // Returns the editor's content
    });
}