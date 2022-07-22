function webbizz_image_upload_handler(blobInfo, success, failure, progress) {
    var xhr, formData;

    xhr = new XMLHttpRequest();
    xhr.withCredentials = false;
    xhr.open('POST', '/tinythumb.php');

    xhr.upload.onprogress = function(e) {
        progress(e.loaded / e.total * 100);
    };

    xhr.onload = function() {
        try {
            var json;

            if (xhr.status === 403) {
                failure('HTTP Error: ' + xhr.status, { remove: true });
                return;
            }

            if (xhr.status < 200 || xhr.status >= 300) {
                failure('HTTP Error: ' + xhr.status);
                return;
            }

            json = JSON.parse(xhr.responseText);

            if (!json || typeof json.location != 'string') {
                failure('Invalid JSON: ' + xhr.responseText);
                return;
            }

            success(json.location.replace('../', ''));

        } catch (e) {
            failure('Fail to upload. Allowed EXT:jpg, jpeg, gif, png. Code: ' + xhr.status);
            //alert("허용하지않는 확장자입니다.")
            console.log("ERROR");
            return;
        }
    };

    xhr.onerror = function() {
        failure('Image upload failed due to a XHR Transport error. Code: ' + xhr.status);
    };

    formData = new FormData();
    formData.append('file', blobInfo.blob(), blobInfo.filename());

    xhr.send(formData);
}


tinymce.init({
    /* replace textarea having class .tinymce with tinymce editor */
    selector: ".mytextarea",

    /* theme of the editor */
    theme: "silver",
    skin: "oxide",
    content_css: "/css/editor-style.css",

    /* width and height of the editor */
    width: "100%",
    height: 500,
    min_height: 350,
    //21.10.14 전남혁 추가
    //파일업로드 상대경로 지정 기능 OFF
    relative_urls: false,
    //언어 타입 한글
    // language : "ko_KR",
    /////////////////////////////////

    /* display statusbar */
    statubar: true,

    /* plugin save table contextmenu directionality emoticons template paste textcolor autoresize */
    plugins: [
        "advlist autolink charmap link image imagetools lists print preview hr anchor pagebreak",
        "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
        "save table directionality emoticons template paste autoresize"
    ],

    /* toolbar */
    //21.10.18 전남혁 추가
    // | fontsizeselect | (폰트 사이즈 설정 툴바 추가)
    // 이미지 아이콘 위치 display가 줄어도 끌어내기
    // toolbar: "styleselect | fontselect | fontsizeselect | bold italic | image link | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | print preview media fullpage | forecolor backcolor",
    toolbar: "styleselect | fontselect | fontsizeselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | image link | print preview media fullpage | forecolor backcolor",

    image_upload_url: '/tinythumb.php',
    images_upload_handler: webbizz_image_upload_handler,
    image_caption: true,

    font_formats: "굴림;돋움;궁서체;바탕체; Noto Sans KR; Andale Mono=andale mono,times; Arial=arial,helvetica,sans-serif; Arial Black=arial black,avant garde; Book Antiqua=book antiqua,palatino; Comic Sans MS=comic sans ms,sans-serif; Courier New=courier new,courier; Georgia=georgia,palatino; Helvetica=helvetica; Impact=impact,chicago; Symbol=symbol; Tahoma=tahoma,arial,helvetica,sans-serif; Terminal=terminal,monaco; Times New Roman=times new roman,times; Trebuchet MS=trebuchet ms,geneva; Verdana=verdana,geneva; Webdings=webdings; Wingdings=wingdings,zapf dingbats",

    //21.10.18 전남혁 추가
    //폰트 사이즈 종류 추가
    fontsize_formats: "8pt 9pt 10pt 11pt 12pt 14pt 18pt 24pt 30pt 36pt 48pt 60pt 72pt 96pt",
    /* style */
    style_formats: [
        // {title: "Headers", items: [
        //     {title: "Header 1", format: "h1"},
        //     {title: "Header 2", format: "h2"},
        //     {title: "Header 3", format: "h3"},
        //     {title: "Header 4", format: "h4"},
        //     {title: "Header 5", format: "h5"},
        //     {title: "Header 6", format: "h6"}
        // ]},
        {
            title: "Inline",
            items: [
                { title: "Bold", icon: "bold", format: "bold" },
                { title: "Italic", icon: "italic", format: "italic" },
                { title: "Underline", icon: "underline", format: "underline" },
                { title: "Strikethrough", icon: "strikethrough", format: "strikethrough" },
                { title: "Superscript", icon: "superscript", format: "superscript" },
                { title: "Subscript", icon: "subscript", format: "subscript" },
                { title: "Code", icon: "code", format: "code" }
            ]
        },
        {
            title: "Blocks",
            items: [
                { title: "Paragraph", format: "p" },
                { title: "Blockquote", format: "blockquote" },
                { title: "Div", format: "div" },
                { title: "Pre", format: "pre" }
            ]
        },
        {
            title: "Alignment",
            items: [
                { title: "Left", icon: "alignleft", format: "alignleft" },
                { title: "Center", icon: "aligncenter", format: "aligncenter" },
                { title: "Right", icon: "alignright", format: "alignright" },
                { title: "Justify", icon: "alignjustify", format: "alignjustify" }
            ]
        }
    ]
});