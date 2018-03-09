package com.revature.hydra.panel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//@EnableSwagger2
@SpringBootApplication
//@EnableEurekaClient
//@ComponentScan({"com.revature.hydra.panel.config", "com.revature.hydra.panel.service"})
@EntityScan("com.revature.beans")
public class PanelRepositoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PanelRepositoryServiceApplication.class, args);
	}
	
//	@Bean
//    public Docket api() { 
//        return new Docket(DocumentationType.SWAGGER_2)  
//          .select()                                  
//          .apis(RequestHandlerSelectors.basePackage("com.revature.hydra.panel.controller"))              
//          .paths(PathSelectors.any())                          
//          .build();
//    }
}
