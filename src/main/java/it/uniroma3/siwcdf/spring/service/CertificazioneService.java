package it.uniroma3.siwcdf.spring.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import it.uniroma3.siwcdf.spring.model.Certificazione;
import it.uniroma3.siwcdf.spring.model.Credentials;
import it.uniroma3.siwcdf.spring.model.User;
import it.uniroma3.siwcdf.spring.repository.CertificazioneRepository;

@Service
public class CertificazioneService {
	
	@Autowired
	protected CertificazioneRepository certificazioneRepository;
	
	@Autowired
	protected UserService userService;

	@Transactional
	public Certificazione getById(Long id) {
		return this.certificazioneRepository.findById(id).get();
	}
	
    @Transactional
    public List<Certificazione> getAll() {
        return (List<Certificazione>) certificazioneRepository.findAll();
    }
    
    @Transactional
    public List<Certificazione> getActiveOrInactive(boolean isActive) {
    	List<Certificazione> Certificazioni = (List<Certificazione>) certificazioneRepository.findAll();
    	
    	if(isActive)
    		Certificazioni.removeIf(c -> (c.getDataScadenzaPrenotazione().before(Date.valueOf(LocalDate.now()))));
    	else
    		Certificazioni.removeIf(c -> (!c.getDataScadenzaPrenotazione().before(Date.valueOf(LocalDate.now()))));
    	
        return Certificazioni;
    }
    
	@Transactional
	public Certificazione add(Certificazione certificazione) {
		LocalDate dataCertificazioneCorrente = certificazione.getData().toLocalDate();
		// la scadenza e sempre un giorno prima, il calcolo e' costante
		Date dataScadenzaPrenotazione = Date.valueOf(dataCertificazioneCorrente.minusDays(1));
		
		certificazione.setDataScadenzaPrenotazione(dataScadenzaPrenotazione);
		
		return certificazioneRepository.save(certificazione);
	}
	
	@Transactional
	public List<Certificazione> getCertificazioniDisponibiliPerAllievo(){
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User allievo = userService.getUserByUsername(userDetails.getUsername());
		List<Certificazione> certificazioniList = this.getAll();
		
		// le certificazioni prenotate non sono da considerarsi, le tolgo dalla lista
		//if(!certificazioniList.isEmpty()) {
		//	List<Certificazione> certificazioniAllievo = this.getCertificazioniAllievo(userService.getUserByUsername(userDetails.getUsername()));
		//	if(certificazioniAllievo != null)
		//		certificazioniList.removeAll(certificazioniAllievo);
		//}
		// se funziona e' meno pesante sul db visto che la seconda fetch sul findbyallieviin non viene fatta
		if(!certificazioniList.isEmpty()) {
			certificazioniList.removeIf(c -> (c.getAllievi().contains(allievo)));
			// togli le prenotazioni scadute, ovvero se siamo oltre il giorno prima la prenotazione
			certificazioniList.removeIf(c -> (
					c.getDataScadenzaPrenotazione().before(Date.valueOf(LocalDate.now()))
					));
		}
		
		return certificazioniList;
	}
	
	@Transactional
	public void remove(Certificazione certificazione) {
		certificazioneRepository.delete(certificazione);
	}
	
	// getter tipicamente per visualizzazione da parte di un admin
	@Transactional
	public List<Certificazione> getCertificazioniAllievo(User allievo){
		List<User> allievi = new ArrayList<>();
		allievi.add(allievo);
		List<List<Certificazione>> certificazioniEallievi = certificazioneRepository.findByAllieviIn(allievi);
		
		if(certificazioniEallievi.isEmpty()) return null;
		return certificazioniEallievi.get(0);
	}
	
	// getter utente individuale
	@Transactional
	public List<Certificazione> getCertificazioniAllievo(){
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User allievo = userService.getUserByUsername(userDetails.getUsername());
		
		List<Certificazione> certificazioniPren = this.getCertificazioniAllievo(allievo);
		
		if(certificazioniPren != null) return certificazioniPren;
		return null;
	}
}
