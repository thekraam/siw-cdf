package it.uniroma3.siwcdf.spring.controller;

import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siwcdf.spring.controller.validator.CertificazioneValidator;
import it.uniroma3.siwcdf.spring.model.Certificazione;
import it.uniroma3.siwcdf.spring.model.Credentials;
import it.uniroma3.siwcdf.spring.model.User;
import it.uniroma3.siwcdf.spring.repository.CertificazioneRepository;
import it.uniroma3.siwcdf.spring.service.CertificazioneService;
import it.uniroma3.siwcdf.spring.service.CredentialsService;
import it.uniroma3.siwcdf.spring.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CertificazioneController {

	@Autowired
	private CertificazioneService certificazioneService;
	
	@Autowired
	private CertificazioneValidator certificazioneValidator;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	
	//USER
	@RequestMapping(value = {"/certifications"}, method = RequestMethod.GET)
	public String showCertificazioni(Model model) {
		
		List<Certificazione> certificazioniDisp = certificazioneService.getCertificazioniDisponibiliPerAllievo();
		
		model.addAttribute("certificazioniDisp", certificazioniDisp);
		model.addAttribute("role", credentialsService.getRoleAuthenticated());
		
		return "certifications";
	}
	
	@RequestMapping(value = {"/certifications/details/{id}"}, method = RequestMethod.GET)
	public String showCertificazioni(@PathVariable("id")Long id, Model model) {
		
		Certificazione certificazione = certificazioneService.getById(id);
		
		model.addAttribute("certificazione", certificazione);
		model.addAttribute("role", credentialsService.getRoleAuthenticated());
		
		return "certificationdetails";
	}
	
	@RequestMapping(value = {"/reserved"}, method = RequestMethod.GET)
	public String showCertificazioniPrenotate(Model model) {
		List<Certificazione> certificazioniPren = certificazioneService.getCertificazioniAllievo();
		
		model.addAttribute("certificazioniPren", certificazioniPren);
		
		return "reserved";
	}
	
	@RequestMapping(value = {"/certifications/enroll/{id}"}, method = RequestMethod.GET)
	public String enrollForCertificazione(@PathVariable("id")Long id, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		User allievo = userService.getUserByUsername(userDetails.getUsername());
		Certificazione certificazioneCorrente = certificazioneService.getById(id);
		
		allievo.getCertificazioni().add(certificazioneCorrente);
		certificazioneCorrente.getAllievi().add(allievo);
		
		userService.add(allievo);
		
		return "redirect:/reserved";
	}
	
	@RequestMapping(value = {"/certifications/unenroll/{id}"}, method = RequestMethod.GET)
	public String unenrollForCertificazione(@PathVariable("id")Long id, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		User allievo = userService.getUserByUsername(userDetails.getUsername());
		Certificazione certificazioneCorrente = certificazioneService.getById(id);
		
		allievo.getCertificazioni().remove(certificazioneCorrente);
		certificazioneCorrente.getAllievi().remove(allievo);
		
		userService.add(allievo);
		
		return "unenrolled";
	}
	
	//ADMIN
	@RequestMapping(value = {"/admin/managecertifications"}, method = RequestMethod.GET)
	public String manageCertificazione(Model model) {
		
		model.addAttribute("certificazioniAttive", certificazioneService.getActiveOrInactive(true));
		model.addAttribute("certificazioniScadute", certificazioneService.getActiveOrInactive(false));
		return "admin/managecertifications";
	}
	
	
	@RequestMapping(value = {"/admin/managestudents/certifications/{id}"}, method = RequestMethod.GET)
	public String showCertificazioniAllievo(@PathVariable("id") Long id, Model model) {
		
		Credentials credenzialiUser = credentialsService.getCredentials(id);
		
		
		List<Certificazione> certificazioniAllievo = credenzialiUser.getUser().getCertificazioni();
		
		model.addAttribute("certificazioniDisp", certificazioniAllievo);
		model.addAttribute("credentials", credenzialiUser);
		model.addAttribute("role", credentialsService.getRoleAuthenticated());
		
		return "admin/studentcertifications";
	}
	
	@RequestMapping(value = {"/admin/addcertification"}, method = RequestMethod.GET)
	public String addCertificazione(Model model) {
		model.addAttribute("certificazione", new Certificazione());
		return "admin/addcertification";
	}
	
	@RequestMapping(value = {"/admin/managecertifications/students/{id}"}, method = RequestMethod.GET)
	public String showPrenotati(@PathVariable("id") Long id, Model model) {
		
		Certificazione certificazione = certificazioneService.getById(id);
		List<User> allieviCertificazione = userService.getAllieviCertificazione(certificazione);
		
		List<Credentials> credenzialiAllieviCertificazioni = new ArrayList<>();
		for(User a : allieviCertificazione) {
			credenzialiAllieviCertificazioni.add(credentialsService.getCredentials(a.getId()));
		}
		
		model.addAttribute("certificazione", certificazione);
		model.addAttribute("credenzialiListaPrenotati", credenzialiAllieviCertificazioni);
		model.addAttribute("role", credentialsService.getRoleAuthenticated());
		
		return "admin/certificationstudents";
	}
	
	@RequestMapping(value = {"/admin/addcertification"}, method = RequestMethod.POST)
	public String addCertificazioneForm(@ModelAttribute("certificazione") Certificazione certificazione, BindingResult bindingResult, Model model) {
		this.certificazioneValidator.validate(certificazione, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.certificazioneService.add(certificazione);
			return "admin/editsuccessful";
		}
		return "admin/addcertification";
	}
	
	 
	@RequestMapping(value = {"/admin/editcertification/{id}"}, method = RequestMethod.POST)
	public String modificaCertificazione(@PathVariable("id")Long id, @ModelAttribute("certificazioneNuova") Certificazione certificazioneNuova, BindingResult bindingResult, final RedirectAttributes redirectAttributes, Model model) {
		
		this.certificazioneValidator.validate(certificazioneNuova, bindingResult);


		if(!bindingResult.hasErrors()) {
			Certificazione certificazioneDaAggiornare = certificazioneService.getById(id);
			
			certificazioneDaAggiornare.updateInfoCertificazione(certificazioneNuova);
			
			certificazioneService.add(certificazioneDaAggiornare);
			
			return "admin/editsuccessful";
		}
		
		redirectAttributes.addFlashAttribute("bindingResult", bindingResult);
		model.addAttribute("certificazione", certificazioneService.getById(id));
		
		return "redirect:/admin/managecertifications/edit/" + id;
	}
	
	@RequestMapping(value = {"/admin/managecertifications/edit/{id}"}, method = RequestMethod.GET)
	public String startModificaCertificazione(@PathVariable("id")Long id, Model model) {	
		
	    if (model.asMap().containsKey("bindingResult"))
	    {
	        model.addAttribute("org.springframework.validation.BindingResult.certificazioneNuova",
	        					model.asMap().get("bindingResult"));
	    }
		
		model.addAttribute("certificazioneNuova", new Certificazione());
		model.addAttribute("certificazione", certificazioneService.getById(id));
		return "admin/editcertification";
	}
	
	@RequestMapping(value = {"/admin/managecertifications/delete/{id}"}, method = RequestMethod.GET)
	public String eliminaCertificazione(@PathVariable("id")Long id, Model model) {
		Certificazione certificazioneDaEliminare = certificazioneService.getById(id);
		certificazioneService.remove(certificazioneDaEliminare);
		
		return "admin/editsuccessful";
	}
	
}
