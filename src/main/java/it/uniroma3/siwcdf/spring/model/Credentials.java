package it.uniroma3.siwcdf.spring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;

@Entity
public @Data class Credentials {
	
	public static final String DEFAULT_ROLE = "DEFAULT";
	public static final String ADMIN_ROLE = "ADMIN";
	public static final String NOTAPPROVED_ROLE = "NOTAPPROVED";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;
	
	@Transient 
	private String passwordConfirm; 
	
	@Column(nullable = false)
	private String role;  
	
	@OneToOne(cascade = CascadeType.ALL) 
    @JoinColumn(name = "id")
    @MapsId
	private User user;

	public void setUser(User user) {
    	char[] adjustFirstName = user.getNome().trim().toLowerCase().toCharArray();
    	adjustFirstName[0] = Character.toUpperCase(adjustFirstName[0]);
    	
    	char[] adjustLastName = user.getCognome().trim().toLowerCase().toCharArray();
    	adjustLastName[0] = Character.toUpperCase(adjustLastName[0]);
    	
    	user.setNome(String.valueOf(adjustFirstName));
    	user.setCognome(String.valueOf(adjustLastName));
    	
    	this.user = user;
	}
}
