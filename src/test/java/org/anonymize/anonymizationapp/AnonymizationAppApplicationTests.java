package org.anonymize.anonymizationapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AnonymizationAppApplicationTests {

	@Test
	public void contextLoads() {
	}
	    
	TestRestTemplate restTemplate;
	URL base;
	@LocalServerPort int port;
	 
	@Before
	public void setUp() throws MalformedURLException {
		restTemplate = new TestRestTemplate("user", "password");
	    base = new URL("https://localhost:" + port);
	}
	 
}
