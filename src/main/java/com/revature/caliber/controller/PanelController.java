package com.revature.caliber.controller;


import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.model.Panel;
import com.revature.caliber.model.PanelFeedback;
import com.revature.caliber.model.PanelStatus;
import com.revature.caliber.service.PanelCompositionMessagingService;
import com.revature.caliber.service.PanelCompositionService;
import com.revature.caliber.exceptions.MalformedRequestException;
//import com.revature.security.models.SalesforceUser;

/**
 * @author Connor Monson
 * @author Matt 'Spring Data' Prass
 * @author Nathan Kozsuta
 */
@RestController
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class PanelController {

	private static final Logger log = Logger.getLogger(PanelController.class);

	//@Autowired
	//private TrainingService trainingService;
	@Autowired
	private PanelCompositionService panelService;
	@Autowired
	private PanelCompositionMessagingService trainingService;

	public void setPanelService(PanelCompositionService panelService) {
		this.panelService = panelService;
	}

	@RequestMapping(value = "/panel/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING','PANEL')")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public ResponseEntity<List<Panel>> findAll() {
		log.debug("Getting all panels");
		List<Panel> panels = panelService.findAll();
		HttpStatus status = panels.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		return new ResponseEntity<>(panels, status);
	}

	@RequestMapping(value = "/panel/{panelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING','PANEL')")
	public ResponseEntity<Panel> findPanelById(@PathVariable int panelId) {
		log.debug("Getting category: " + panelId);
		Panel panel = panelService.findOne(panelId);
		if (panel == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(panel, HttpStatus.OK);
	}

	@RequestMapping(value = "/panel/trainee/{traineeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING','PANEL')")
	public ResponseEntity<List<Panel>> findPanelByTraineeId(@PathVariable int traineeId) {
		log.debug("Getting category: " + traineeId);
		//if (trainingService.findTrainee(traineeId) == null) {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		//}
		List<Panel> panels = panelService.findAllByTrainee(traineeId);
		HttpStatus status = panels.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		log.info(panels);
		return new ResponseEntity<>(panels, status);
	}

	@RequestMapping(value = "/panel/repanel/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING','PANEL')")
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public ResponseEntity<List<Panel>> findAllRepanel() {
		log.debug("Getting all panels with repanel");
		List<Panel> panels = panelService.findAllRepanel();
		HttpStatus status = panels.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
		return new ResponseEntity<>(panels, status);
	}

	@RequestMapping(value = "/panel/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP','PANEL')")
	public ResponseEntity<Panel> updatePanel(@Valid @RequestBody Panel panel) {
		panelService.update(panel);
		return new ResponseEntity<>(panel, HttpStatus.OK);
	}

	@RequestMapping(value = "/panel/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP','PANEL')")
	public ResponseEntity<Panel> savePanel(@CookieValue("email") String email, @CookieValue("role") String role, @Valid @RequestBody Panel panel) throws MalformedRequestException {

		/*
		SalesforceUser user = (SalesforceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info(user.getEmail());
		panel.setPanelist(trainingService.findTrainer(user.getEmail()));
		panelService.createPanel(panel);
		return new ResponseEntity<>(panel, HttpStatus.CREATED);
		*/
		
		panel.setPanelist(trainingService.findTrainer(email));
        log.info(email);
        panelService.save(panel);
        return new ResponseEntity<>(panel, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/panel/delete/{panelId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'TRAINER', 'PANEL')")
	public ResponseEntity<Void> deleteAssessment(@PathVariable int panelId) {
		log.info("Deleting panel: " + panelId);
		if (panelService.findOne(panelId) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		panelService.delete(panelId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Gets all trainees with last panel status = Repanel
	 *
	 * @return the all batches
	 */
	@RequestMapping(value = { "/panel/repanel/recent" },
			method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'STAGING','PANEL')")
	public ResponseEntity<List<Panel>> getAllRecentRepanel() {
		log.info("Fetching all trainees whose last panel status was Repanel");
		List<Panel> panels = panelService.findAllRepanel();
		if (panels == null || panels.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<>(panels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/all/reports/batch/{batchId}/panel-batch-all-trainees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	@PreAuthorize("hasAnyRole('VP', 'QC', 'TRAINER', 'STAGING', 'PANEL')")
	public ResponseEntity<List<Map<String, String>>> getBatchAllTraineesPanelTable(
			@PathVariable Integer batchId) {
		log.info("getBatchOverallPanelTable   ===>   /all/reports/batch/{batchId}/overall/panel-batch-overall");
		//if (panelService.getBatchPanels(batchId).isEmpty()) {
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		//}
		//return new ResponseEntity<>(panelService.getBatchPanels(batchId), HttpStatus.OK);
		return null;
	}
}
