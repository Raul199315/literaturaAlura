package com.challenge.literatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication {

	public static void main(String[] args) {

		var context = SpringApplication.run(LiteraturaApplication.class, args);
		ConvertirJson convertirJson = context.getBean(ConvertirJson.class);
		convertirJson.menu();
	}

}
