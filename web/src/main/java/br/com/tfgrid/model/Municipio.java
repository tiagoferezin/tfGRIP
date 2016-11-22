/**
 * 
 */
package br.com.tfgrid.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.tfgrid.model.ancestral.AEntity;

/**
 * @author Tiago Ferezin (Tiago, 22 de nov de 2016)
 * Funcionalidade da Classe:
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
	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Calendar getDataCriacao() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setDataCriacao(Calendar dataCriacao) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Calendar getDataDesativacao() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setDataDesativacao(Calendar dataDesativacao) {
		// TODO Auto-generated method stub
		
	}
	
}
