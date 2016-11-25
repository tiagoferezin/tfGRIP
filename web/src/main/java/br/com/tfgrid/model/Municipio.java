/**
 * 
 */
package br.com.tfgrid.model;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.tfgrid.enumarator.EEstado;
import br.com.tfgrid.enumarator.EPais;
import br.com.tfgrid.model.ancestral.AEntity;

/**
 * @author Tiago Ferezin (Tiago, 22 de nov de 2016) Funcionalidade da Classe:
 */
@Entity
public class Municipio extends AEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long idMunicipio;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EEstado estado;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EPais pais;

	@Column(nullable = false)
	private Calendar dataCriacao;

	private Calendar dataDesativacao;

	public Municipio() {
		this.pais = EPais.BRASIL;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EEstado getEstado() {
		return estado;
	}

	public void setEstado(EEstado estado) {
		this.estado = estado;
	}

	public EPais getPais() {
		return pais;
	}

	public void setPais(EPais pais) {
		this.pais = pais;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return idMunicipio;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.idMunicipio = id;

	}

	@Override
	public Calendar getDataDesativacao() {
		// TODO Auto-generated method stub
		return dataDesativacao;
	}

	@Override
	public void setDataDesativacao(Calendar dataDesativacao) {
		// TODO Auto-generated method stub
		this.dataDesativacao = dataDesativacao;
	}

	@Override
	public Calendar getDataCriacao() {
		// TODO Auto-generated method stub
		return dataCriacao;
	}

	@Override
	public void setDataCriacao(Calendar dataCriacao) {
		// TODO Auto-generated method stub
		this.dataCriacao = dataCriacao;
	}

}
