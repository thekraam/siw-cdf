package it.uniroma3.siwcdf.spring.controller.validator;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siwcdf.spring.model.Certificazione;
import it.uniroma3.siwcdf.spring.model.Credentials;
import it.uniroma3.siwcdf.spring.model.User;
import it.uniroma3.siwcdf.spring.service.CertificazioneService;
import it.uniroma3.siwcdf.spring.service.CredentialsService;


@Component
public class CertificazioneValidator implements Validator{
	
	private final int TEMPO_PER_PRENOTARE_MINIMO = 20;//gg

    @Override
    public void validate(Object o, Errors errors) {
        Certificazione certificazione = (Certificazione) o;
        String nome = certificazione.getNome().trim();
        String luogo = certificazione.getLuogo().trim();
        String descrizione = certificazione.getDescrizione();
        Date data = certificazione.getData();
        
        // diamo agli studenti almeno 20 giorni per prenotarsi, percio la data di una certificazione deve distare almeno 20 giorni da adesso
        Date minimoDistanzaData = Date.valueOf(LocalDate.now().plusDays(TEMPO_PER_PRENOTARE_MINIMO));
        
        if(descrizione.isEmpty())
        	errors.rejectValue("descrizione", "required");
        
        if(data!=null) {
	        try {
	        	Date.valueOf(data.toString());
	        } catch (IllegalArgumentException exception) { 
	        	errors.rejectValue("data", "nocharacters"); 
	        }
	        if(data.before(minimoDistanzaData))
	        	errors.rejectValue("data", "invalid");
        }
        else
        	errors.rejectValue("data", "nocharacters");
        
        if(nome.isEmpty())
            errors.rejectValue("nome", "required");
        
        if(luogo.isEmpty())
        	errors.rejectValue("luogo", "required");
    }

	@Override
	public boolean supports(Class<?> clazz) {
		return Certificazione.class.equals(clazz);
	}

}