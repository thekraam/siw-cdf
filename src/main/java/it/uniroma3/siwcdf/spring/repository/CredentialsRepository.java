package it.uniroma3.siwcdf.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siwcdf.spring.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {
	
	public Credentials findByUsername(String username);

	public boolean existsBy(); // restituisce true se esiste almeno un utente? utile per diminuire il peso sulla fetch per il primo admin e tutti i futuri
}