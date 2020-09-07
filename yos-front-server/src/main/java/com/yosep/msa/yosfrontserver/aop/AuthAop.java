package com.yosep.msa.yosfrontserver.aop;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.yosep.msa.yosfrontserver.util.AuthUtil;

@Aspect
@Component
public class AuthAop {
	@Pointcut("execution(public * com.yosep.msa.yosfrontserver.client.*Controller.*(..))"
			+ "&& !@annotation(com.yosep.msa.yosfrontserver.annotation.NoAuthMark)")
	public void controllerMethods() {}
	
	@Autowired
	RestTemplate restTemplate;
	
	@SuppressWarnings({ "unused", "rawtypes" })
	@Around("controllerMethods()")
	public Object processCheckAuth(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("auth aop start");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		ModelAndView mav;
		if(request.getCookies() == null) {
			mav = new ModelAndView();
			mav.setViewName("redirect:/login");
			
			return mav;
		}
		
		Cookie userAuthCookie = AuthUtil.getAuthCookie(request.getCookies());
		if(userAuthCookie == null) {
			mav = new ModelAndView();
			mav.setViewName("redirect:/login");
			
			return mav;
		}
		
		String token = userAuthCookie.getValue();
		
		if(userAuthCookie == null) {
			mav = new ModelAndView();
			mav.setViewName("redirect:/login");
			return mav;
		}
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<MultivaluedMap<String, String>> httpEntity = new HttpEntity<MultivaluedMap<String,String>>(headers);
			ResponseEntity response = restTemplate.exchange("http://3.35.107.191:8060/checkAuth", HttpMethod.POST,httpEntity,Map.class);
			
		}catch(HttpClientErrorException e) {
			HttpStatus httpStatus = e.getStatusCode();
			httpStatus.value();
			if(httpStatus.value() == 401) {
				mav = new ModelAndView();
				mav.setViewName("redirect:/login");
				return mav;
			}
		}
		
		Object result = pjp.proceed();
		
		return result;
	}
	
	
}
