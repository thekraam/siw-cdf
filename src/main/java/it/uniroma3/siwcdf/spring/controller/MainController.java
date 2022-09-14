package it.uniroma3.siwcdf.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siwcdf.spring.model.Credentials;
import it.uniroma3.siwcdf.spring.service.CredentialsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//import it.uniroma3.siwcdf.spring.service.CredentialsService;


@Controller
@ComponentScan(basePackages = "it.uniroma3.siwcdf.spring.controller")
public class MainController {

	@Autowired
	private CredentialsService credentialsService;
	
	
	
	
	//@RequestMapping(value = {"error"}, method = RequestMethod.GET)
	//public String errorPage(Model model) {
	//	model.addAttribute("role", credentialsService.getRoleAuthenticated());
	//	return "error";
	//}
	
	@RequestMapping(value = {"/", "index"}, method = RequestMethod.GET)
	public String index(Model model) {
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userDetails.getUsername() != null) {
			Credentials credenzialiCorrenti = credentialsService.getCredentialsByUsername(userDetails.getUsername());
			model.addAttribute("role", credenzialiCorrenti.getRole());
			model.addAttribute("user", credenzialiCorrenti.getUser());
		}
		return "index";
	}
	
	//@RequestMapping(value = {"/chisiamo"}, method = RequestMethod.GET)
	//public String chiSiamo(Model model) {
	//	model.addAttribute("role", credentialsService.getRoleAuthenticated());
	//	return "chisiamo";
	//}
	
	//@RequestMapping(value = {"/contattaci"}, method = RequestMethod.GET)
	//public String contattaci(Model model) {
	//	model.addAttribute("role", credentialsService.getRoleAuthenticated());	
	//	return "contattaci";
	//}
}