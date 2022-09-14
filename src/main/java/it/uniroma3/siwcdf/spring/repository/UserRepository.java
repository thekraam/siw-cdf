package it.uniroma3.siwcdf.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwcdf.spring.model.Certificazione;
import it.uniroma3.siwcdf.spring.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	public List<User> findByNome(String nome);

	public List<User> findByNomeAndCognome(String nome, String cognome);

	public List<User> findByNomeOrCognome(String nome, String cognome);
	
	public Optional<User>  findTopByOrderByIdDesc();
	
	//public List<List<User>> findByCertificazioniIn(List<Certificazione> certificazioni);
	public List<User> findAllByCertificazioniIn(List<Certificazione> certificazioni);
}