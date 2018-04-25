package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Adversiment;
import domain.Agent;
import domain.Folder;
import domain.Message;
import forms.ActorForm;

import repositories.AgentRepository;
import security.Authority;


@Transactional
@Service
public class AgentService {
	
	@Autowired
	AgentRepository agentRepository;
	
	@Autowired
	Validator 		validator;
	
	public AgentService(){
		super();
	}
	
	public Agent create(){
		Agent result;
		result = new Agent();
		Collection<Adversiment> adverts = new ArrayList<Adversiment>();
		Collection<Message> sentMessages = new ArrayList<Message>();
		Collection<Message> receivedMessages = new ArrayList<Message>();
		Collection<Folder> folders = new ArrayList<Folder>();
		
		result.setAdversiments(adverts);
		result.setReceivedMessages(receivedMessages);
		result.setSentMessages(sentMessages);
		result.setFolders(folders);
		
		
		return result;
	}
	
	public void save(final Agent agent){
		Assert.notNull(agent);
		
		if(agent.getId() == 0){
			final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			agent.getUserAccount().setPassword(passwordEncoder.encodePassword(agent.getUserAccount().getPassword(), null));
		}
		
		this.agentRepository.save(agent);
	}

	public Agent reconstruct(ActorForm actorForm, BindingResult binding){
		Agent agent = this.create();
		
		agent.setName(actorForm.getName());
		agent.setSurname(actorForm.getSurname());
		agent.setEmail(actorForm.getEmail());
		agent.setPostalAddress(actorForm.getAddress());
		agent.setPhone(actorForm.getPhone());
		agent.setUserAccount(actorForm.getUserAccount());
		
		
		Collection<Authority> authorities = new ArrayList<Authority>();
		Authority auth = new Authority();
		
		auth.setAuthority("AGENT");
		authorities.add(auth);
		agent.getUserAccount().setAuthorities(authorities);
		
		this.validator.validate(actorForm, binding);
		if(!(actorForm.getConfirmPassword().equals(actorForm.getUserAccount().getPassword())) || actorForm.getConfirmPassword() == null)
			binding.rejectValue("confirmPassword", "agent.passwordMiss");
		if((actorForm.getCheck() == false) || actorForm.getCheck() == null)
			binding.rejectValue("check", "agent.uncheck");
		
		return agent;
	}
	
	
}
