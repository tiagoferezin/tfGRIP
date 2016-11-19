/**
 * 
 */
package br.com.tfgrid.model.ancestral;

/**
 * @author Tiago Ferezin (Tiago, 19 de nov de 2016) Funcionalidade da Classe:
 */
public abstract class AEntity implements IEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isEmptyId() {
		return ((getId() == null) || (getId() == 0L));
	}

	@Override
	public boolean isDeleted() {
		return (getDataDesativacao() != null);
	}

}
