package services;

import java.util.ArrayList;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import services.UserService;
import repositories.VolumeRepository;
import domain.Newspaper;
import domain.User;
import domain.Volume;


@Service
@Transactional
public class VolumeService {

	//Managed Repository ----
	@Autowired
	private VolumeRepository	volumeRepository;
	//Services
	@Autowired
	private UserService userService;

	

	//Constructors
	public VolumeService() {
		super();
	}

	public Volume create() {
		Volume result;
		User principal = this.userService.findByPrincipal();
		Assert.notNull(principal);
		result = new Volume();
		result.setUser(principal);
		result.setNewspapers(new ArrayList<Newspaper>());
		return result;
	}

	public Collection<Volume> findAll() {
		Collection<Volume> result;

		result = this.volumeRepository.findAll();

		return result;
	}
	

	public Volume save(final Volume volume) {
		Volume result;
		User principal = userService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(volume.getUser().equals(principal));
		
		result = this.volumeRepository.save(volume);
		
		Collection<Volume> creatorsVolumes = principal.getVolumes();
		creatorsVolumes.add(result);
		principal.setVolumes(creatorsVolumes);
		
	
		return result;
	}

	public Volume findOne(final int volumeId) {
		Volume result;

		result = this.volumeRepository.findOne(volumeId);
		Assert.notNull(result);

		return result;
	}
	

	public void flush() {
		this.volumeRepository.flush();
	}


}