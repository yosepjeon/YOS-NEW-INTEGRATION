/**
 * 
 */

$(document).ready(function () {
    $('#loginBtn').on('click', function () {
        console.log('login');

        let userName = document.getElementById('userName').value;
        let password = document.getElementById('password').value;
        let originUrl = window.location.origin;
        //let XSRF_TOKEN = getCookieValue('XSRF-TOKEN')
        let XSRF_TOKEN = $("meta[name='YCT-XSRF-TOKEN']").attr("content");
        let XSRF_HEADER = $("meta[name='YCT-XSRF-HEADER']").attr("content");
        
        let formData = new FormData(document.getElementById('loginForm'));

        if (userName === '') {
            alert('ID를 입력하세요.');
            $('#userName').focus();
            return;
        }

        if (password === '') {
            alert('비밀번호를 입력하세요.');
            $('#password').focus();
            return;
        }

        $.ajax({
            type: 'post',
            url: originUrl + '/loginCheck',
            data: formData,
            cache: false,
//            headers: { XSRF_TOKEN},
            beforeSend : function(xhr) {
            	xhr.setRequestHeader(XSRF_HEADER,XSRF_TOKEN);
            },
            contentType: false,
            processData: false,
            success: (data) => {
//                window.location.href= window.location.origin + '/main';
                // console.log(data);
                if(data.result === 'success') {
                     window.location.href= window.location.origin + '/main';
                }else if(data.result === 'wrong_info') {
                	alert('등록되지 않은 회원이거나, 아이디 또는 비밀번호가 틀렸습니다.');
                }else if(data.result === 'empty_refresh_token'){
                    alert('다시 로그인 바랍니다.');
                }
            },
            error: (error) => {
                alert('Error');
                console.log(error);
            }
        });
    });
});

const getCookieValue = (key) => {
    let cookieKey = key + "=";
    let result = "";
    const cookieArr = document.cookie.split(";");

    for (let i = 0; i < cookieArr.length; i++) {
        if (cookieArr[i][0] === " ") {
            cookieArr[i] = cookieArr[i].substring(1);
        }

        if (cookieArr[i].indexOf(cookieKey) === 0) {
            result = cookieArr[i].slice(cookieKey.length, cookieArr[i].length);
            return result;
        }
    }
    return result;
}