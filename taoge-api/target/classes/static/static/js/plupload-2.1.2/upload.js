function removeUploadFile(id) {
    $("#" + id).remove();
}
$.ossUpload = function(aliOss, callback, option){
    option = $.extend({}, option);
    var g_object_name = '';
    function random_string(len) {
   　　len = len || 32;
   　　var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
   　　var pwd = '';
   　　for (i = 0; i < len; i++) {
       　　pwd += chars.charAt(Math.floor(Math.random() * chars.length));
       }
       return pwd;
   }
   function get_suffix(filename) {
        pos = filename.lastIndexOf('.');
        return pos != -1 ? filename.substring(pos) : '';
    }
    function getG_object_name(filename){
        if (g_object_name == '') {
            g_object_name = aliOss.dir + "/" + random_string(10) + get_suffix(filename);
        }
        return g_object_name;
    }
    function set_upload_param(up, filename, ret) {
         if (ret) {
             g_object_name = getG_object_name(filename);
         }
         up.setOption({
             'url': aliOss.host,
             'multipart_params': {
                 'key' : g_object_name,
                 'policy': aliOss.policy,
                 'OSSAccessKeyId': aliOss.accessId,
                 'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
                 'signature': aliOss.signature,
             }
         });
         up.start();
    }
    var uploader = new plupload.Uploader({
         runtimes : 'html5,flash,silverlight,html4',
         browse_button : option.browse_button || 'selectfiles',
         url : aliOss.host,
         init: {
             PostInit: function() {
                 document.getElementById('ossfile').innerHTML = '';
                 document.getElementById(option.postfiles || 'postfiles').onclick = function() {
                     set_upload_param(uploader, '', false);
                     return false;
                 };
             },
             FilesAdded: function(up, files) {
                if (option.addFiles) {
                    option.addFiles(files);
                    console.log("addFile")
                    return;
                }
                 plupload.each(files, function(file) {
                     document.getElementById('ossfile').innerHTML += '<div id="' + file.id + '" style="position: relative">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
                     +'<div class="progress" style="width:200px;"><div class="progress-bar" style="width: 0%"></div></div>'
                     + '<span style="height: 14px; line-height: 10px; margin-left: 10px; top: 0px; position: absolute; left: 200px; font-size: 16px; cursor: pointer" onclick="removeUploadFile(\'' + file.id + '\')">x</span>'
                     + '</div>';
                 });
             },
             BeforeUpload: function(up, file) {
                 set_upload_param(up, file.name, true);
             },
             UploadProgress: function(up, file) {
                 var d = document.getElementById(file.id);
                 d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
                 var prog = d.getElementsByTagName('div')[0];
                 var progBar = prog.getElementsByTagName('div')[0]
                 progBar.style.width= 2*file.percent+'px';
                 progBar.setAttribute('aria-valuenow', file.percent);
             },
             FileUploaded: function(up, file, info) {
                 if (info.status == 200) {
                     console.log("upload success : " + aliOss.host + "/" + g_object_name);
                     console.log("file.id : " + file.id);
                     setTimeout(function(){
                        $("#" + file.id).remove();
                     }, 2000);
                     callback && callback(aliOss.host + "/" + g_object_name);
                     g_object_name = '';
                 }else {
                     console.log("upload fail : " + info.response);
                 }
             },
             Error: function(up, err) {
                 console.log(err.response);
             }
         }
     })
    uploader.init();
};
