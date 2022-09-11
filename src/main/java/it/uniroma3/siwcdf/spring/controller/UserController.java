package it.uniroma3.siwcdf.spring.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siwcdf.spring.controller.validator.CredentialsValidator;
import it.uniroma3.siwcdf.spring.controller.validator.UserValidator;
import it.uniroma3.siwcdf.spring.model.Credentials;
import it.uniroma3.siwcdf.spring.model.User;
import it.uniroma3.siwcdf.spring.service.CredentialsService;
import it.uniroma3.siwcdf.spring.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private CredentialsService credentialsService;
    
    @Autowired
    private CredentialsValidator credentialsValidator;
    
    
    @RequestMapping(value = "/admin/managestudents", method = RequestMethod.GET)
    public String showAllievi(Model model) {
    	model.addAttribute("allievi", this.userService.getAllUsers());
    	return "admin/managestudents";
    }

    @RequestMapping(value = "/admin/managestudents/edit/{id}", method = RequestMethod.GET)
    public String editAllievo(@PathVariable("id") Long id, Model model) {
    	User allievoCorrente=this.userService.getUser(id);
    	
    	if(allievoCorrente.getId()==1) return "TESTerror";
    	
    	model.addAttribute("allievo", allievoCorrente);
    	model.addAttribute("credentials", credentialsService.getCredentialsByUsername(allievoCorrente.getUsername()));
    	
    	//System.out.println(allievoCorrente.getDataDiNascita().toLocaleString());
    	return "admin/editstudent";
    }
    
    @RequestMapping(value = "/admin/managestudents/edit/{id}", method = RequestMethod.POST)
    public String editAllievoConfirm(@ModelAttribute("allievo") User allievo, BindingResult userBindingResult, @ModelAttribute("allievo") Credentials credentials, Model model) {
    	
    	User allievoSalvato = credentials.getUser();
    	// forzo i dati mancanti per far passare il check
    	allievo.setCertificazioni(allievoSalvato.getCertificazioni());
    	allievo.setDataDiNascita(allievoSalvato.getDataDiNascita());
    	allievo.setRole(allievoSalvato.getRole());
    	
    	this.userValidator.validate(allievo, userBindingResult);
    	
    	//System.out.println("### ERRORE: " + userBindingResult.getAllErrors());
    	//System.out.println("### WARN: " + credentials.getUser());
    	//System.out.println("### WARN: " + allievo);
    	
    	
    	
    	if(!userBindingResult.hasErrors()) {
    		User allievoDaSalvare = userService.getUser(credentials.getId());
    		allievoDaSalvare.setNome(allievo.getNome());
    		allievoDaSalvare.setCognome(allievo.getCognome());
    		allievoDaSalvare.setUsername(allievo.getUsername());
    		
    		Credentials credenziali = credentialsService.getCredentials(credentials.getId());
    		credentialsService.saveCredentials(credenziali, credenziali.getRole());
    		
    		
    		return "admin/editsuccessful";
    	}
    	
    	return "admin/editstudent";
    }
}
