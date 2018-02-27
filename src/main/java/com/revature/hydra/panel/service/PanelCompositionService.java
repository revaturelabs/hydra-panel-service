package com.revature.hydra.panel.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.revature.beans.Panel;
import com.revature.beans.SimplePanel;
import com.revature.beans.SimpleTrainee;
import com.revature.beans.SimpleTrainer;
import com.revature.beans.Trainee;
import com.revature.beans.Trainer;
import com.revature.beans.TrainingStatus;
import com.revature.hydra.panel.data.PanelRepository;

public class PanelCompositionService {

	@Autowired
	private PanelRepository panelRepository;
	@Autowired
	private PanelCompositionMessagingService panelCompositionMessagingService;

	public Panel findOne(Integer panelId) {
		SimplePanel basis = panelRepository.findOne(panelId);
		Panel result = composePanel(basis);

		return result;
	}

	public List<Panel> findAll() {
		List<SimplePanel> basis = panelRepository.findAll();
		List<Panel> result = composeListOfPanels(basis, false);

		return result;
	}

	public List<Panel> findAllRepanel() {
		List<SimplePanel> basis = panelRepository.findAllRepanels();
		List<Panel> result = composeListOfPanels(basis, false);

		return result;

	}

	public List<Panel> findBiWeeklyPanels() {
		List<SimplePanel> basis = panelRepository.findRecentPanels();
		List<Panel> result = composeListOfPanels(basis, false);

		return result;
	}

	public List<Panel> findAllByTrainee(Integer traineeId) {
		List<SimplePanel> basis = panelRepository.findByTraineeId(traineeId);
		List<Panel> result = composeListOfPanels(basis, false);

		return result;
	}

	// move to trainee
	// public List<Trainee> findAllTraineesAndPanels(Integer batchId) {
	// List<SimpleTrainee> basis =
	// panelRepository.findAllTraineesAndPanelsByBatch(batchId);
	// List<Trainee> result = composeListOfPanels(basis, false);
	//
	// return result;
	// }

	public void save(Panel panel) {
		SimplePanel simplePanel = new SimplePanel(panel);
	 
		panelRepository.save(simplePanel);
	}
	
	public void update(Panel panel) {
		save(panel);
	}
	
	public void delete(Integer panelId) {
		if(panelRepository.findOne(panelId) != null) {
			panelRepository.delete(panelId);
			panelCompositionMessagingService.sendPanelFeedbackDeleteRequest(panelId);
		}
	}

	private List<Panel> composeListOfPanels(List<SimplePanel> src, boolean includeDropped) {
		List<Panel> dest = new LinkedList<Panel>();

		for (SimplePanel curr : src) {
			Panel panel = new Panel(curr);

			if (!includeDropped && panel.getTrainee().getTrainingStatus() != TrainingStatus.Dropped)
				dest.add(new Panel(curr));
			else if (includeDropped)
				dest.add(new Panel(curr));
		}
		return dest;
	}

	private Panel composePanel(SimplePanel src) {
		SimpleTrainee simpleTrainee = panelCompositionMessagingService
				.sendSingleSimpleTraineeRequest(src.getTraineeId());
		SimpleTrainer simpleTrainer = panelCompositionMessagingService
				.sendSingleSimpleTrainerRequest(src.getPanelist());
		
		panelCompositionMessagingService.sendSingleSimpleTrainerRequest(src.getPanelist());

		Trainee trainee = new Trainee(simpleTrainee);
		Trainer trainer = new Trainer(simpleTrainer);

		Panel dest = new Panel(src);

		dest.setTrainee(trainee);
		dest.setPanelist(trainer);

		return dest;
	}
}
