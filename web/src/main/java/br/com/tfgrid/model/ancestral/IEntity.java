package br.com.tfgrid.model.ancestral;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author Tiago Ferezin (Tiago, 19 de nov de 2016) Funcionalidade da Classe:
 */
public interface IEntity extends Serializable {

	public Long getId();

	public void setId(Long id);

	public Calendar getDataCriacao();

	public void setDataCriacao(Calendar dataCriacao);

	public Calendar getDataDesativacao();

	public void setDataDesativacao(Calendar dataDesativacao);

	public boolean isEmptyId();

	public boolean isDeleted();
}
