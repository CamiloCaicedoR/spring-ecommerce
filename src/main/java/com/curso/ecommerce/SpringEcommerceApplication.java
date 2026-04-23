package com.curso.ecommerce;

import javax.xml.crypto.Data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEcommerceApplication.class, args);
	}

}
