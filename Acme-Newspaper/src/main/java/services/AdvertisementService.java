package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Advertisement;
import domain.Agent;

import repositories.AdvertisementRepository;


@Transactional
@Service
public class AdvertisementService {

	@Autowired
	AdvertisementRepository advertisementRepository;
	
	@Autowired
	AgentService agentService;
	
	public AdvertisementService(){
		super();
	}
	
	public Advertisement create(){
		Advertisement result;
		Agent principal;
		
		principal = this.agentService.findByPrincipal();
		
		result = new Advertisement();
		result.setAgent(principal);
		
		return result;
	}
	
}
