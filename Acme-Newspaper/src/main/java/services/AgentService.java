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

import domain.Advertisement;
import domain.Agent;
import domain.MailMessage;
import forms.ActorForm;
import forms.EditActorForm;

import repositories.AgentRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;


@Transactional
@Service
public class AgentService {
	
	@Autowired
	AgentRepository agentRepository;
	
	@Autowired
	FolderService	folderService;
	
	@Autowired
	Validator 		validator;
	
	public AgentService(){
		super();
	}
	
	public Agent create(){
		Agent result;
		result = new Agent();
		
		
		result.setAdvertisements(new ArrayList<Advertisement>());
		result.setReceivedMessages(new ArrayList<MailMessage>());
		result.setSentMessages(new ArrayList<MailMessage>());
		result.setFolders(this.folderService.createSystemFolders());
		
		
		return result;
	}
	
	public Agent save(final Agent agent){
		Agent saved;
		Assert.notNull(agent);
		
		if(agent.getId() == 0){
			final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
			agent.getUserAccount().setPassword(passwordEncoder.encodePassword(agent.getUserAccount().getPassword(), null));
		}
		
		saved = this.agentRepository.save(agent);
		
		Assert.isTrue(this.agentRepository.findAll().contains(saved));
		
		return saved;
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

	public Agent findByPrincipal() {
		Agent result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.findByUserAccount(userAccount);
		Assert.notNull(result);
	
		return result;
	}

	public Agent findByUserAccount(UserAccount userAccount) {
		Agent result;
		
		result = this.agentRepository.findByUserAccount(userAccount.getId());
		
		return result;
	}

	public Agent findOne(int agentId) {
		return this.agentRepository.findOne(agentId);
	}

	public Agent reconstruct(EditActorForm editActorForm, BindingResult binding) {
		Agent agent;
		agent = this.findByPrincipal();
		
		agent.setName(editActorForm.getName());
		agent.setSurname(editActorForm.getSurname());
		agent.setEmail(editActorForm.getEmail());
		agent.setId(editActorForm.getId());
		agent.setPostalAddress(editActorForm.getAddress());
		agent.setVersion(editActorForm.getVersion());
		agent.setPhone(editActorForm.getPhone());
	
		
		this.validator.validate(editActorForm, binding);

		return agent;
	}

	public EditActorForm construct(Agent agent, EditActorForm editActorForm) {
		editActorForm.setId(agent.getId());
		editActorForm.setVersion(agent.getVersion());
		editActorForm.setName(agent.getName());
		editActorForm.setSurname(agent.getSurname());
		editActorForm.setEmail(agent.getEmail());
		editActorForm.setPhone(agent.getPhone());
		editActorForm.setAddress(agent.getPostalAddress());

		return editActorForm;
	}
	
	
	
	
}
