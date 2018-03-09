package com.revature.hydra.panel.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.JsonObject;
import com.revature.beans.Panel;
import com.revature.hydra.panel.service.PanelCompositionMessagingService;
import com.revature.hydra.panel.service.PanelCompositionService;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableEurekaClient
@Configuration
public class RepositoryProducerConfiguration {

	@Autowired
	PanelCompositionService panelCompositionService;

	@Bean
	public AmqpTemplate rabbitTemplate(ConnectionFactory factory) {
		return new RabbitTemplate(factory);
	}

	@Bean
	public PanelCompositionService panelCompositionService() {
		return new PanelCompositionService();
	}
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.revature.hydra.panel.controller"))              
          .paths(PathSelectors.any())                          
          .build();
    }

//	@Bean
//	public CommandLineRunner runner() {
//		return args -> {
//			Panel panel = panelCompositionService.findOne(1);
//			System.out.println(panel);
//		};
//	}
}
