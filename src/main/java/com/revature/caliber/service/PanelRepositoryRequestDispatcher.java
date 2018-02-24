package com.revature.caliber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.revature.caliber.model.Panel;
import com.revature.caliber.model.SimplePanel;
import com.revature.caliber.repository.PanelRepository;

@Service
public class PanelRepositoryRequestDispatcher {
	
	@Autowired
	private PanelRepository panelRepository;
	
	@Autowired
	private PanelCompositionService panelCompositionService;
	
	public SimplePanel processSingleSimplePanelRequest(JsonObject request) {
		SimplePanel result = null;
		String methodName = request.get("methodName").getAsString();
		
		if(methodName.equals("findOne")) {
			Integer panelId = request.get("panelId").getAsInt();
			result = panelRepository.findOne(panelId);
		} else if(methodName.equals("delete")) {
			if(request.has("traineeId")) {
				panelRepository.deleteByTraineeId(request.get("traineeId").getAsInt());
			} else if(request.has("panelId")) {
				panelRepository.delete(request.get("panelId").getAsInt());
			}
		}
		return result;
	}

	public List<Panel> processSinglePanelRequest(JsonObject request) {
		List<Panel> result = null;
		String methodName = request.get("methodName").getAsString();
		
		if(methodName.equals("findBiWeeklyPanels")) {
			result = panelCompositionService.findBiWeeklyPanels();
		}
		
		return result;
	}
}
