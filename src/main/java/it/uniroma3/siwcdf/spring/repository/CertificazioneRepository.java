package it.uniroma3.siwcdf.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwcdf.spring.model.Certificazione;
import it.uniroma3.siwcdf.spring.model.User;

public interface CertificazioneRepository extends CrudRepository<Certificazione, Long> {
	
	public Optional<Certificazione> findById(Long id);

	public List<Certificazione> findAllByAllieviIn(List<User> allievi);
}