package com.revature.hydra.panel.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.revature.beans.SimpleTrainee;
import com.revature.beans.SimpleTrainer;
import com.revature.beans.Trainer;

@Service
public class PanelCompositionMessagingService {

	@Autowired
	AmqpTemplate rabbitTemplate;

	private static final String SINGLE_TRAINEE_ROUTING_KEY = "JyoH3uRmktGn9MnW";
	private static final String SINGLE_TRAINER_ROUTING_KEY = "9xdaX72tPYuz8xDP";
	private static final String SINGLE_PANEL_FEEDBACK_ROUTING_KEY = "4jZ2GMxLP7VyQPBn";
	private static final String RABBIT_REPO_EXCHANGE = "revature.hydra.repos";

	public SimpleTrainee sendSingleSimpleTraineeRequest(Integer traineeId) {
		JsonObject traineeRequest = new JsonObject();

		traineeRequest.addProperty("methodName", "findOne");
		traineeRequest.addProperty("traineeId", traineeId);

		return (SimpleTrainee) rabbitTemplate.convertSendAndReceive(RABBIT_REPO_EXCHANGE, SINGLE_TRAINEE_ROUTING_KEY,
				traineeRequest.toString());
	}

	public SimpleTrainer sendSingleSimpleTrainerRequest(Integer trainerId) {
		JsonObject trainerRequest = new JsonObject();

		trainerRequest.addProperty("methodName", "findOne");
		trainerRequest.addProperty("trainerId", trainerId);

		return (SimpleTrainer) rabbitTemplate.convertSendAndReceive(RABBIT_REPO_EXCHANGE, SINGLE_TRAINER_ROUTING_KEY,
				trainerRequest.toString());
	}
	
	public void sendPanelFeedbackDeleteRequest(Integer panelId) {
		JsonObject panelFeedbackRequest = new JsonObject();
		
		panelFeedbackRequest.addProperty("methodName", "delete");
		panelFeedbackRequest.addProperty("panelId", panelId);
		
		rabbitTemplate.convertAndSend(RABBIT_REPO_EXCHANGE, SINGLE_PANEL_FEEDBACK_ROUTING_KEY, panelFeedbackRequest.toString());
	}
	public Trainer findTrainer(String email) {
		JsonObject trainerRequest = new JsonObject();
		trainerRequest.addProperty("methodName", "findByEmail");
		trainerRequest.addProperty("trainerEmail", email);
		return (Trainer) rabbitTemplate.convertSendAndReceive(RABBIT_REPO_EXCHANGE, SINGLE_TRAINER_ROUTING_KEY, trainerRequest.toString());
	}
	
}