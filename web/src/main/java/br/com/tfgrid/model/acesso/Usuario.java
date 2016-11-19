/**
 * 
 */
package br.com.tfgrid.model.acesso;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.tfgrid.model.ancestral.AEntity;

/**
 * @author Tiago Ferezin (Tiago, 19 de nov de 2016) Funcionalidade da Classe:
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "username", name = "uk_username"),
		@UniqueConstraint(columnNames = "email", name = "uk_email") })
public class Usuario extends AEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long idUsuario;

	@Column(nullable = false)
	private String nomeCompleto;

	private String username;

	@Column(nullable = false)
	private String senha;

	@Column(nullable = false)
	private String salt;
	
	private String email;

	@Column(nullable = false)
	private Calendar dataContratacao;

	@Column(nullable = false)
	private Calendar dataCriacao;

	private Calendar dataDesativacao;

	public Usuario() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Calendar getDataContratacao() {
		return dataContratacao;
	}

	public void setDataContratacao(Calendar dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return this.idUsuario;
	}

	@Override
	public void setId(Long id) {
		// TODO Auto-generated method stub
		this.idUsuario = id;
	}

	@Override
	public Calendar getDataCriacao() {
		// TODO Auto-generated method stub
		return this.dataCriacao;
	}

	@Override
	public void setDataCriacao(Calendar dataCriacao) {
		// TODO Auto-generated method stub
		this.dataCriacao = dataCriacao;
	}

	@Override
	public Calendar getDataDesativacao() {
		// TODO Auto-generated method stub
		return this.dataDesativacao;
	}

	@Override
	public void setDataDesativacao(Calendar dataDesativacao) {
		// TODO Auto-generated method stub
		this.dataDesativacao = dataDesativacao;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nomeCompleto + "(" + this.email + ")";

	}

}
