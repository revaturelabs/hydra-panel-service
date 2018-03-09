package com.revature.hydra.panel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@ComponentScan({"com.revature.hydra.panel.config", "com.revature.hydra.panel.service"})
@EntityScan("com.revature.beans")
public class PanelRepositoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PanelRepositoryServiceApplication.class, args);
	}
}
