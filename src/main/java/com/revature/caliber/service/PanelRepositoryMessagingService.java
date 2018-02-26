package com.revature.caliber.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.revature.caliber.model.Panel;
import com.revature.caliber.model.SimplePanel;

@Service
public class PanelRepositoryMessagingService {

	@Autowired
	private PanelRepositoryRequestDispatcher panelRepositoryRequestDispatcer;

	@RabbitListener(queues = "revature.caliber.repos.panel")
	public SimplePanel receiveSingleSimplePanelRequest(String message) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(message);
		JsonObject request = element.getAsJsonObject();

		return panelRepositoryRequestDispatcer.processSingleSimplePanelRequest(request);

	}
	
	@RabbitListener(queues = "revature.caliber.service.panel.list")
//	@RabbitListener(queues = "revature.caliber.service.test.list")
	public List<Panel> receiveListPanelRequest(String message) {
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(message);
		JsonObject request = element.getAsJsonObject();
		System.out.println(message);
		return panelRepositoryRequestDispatcer.processSinglePanelRequest(request);
	}
}
