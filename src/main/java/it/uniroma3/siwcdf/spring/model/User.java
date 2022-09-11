package it.uniroma3.siwcdf.spring.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity

@Table(name = "users")
public @Data class User{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=true)
	private String nome;
	
	@Column(nullable=true)
	private String cognome;
	
	@Column(nullable=true)
	private Date dataDiNascita;
	
	@Column(nullable=false, unique=true)
	private String username;
	
	@Column(nullable=false)
	private String role;
	
	@ManyToMany(mappedBy="allievi",cascade= {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}) 
	private List<Certificazione> certificazioni = new ArrayList<>();
	
	//debug visualizzazione password
	//@Column(nullable=true)
	//private String password;

}
