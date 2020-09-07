/**
 * 
 */
$(document).ready(function() {
	$('#checkDupIdBtn').on('click',function() {
		var userName = $('#userName').val();
		console.log('call');
		
		if(userName === '') {
			alert('ID를 입력하세요.');
			$('#userId').focus();
			return;
		}
		
		$.ajax({
			url:"http://yos.yoggaebi.com/checkdupid?userName=" + userName,
			type:"GET",
			dataType:"text",
			processData:false,
			contentType:true,
			success: function(data) {
				if(data === "true") {
					console.log(data);
					alert('이미 사용중인 아이디입니다.');
					$("#isCheckId").val("unChecked");
					$('#userName').val("");
				}else if(data === "false") {
					console.log(data);
					$("#isCheckId").val("checked");
					alert('사용가능한 아이디 입니다.');
				}
			},
			error: function() {
				console.log(userName);
				alert("error.");
			}
		});
	});
	
	$("#registerForm").on("submit",function() {
		if($("#isCheckId").val() === "unChecked") {
			alert("중복확인 해주세요.");
			event.preventDefault();
			$("isCheckId").focus();
		}
	});

	$('#registerBtn').on('click',() => {
		let userName = document.getElementById('userName').value;
		let password = document.getElementById('password').value;
		let name = document.getElementById('name').value;
		let email = document.getElementById('email').value;
		let phone = document.getElementById('phone').value;
		let postCode = document.getElementById('postcode').value;
		let roadAddress = document.getElementById('roadAddress').value;
		let jibunAddress = document.getElementById('jibunAddress').value;
		let extraAddress = document.getElementById('extraAddress').value;
		let detailAddress = document.getElementById('detailAddress').value;

		let originUrl = window.location.origin;
		let XSRF_TOKEN = getCookieValue('XSRF-TOKEN');
		let formData = new FormData(document.getElementById('registerForm'));

		let user = new Object();
		user.userName = userName;
		user.password = password;
		user.name = name;
		user.email = email;
		user.phone = phone;
		user.postCode = postCode;
		user.roadAddr = roadAddress;
		user.jibunAddr = jibunAddress;
		user.extraAddr = extraAddress;
		user.detailAddr = detailAddress;
		
		let jsonData = JSON.stringify(user);
		
		console.log(jsonData);
		
		$.ajax({
			type: 'post',
			url: originUrl + '/createUser',
			data: jsonData,
			cache: false,
			headers:{"YCT-XSRF-TOKEN": XSRF_TOKEN},
			contentType: "application/json",
			processData: false,
			success: (response) => {
				//window.location.href= window.location.origin + '/login';
				window.location.href = "http://yos.yoggaebi.com/login";
			},
			error:(error) => {
				alert('생성 실패!!! ' + error);
			}
		});
	});
});