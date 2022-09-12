package it.uniroma3.siwcdf.spring.controller;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import it.uniroma3.siwcdf.spring.controller.validator.CredentialsValidator;
import it.uniroma3.siwcdf.spring.controller.validator.UserValidator;
import it.uniroma3.siwcdf.spring.model.Credentials;
import it.uniroma3.siwcdf.spring.model.User;
import it.uniroma3.siwcdf.spring.repository.UserRepository;
import it.uniroma3.siwcdf.spring.service.CredentialsService;
import it.uniroma3.siwcdf.spring.service.UserService;

import static it.uniroma3.siwcdf.spring.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siwcdf.spring.model.Credentials.DEFAULT_ROLE;
import static it.uniroma3.siwcdf.spring.model.Credentials.NOTAPPROVED_ROLE;

@Controller
@SessionAttributes(value="role", types= {String.class})
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@RequestMapping("role")
	public String roleUser() {
		String role=credentialsService.getRoleAuthenticated();
    	return role;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET) 
	public String showLoginForm (Model model) {
		model.addAttribute("errorMessage", null);
		return "login";
	}
	
	@GetMapping("/login-error") 
    public String loginError(HttpServletRequest request, Model model) {
		
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
                if(errorMessage == "Bad credentials") errorMessage="Credenziali invalide";
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET) 
	public String logout(Model model) {
		return "index";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET) 
	public String showRegisterForm (Model model) {
		
		model.addAttribute("lastUserId", userService.getLastUserId());
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		
		return "register";
	}
	
    @RequestMapping(value = { "/register" }, method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user,
                 BindingResult userBindingResult,
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult,
                 Model model) {
        // valida lo user e le credenziali
        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);

        // se non ci sono errori salva tutto e porta l'utente alla pagina di approvazione
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
        	
            credentials.setUser(user);
            
            // per controllare se e il primo utente mi evito una fetch per la size salvando le credenziali utente
            Credentials credenzialiUtente = credentialsService.saveCredentials(credentials, Credentials.NOTAPPROVED_ROLE);
            
            //se il primo utente e un admin, allora nessuna approvazione richiesta
            if(credenzialiUtente.getId()>1) {
            	model.addAttribute("allievo", credentials.getUser());
            	return "needsapproval";
            }
            return "login";
        }
        // altrimenti stampa gli errori in console...
        //if(userBindingResult.hasErrors()) {
        //	System.out.println("######## Errore ######## [ "+ userBindingResult.getAllErrors() + " ]");
        //}
        return "register";
    }
    
    @RequestMapping(value="/admin/managestudents/approve/{id}",method= RequestMethod.GET)
    public String approveUser(@PathVariable("id")Long id, Model model) {
    	Credentials currentCredentials = this.credentialsService.getCredentialsByUsername(userService.getUser(id).getUsername());
    	credentialsService.saveCredentials(currentCredentials, Credentials.DEFAULT_ROLE);
        return "admin/approvalsuccessful";
    }
    
    @RequestMapping(value = "/admin/new", method = RequestMethod.GET)
    public String addAdmin(Model model) {
    	model.addAttribute("user", new User());
    	model.addAttribute("credentials", new Credentials());
    	return "admin/new";
    }
    
    @RequestMapping(value = "/admin/new", method = RequestMethod.POST)
    public String addAdminForm(@ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult, @ModelAttribute("user") User admin, BindingResult adminBindingResult, Model model) {
    	userValidator.validate(admin, adminBindingResult);
    	credentialsValidator.validate(credentials, credentialsBindingResult);
    	
    	if(!adminBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
    		credentials.setUser(admin);
    		
    		credentialsService.saveCredentials(credentials, ADMIN_ROLE);
    		model.addAttribute("credentials", credentials);
    		model.addAttribute("admin", admin);
    		
    		return "admin/admincreated";
    	}
    	
    	return "admin/new";
    }
	
}
