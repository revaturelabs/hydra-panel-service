package com.revature.caliber.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.JsonObject;
import com.revature.caliber.model.Panel;
import com.revature.caliber.service.PanelCompositionMessagingService;
import com.revature.caliber.service.PanelCompositionService;

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

//	@Bean
//	public CommandLineRunner runner() {
//		return args -> {
//			Panel panel = panelCompositionService.findOne(1);
//			System.out.println(panel);
//		};
//	}
}
