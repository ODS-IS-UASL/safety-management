package com.intent_exchange.uasl;

import org.springframework.boot.SpringApplication;

public class TestUaslApplication {

	public static void main(String[] args) {
		SpringApplication.from(UaslApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
