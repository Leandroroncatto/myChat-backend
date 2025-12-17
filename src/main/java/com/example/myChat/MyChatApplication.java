package com.example.myChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyChatApplication.class, args);
	}

}
