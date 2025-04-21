package com.intent_exchange.drone_highway;

import org.springframework.boot.SpringApplication;

public class TestDroneHighwayApplication {

	public static void main(String[] args) {
		SpringApplication.from(DroneHighwayApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
