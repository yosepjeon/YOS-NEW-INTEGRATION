package com.yosep.msa.yosfrontserver.client;

//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.yosep.msa.yosfrontserver.annotation.NoAuthMark;
import com.yosep.msa.yosfrontserver.common.ErrorsResource;
import com.yosep.msa.yosfrontserver.entity.User;
import com.yosep.msa.yosfrontserver.entity.UserDTO;
import com.yosep.msa.yosfrontserver.entity.UserResource;
import com.yosep.msa.yosfrontserver.util.AuthUtil;


@CrossOrigin
@Controller
@RefreshScope
@RequestMapping(produces=MediaTypes.HAL_JSON_VALUE)
public class UserController {
	// TODO: Config Server로 이전
//	final String CLIENT_ID = "yoggaebi";
//	final String CLIENT_SECRET = "pass";
//	final String GRANT_TYPE = "refresh_token";
//	final String AUTH_URL = "http://localhost:" + 8095 + "/oauth/token";
	
	String clientCredentials = AuthUtil.CLIENT_ID + ":" + AuthUtil.CLIENT_SECRET;
	Base64.Encoder encoder = Base64.getEncoder();
	String auth = encoder.encodeToString(clientCredentials.getBytes());
	
//	private static final String USER_TOKEN = "yos-user-token";
//	private static final String USER_TOKEN_EXPIRATION = "yos-user-expiration";
//	private static final String USER_TOKEN_REFRESH_EXPIRATION = "yos-user-refresh-token";
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ModelMapper modelMapper;
	
	WebMvcLinkBuilder controllerLinkBuilder = linkTo(UserController.class);
	
//	@Auto
	ResourceOwnerPasswordResourceDetails resourceForAuth;

	OAuth2AccessToken accessTokenForAuth;
	DefaultOAuth2ClientContext clientContextForAuth;
	OAuth2RestTemplate oAuth2RestTemplateForAuth;
	
	@NoAuthMark
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request,@ModelAttribute User user) {
		ModelAndView mav = new ModelAndView();
		
		Cookie[] userCookies = request.getCookies();
		Cookie userAuthCookie = null;
		Cookie userAuthRefreshCookie = null;
		
		if(userCookies == null) {
			mav.setViewName("login");

			return mav;
		}
		
		for(Cookie userCookie : userCookies) {
			
			if(AuthUtil.USER_TOKEN.equals(userCookie.getName())) {
				userAuthCookie = userCookie;
			}
			
			if(AuthUtil.USER_TOKEN_REFRESH_EXPIRATION.equals(userCookie.getValue())) {
				userAuthRefreshCookie = userCookie;
			}
			
			if(userAuthCookie != null && userAuthRefreshCookie != null) {
				break;
			}
		}
		
		if (userAuthCookie != null || userAuthRefreshCookie != null) {
			
			mav.setViewName("redirect:/main");
			return mav;
		}
		
		mav.setViewName("login");

		return mav;
	}

	@NoAuthMark
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("register");

		return mav;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@NoAuthMark
	@RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
	public ResponseEntity loginCheck(HttpServletRequest request,HttpServletResponse response,@ModelAttribute User user, Errors errors) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic " + auth);
		
		Map<String, Object> result;
//		System.out.println(user.toString());
//		
//		Cookie[] userCookies = request.getCookies();
//		Cookie userAuthCookie = null;
//		Cookie userAuthRefreshCookie = null;
//		Cookie userIdCookie = null;
//		
//		for(Cookie userCookie : userCookies) {
//			if(USER_TOKEN.equals(userCookie.getName())) {
//				userAuthCookie = userCookie;
//			}
//			
//			if(USER_TOKEN_REFRESH_EXPIRATION.equals(userCookie.getValue())) {
//				userAuthRefreshCookie = userCookie;
//			}
//			
//			if(userAuthCookie != null && userAuthRefreshCookie != null) {
//				break;
//			}
//		}
		
//		if(userAuthCookie == null) {
//			result = new HashMap<String,Object>();
//			result.put("result", "empty_token");
//			return ResponseEntity.ok(result);
//		}
//		
//		if(userAuthCookie.getMaxAge() <= 30) {
//			
//		}
		
//		기존 인증 방식
//		accessTokenForAuth = OAuthUtil.setResourceForAuth(resourceForAuth,user);
//		userAuthCookie = new Cookie(USER_TOKEN, accessTokenForAuth.getValue());
//		userAuthCookie.setMaxAge(accessTokenForAuth.getExpiresIn());
//		userAuthCookie.setHttpOnly(true);
//		userAuthRefreshCookie = new Cookie(USER_TOKEN_REFRESH_EXPIRATION,accessTokenForAuth.getRefreshToken().getValue());
//		userAuthRefreshCookie.setHttpOnly(true);
//		
//		if(errors.hasErrors()) {
//			System.out.println("errors: " + errors);
//		}
		
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("grant_type", "password");
		parameters.add("username", user.getUserName());
		parameters.add("password", user.getPassword());
		
		HttpEntity<MultiValueMap<String, String>> authRequest = new HttpEntity<>(parameters,headers);
		ResponseEntity<Map> responseEntity;
		
		try {
			responseEntity = restTemplate.postForEntity(AuthUtil.AUTH_URL, authRequest, Map.class);
			
			Map<String, String> body = responseEntity.getBody();
			
			Cookie authCookie = new Cookie(AuthUtil.USER_TOKEN, String.valueOf(body.get("access_token")));
			System.out.println(Integer.valueOf(String.valueOf(body.get("expires_in"))));
			authCookie.setMaxAge(Integer.valueOf(String.valueOf(body.get("expires_in"))));
			authCookie.setHttpOnly(true);
			
			Cookie refreshCookie = new Cookie(AuthUtil.USER_TOKEN_REFRESH_EXPIRATION, String.valueOf(body.get("refresh_token")));
			refreshCookie.setMaxAge(60*60*24*30);
			refreshCookie.setHttpOnly(true);
			System.out.println("auth_token:" + authCookie.getValue());
			
			response.addCookie(authCookie);
			
			result = new HashMap<String, Object>();
			result.put("result", "success");
			return ResponseEntity.ok().body(result);
		}catch(HttpClientErrorException e) {
			result = new HashMap<String, Object>();
			result.put("result", "wrong_info");
			
			return ResponseEntity.ok().body(result);
		}
	}
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView showMainPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("main");
		return mav;
	}

//	@RequestMapping(value="/createUser",method=RequestMethod.POST)
//	public ResponseEntity userRegister(@ModelAttribute @Valid UserDTO user, Errors errors) {
//		System.out.println("call createUser");
//		if(errors.hasErrors()) {
//			System.out.println(errors);
////			return badRequest(errors);
//			return ResponseEntity.badRequest().body(errors);
//		}
//		
//		return null;
//	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unused" })
	@NoAuthMark
	@RequestMapping(value = "/createUser", method = RequestMethod.POST,
			produces="application/json", consumes = "application/json")
	public ResponseEntity userRegister(@RequestBody @Valid UserDTO userDTO, Errors errors) {
//		int status = restTemplate.postForObject("http://user-apigateway/api/user/register", user, int.class);
//
//		if (status == 200)
//			return "redirect:/login";
//		else
//			return "redirect:/register";
		
//		ResponseEntity result = restTemplate();
		System.out.println("call createUser");
		
		if(errors.hasErrors()) {
			System.out.println("come out errors");
			System.out.println(errors);
			return ResponseEntity.badRequest().build();
//			return "redirect:/register";
		}
		
		// TODO: userValidator 구현
		
		User user = modelMapper.map(userDTO, User.class);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserDTO> httpEntity = new HttpEntity<UserDTO>(userDTO, headers);
		ResponseEntity responseEntity = restTemplate.exchange("http://3.35.107.191:8095/user/register", HttpMethod.POST, httpEntity, Map.class);
		System.out.println("생성: " + responseEntity.toString());
		
		if(responseEntity.getStatusCodeValue() == 201) {
			WebMvcLinkBuilder selfLinkBuilder = linkTo(UserController.class).slash(user.getUserName());
//			
			URI createdUri = selfLinkBuilder.toUri();
			UserResource userResource = new UserResource(user);
			userResource.add(linkTo(UserController.class).withRel("query-user"));
			userResource.add(selfLinkBuilder.slash("update").withRel("update-user"));
			userResource.add(new Link("/docs/index#resources-users-create").withRel("profile"));
//			return ResponseEntity.created(location);
			
			return ResponseEntity.created(createdUri).body(userResource);
//			return "redirect:/login";
		}else {
			return ResponseEntity.badRequest().build();
//			return "redirect:/register";
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NoAuthMark
	@GetMapping(value = "/checkdupid")
	public ResponseEntity<String> checkDupId(@RequestParam("userName") String userId) {
		ResponseEntity re = restTemplate.getForEntity("http://3.35.107.191:8095/user/checkdupid?userName=" + userId, String.class);

		return re;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
	
}
