package it.uniroma3.siwcdf.spring.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siwcdf.spring.model.Credentials;
import it.uniroma3.siwcdf.spring.model.User;
import it.uniroma3.siwcdf.spring.service.CredentialsService;

/**
 * Validator for Credentials
 */
@Component
public class CredentialsValidator implements Validator {

    @Autowired
    private CredentialsService credentialsService;

    final Integer MAX_PASSWORD_LENGTH = 20;
    final Integer MIN_PASSWORD_LENGTH = 6;

    @Override
    public void validate(Object o, Errors errors) {
        Credentials credentials = (Credentials) o;
        String username = credentials.getUsername().trim();
        String password = credentials.getPassword().trim();
        String passwordConfirm = credentials.getPasswordConfirm().trim();


        if (password.isEmpty())
            errors.rejectValue("password", "required");
        else if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH)
            errors.rejectValue("password", "size");
        
        if(!password.contentEquals(passwordConfirm))
        	errors.rejectValue("password", "matching");
        
        if(username.isEmpty()) {
        	errors.rejectValue("username", "required");
        }
        else if (this.credentialsService.getCredentialsByUsername(username) != null)
            errors.rejectValue("username", "duplicate");
        
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

}