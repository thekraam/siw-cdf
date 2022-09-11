package it.uniroma3.siwcdf.spring.model;


import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import lombok.Data;

@Entity
public @Data class Certificazione {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private String nome;
	
	@Column(nullable=false)
	private String luogo;
	
	@Column(nullable=false)
	private Date data;
	
	@Column(nullable=false)
	private String descrizione;
	
	@Column(nullable=false)
	private Date dataScadenzaPrenotazione;
	
	@ManyToMany(cascade= {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	private List<User> allievi = new ArrayList<>();
	
	public void updateInfoCertificazione(Certificazione certificazione) {
		this.nome = certificazione.getNome();
		this.luogo = certificazione.getLuogo();
		this.data = certificazione.getData();
		this.descrizione = certificazione.getDescrizione();
	}
}
