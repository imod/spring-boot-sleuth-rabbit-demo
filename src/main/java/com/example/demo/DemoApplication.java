package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	private MyMessageSender sender;

	@RequestMapping("/")
	public String home() {
		LOGGER.info("Handling home");
		String greeting = "Hello World!";
		sender.send(new MyMessage(greeting));
		return greeting;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}