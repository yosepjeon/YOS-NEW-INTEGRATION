$(document).ready(function() {
    console.log('ready!');
    $('#productProfileImageFiles').change(function(){
        console.log('change!');
        fileBuffer = [];
        const target = document.getElementsByName('productProfileImageFiles[]');
        
        Array.prototype.push.apply(fileBuffer, target[0].files);
        var html = '';
        $.each(target[0].files, function(index, file){
            const fileName = file.name;
            html += '<div class="file">';
            html += '<img src="'+URL.createObjectURL(file)+'">'
            html += '<span>'+fileName+'</span>';
            html += '<span>기간 '+'<input type="text" style="width:250px/"></span>';
            html += '<a href="#" id="removeImg">╳</a>';
            html += '</div>';
            const fileEx = fileName.slice(fileName.indexOf(".") + 1).toLowerCase();
            if(fileEx != "jpg" && fileEx != "png" &&  fileEx != "gif" &&  fileEx != "bmp" && fileEx != "wmv" && fileEx != "mp4" && fileEx != "avi"){
                alert("파일은 (jpg, png, gif, bmp, wmv, mp4, avi) 형식만 등록 가능합니다.");
                resetFile();
                return false;
            }
            console.log('!!');
            $('.fileList').append(html);
        });
 
    });

    $('#addToCart').on('click',function() {
        
    });

    $('#removeImg').on('click', function(){
        const fileIndex = $(this).parent().index();
         fileBuffer.splice(fileIndex,1);
         $('.fileList>div:eq('+fileIndex+')').remove();
    });
   
    $('#removeImg').on('click', function(){
        const fileIndex = $(this).parent().index();
         fileBuffer.splice(fileIndex,1);
         fileArray.splice(fileIndex,1);
         $('.fileList>div:eq('+fileIndex+')').remove();
         
        const target = document.getElementsByName('productProfileImageFiles[]');
        console.log(fileBuffer);
        console.log(target[0].files);
    });

    // $(document).on('click', '#removeImg', function(){
    //     const fileIndex = $(this).parent().index();
    //      fileBuffer.splice(fileIndex,1);
    //      $('.fileList>div:eq('+fileIndex+')').remove();
    // });
   
    // $(document).on('click', '#removeImg', function(){
    //     const fileIndex = $(this).parent().index();
    //      fileBuffer.splice(fileIndex,1);
    //      fileArray.splice(fileIndex,1);
    //      $('.fileList>div:eq('+fileIndex+')').remove();
         
    //     const target = document.getElementsByName('productProfileImageFiles[]');
    //     console.log(fileBuffer);
    //     console.log(target[0].files);
    // });
    
    // $('#createProductBtn').on('click',function() {
    //     var productProfileImageFiles = $('#productProfileImageFiles').val();
    //     var productName = $('#productName').val();
    //     var productId = $('#productId').val();
    //     var productQuantity = $('#productQuantity').val();
    //     var productSale = $('#productSale').val();
    //     var productDetail = $('#productDetail').val();
    //     var productPrice = $('#productPrice').val();
    //     var productDescription1_file = $('#productDescription1_file').val();
    //     var productDescription1 = $('#productDescription1').val();
    //     var productDescription2_file = $('#productDescription2_file').val();
    //     var productDescription2 = $('#productDescription2').val();
    //     var productDescription3_file = $('#productDescription3_file').val();
    //     var productDescription3 = $('#productDescription3').val();

    //     if(productProfileImageFiles === '') {
    //         alert('프로필 이미지 파일들을 넣어주세요.');
    //         $('#productProfileImageFiles').focus();
    //         return;
    //     }

    //     if(productName === '') {
    //         alert('이름을 입력하세요.');
    //         $('#productName').focus();
    //         return;
    //     }

    //     if(productId === '') {
    //         alert('제품 코드를 입력하세요.');
    //         $('#productId').focus();
    //         return;
    //     }

    //     if(productQuantity === '') {
    //         alert('수량을 입력하세요.');
    //         $('#productQuantity').focus();
    //         return;
    //     }

    //     if(productDetail === '') {
    //         alert('세부사항을 입력하세요.');
    //         $('#productDetail').focus();
    //         return;
    //     }

    //     $.ajax({
    //         url:"",
    //         type:"POST",
    //         dataType:"",
    //         processData:false,
    //     });
    // });
});