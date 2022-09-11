package it.uniroma3.siwcdf.spring.controller.validator;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siwcdf.spring.model.User;

/**
 * Validator for User
 */
@Component
public class UserValidator implements Validator {

    final Integer MAX_NAME_LENGTH = 100;
    final Integer MIN_NAME_LENGTH = 2;

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        String nome = user.getNome().trim();
        String cognome = user.getCognome().trim();
        Date dataDiNascita = user.getDataDiNascita();
        // nessun controllo sullo username perche creato da JS in register.html
        if(dataDiNascita!=null) {
	        if(dataDiNascita.after(Date.valueOf("2005-01-01")) || dataDiNascita.before(Date.valueOf("1910-01-01"))) {
	        	errors.rejectValue("dataDiNascita", "invalid");
	        }
        }
        else
        	errors.rejectValue("dataDiNascita", "required");
        
        if (nome.isEmpty())
            errors.rejectValue("nome", "required");
        for(char c : nome.toCharArray()) {
        	if(Character.isDigit(c)) {
        		errors.rejectValue("nome", "size");
        		break;
        	}
        }
        if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
            errors.rejectValue("nome", "size");

        if (cognome.isEmpty())
            errors.rejectValue("cognome", "required");
        for(char c : cognome.toCharArray()) {
        	if(Character.isDigit(c)) {
        		errors.rejectValue("cognome", "size");
        		break;
        	}
        }
        if (cognome.length() < MIN_NAME_LENGTH || cognome.length() > MAX_NAME_LENGTH)
            errors.rejectValue("cognome", "size");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

}

