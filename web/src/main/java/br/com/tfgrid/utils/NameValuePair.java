/**
 * 
 */
package br.com.tfgrid.utils;

import java.io.Serializable;

/**
 * @author Tiago Ferezin (Tiago, 10 de jun de 2016) Funcionalidade da Classe:
 */
public class NameValuePair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nome;
	private Object valor;

	public NameValuePair() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param nome
	 * @param valor
	 */
	public NameValuePair(String nome, Object valor) {
		super();
		this.nome = nome;
		this.valor = valor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Object getValor() {
		return valor;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

}
