package com.example.sadovod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SadovodApplication {

	public static void main(String[] args) {
		SpringApplication.run(SadovodApplication.class, args);
	}

}
