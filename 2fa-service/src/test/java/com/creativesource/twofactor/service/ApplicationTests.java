package com.creativesource.fa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.creativesource.twofactor.service.Application;
import com.creativesource.twofactor.service.ApplicationSecurityConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, ApplicationSecurityConfig.class})
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
