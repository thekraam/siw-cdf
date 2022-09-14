package it.uniroma3.siwcdf.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static it.uniroma3.siwcdf.spring.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siwcdf.spring.model.Credentials.DEFAULT_ROLE;
import static it.uniroma3.siwcdf.spring.model.Credentials.NOTAPPROVED_ROLE;

import it.uniroma3.siwcdf.spring.model.Credentials;
import it.uniroma3.siwcdf.spring.model.User;
import it.uniroma3.siwcdf.spring.repository.CredentialsRepository;

@Service
public class CredentialsService {
	
    @Autowired
    protected PasswordEncoder passwordEncoder;

	@Autowired
	protected CredentialsRepository credentialsRepository;
	
	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Credentials getCredentialsByUsername(String username) {
		Credentials result = this.credentialsRepository.findByUsername(username);
		return result;
	}
	
	@Transactional
	public List<Credentials> getAll(){
		return (List<Credentials>) this.credentialsRepository.findAll();
	}
		
    @Transactional
    public Credentials saveCredentials(Credentials credentials, String Role) {
    	if(!credentialsRepository.existsBy()) {
    		credentials.setRole(ADMIN_ROLE);
    	}
    	else {
    		credentials.setRole(Role);
    	}
        // se sto approvando un utente gia esistente non criptare la password...
        if(this.getCredentialsByUsername(credentials.getUsername())!=null)
        	return this.credentialsRepository.save(credentials);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }
    
    @Transactional
    public String getRoleAuthenticated() {
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = this.getCredentialsByUsername(userDetails.getUsername());
    	return credentials.getRole();
    }
    
}
