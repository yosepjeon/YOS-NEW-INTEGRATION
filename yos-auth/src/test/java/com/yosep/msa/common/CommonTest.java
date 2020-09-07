package com.yosep.msa.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonTest {
	@Value("${security.oauth2.resource.jwt.key-value}")
	String jwtKey;
	
	@Test
	public void jwtKeyTest() {
		System.out.println(jwtKey);
	}

}
