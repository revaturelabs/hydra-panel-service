package com.revature.caliber.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.revature.caliber.model.SimplePanel;

public interface PanelRepository extends JpaRepository<SimplePanel, Integer> {

	/**
	 * find by panel id
	 * 
	 * @param id
	 * @return a panel found with the id parameter
	 */
	SimplePanel findOne(Integer id);

	/**
	 * find all panels Useful for listing available panels
	 * 
	 * @return a list of all panels
	 */
	List<SimplePanel> findAll();

	/**
	 * find by repanels ordered descending by interview date
	 * 
	 * @return a list of panels that have a status of Repanel
	 */
	@Query("SELECT p FROM SimplePanel p WHERE p.statusId = com.revature.caliber.model.PanelStatus.Repanel ORDER BY p.interviewDate DESC")
	List<SimplePanel> findAllRepanels();

	/**
	 * find panels in the last 14 days
	 * 
	 * @return a list of panels that happen in the last 14 days
	 */
	@Query("SELECT p FROM SimplePanel p WHERE p.interviewDate >= TRUNC(SYSDATE) - 13")
	List<SimplePanel> findRecentPanels();

	/**
	 * find by trainee id
	 * 
	 * @param traineeID
	 *            the traineeId that identifies the trainee
	 * @return a list of panels associated with the trainee
	 */
	List<SimplePanel> findByTraineeId(Integer traineeId);

	/**
	 * find all trainees and panels
	 * 
	 * @param batchId
	 * @return a list of trainees and panels by batch id
	 */
	// List<SimpleTrainee> findAllTraineesAndPanelsByBatch(int batchId);

	/**
	 * Convenience method save a panel
	 * 
	 * @return a new panel
	 */
	@SuppressWarnings("unchecked")
	SimplePanel save(SimplePanel panel);

	// update();

	/**
	 * Convenience method delete a panel by panel id
	 * 
	 * @return a panel that was deleted by panel id
	 */
	void delete(Integer id);
	
	void deleteByTraineeId(Integer traineeId);
}