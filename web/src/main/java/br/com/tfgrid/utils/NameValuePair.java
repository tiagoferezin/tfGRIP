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

	private String name;
	private Object value;

	public NameValuePair() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param value
	 */
	public NameValuePair(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
