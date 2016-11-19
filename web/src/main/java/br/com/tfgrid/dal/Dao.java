/**
 * 
 */
package br.com.tfgrid.dal;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import br.com.tfgrid.model.ancestral.AEntity;
import br.com.tfgrid.utils.NameValuePair;

/**
 * @author Tiago Ferezin (Tiago, 10 de jun de 2016) Funcionalidade da Classe:
 * @param <AEntity>
 */
public class Dao {

	protected EntityManager entityManager;
	protected AEntity entity;
	protected Validator validator;
	protected String userName;
	protected DaoFactory daoFactory;

	public Dao(AEntity entity, EntityManager entityManager) {
		// TODO Auto-generated constructor stub
		setEntity(entity);
		setEntityManager(entityManager);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public AEntity getEntity() {
		return entity;
	}

	public void setEntity(AEntity entity) {
		this.entity = entity;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	Class<AEntity> getClassT() throws Exception {
		ParameterizedType superclass = (ParameterizedType) getClass().getGenericSuperclass();

		return (Class<AEntity>) superclass.getActualTypeArguments()[0];
	}

	protected AEntity getInstanceT() throws Exception {

		Class<AEntity> classT = getClassT();

		return classT.newInstance();
	}

	void beforePersistence() throws ConstraintViolationException, Exception {
	}

	void afterPersistence() throws ConstraintViolationException, Exception {
	}

	void validate() throws ConstraintViolationException {
		Set<ConstraintViolation<AEntity>> constraintViolations = getValidator().validate(entity);

		String validationErrors = "";

		for (ConstraintViolation<AEntity> constraintViolation : constraintViolations) {
			if (!validationErrors.isEmpty()) {
				validationErrors += ", ";
			}

			validationErrors += "\"" + constraintViolation.getPropertyPath() + "\" " + constraintViolation.getMessage();
		}

		if (!validationErrors.isEmpty()) {
			throw new ConstraintViolationException(validationErrors, constraintViolations);
		}
	}

	public void create() throws Exception, ConstraintViolationException {
		entity.setDataCriacao(Calendar.getInstance());
		boolean fecharTransacao = false;

		try {

			validate();
			beforePersistence();
			if (!entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().begin();
				fecharTransacao = true;
			}

			entityManager.persist(this.entity);

			if (entityManager.getTransaction().isActive()) {
				afterPersistence();
				if (fecharTransacao) {
					entityManager.getTransaction().commit();
				}
			}
		} catch (ConstraintViolationException e) {
			// TODO: handle exception
			if (entityManager.getTransaction().isActive()) {
				if (fecharTransacao) {
					entityManager.getTransaction().rollback();

				}
			}

			e.printStackTrace();
			throw e;

		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				if (fecharTransacao) {
					entityManager.getTransaction().rollback();
				}
			}
			e.printStackTrace();
			throw e;

		}

	}

	private void initializeEntityManager() throws Exception {
		if ((entityManager == null) || (!entityManager.isOpen())) {
			throw new Exception(
					"A entityManager de acesso a dados da " + entity.getClass() + " está fechado ou é nulo.");
		}
	}

	public List<AEntity> read(AEntity entity, EntityManager entityManager, String where,
			List<NameValuePair> whereParameters, Integer initialRecord, Integer amountRecord,
			List<NameValuePair> orderBy, Boolean registrosAtivos) throws Exception {

		initializeEntityManager();

		List<AEntity> list = new ArrayList<AEntity>();

		if (registrosAtivos == null) {
			registrosAtivos = true;
		}

		boolean closeTransaction = false;
		try {
			if (!entityManager.getTransaction().isActive()) {

				entityManager.getTransaction().begin();
				closeTransaction = true;
			}

			String hQl = "from " + entity.getClass().getSimpleName() + " t ";
			// String hQl = "from T t ";
			System.out.println(hQl);
			if ((where != null) && (!where.trim().isEmpty())) {
				hQl += " where " + where;
				if (registrosAtivos) {
					hQl += " and (dataDesativacao is null)";
				}
				System.out.println(hQl);
			} else {
				if (registrosAtivos) {
					hQl += " where (dataDesativacao is null)";
				}
			}
			System.out.println(hQl);

			String order = "";

			if ((orderBy != null) && (orderBy.size() > 0)) {

				order = "";

				for (NameValuePair orderColumn : orderBy) {

					if ((order != null) && (!order.isEmpty())) {
						order += ", ";
					}

					order += orderColumn.getNome();

					if (orderColumn.getValor() != null) {
						order += " " + orderColumn.getValor();
					}

				}

				order = " order by " + order;

			}

			hQl += order;
			System.out.println(hQl);

			Query query = entityManager.createQuery(hQl);

			if ((where != null) && (!where.trim().isEmpty())) {
				if (whereParameters != null) {
					for (NameValuePair par : whereParameters) {

						query.setParameter(par.getNome(), par.getValor());
					}
				}
			}

			if (initialRecord > 0) {
				query.setFirstResult(initialRecord);
			}

			if (amountRecord > 0) {
				query.setMaxResults(amountRecord);
			}

			list = query.getResultList();

			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {

					entityManager.getTransaction().commit();
				}
			}

		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {

					entityManager.getTransaction().rollback();
				}
			}

			throw e;

		} finally {
			// if (closeTransaction) {
			// logger.debug("\n*** Closing EM\n");
			// entityManager.close();
			// }
		}
		return list;

	}

	public void update() throws Exception, ConstraintViolationException {
		initializeEntityManager();

		boolean closeTransaction = false;

		try {
			validate();
			// T encontrada = (T) manager.find(entity.getClass(),
			// entity.getId());

			beforePersistence();
			if (!entityManager.getTransaction().isActive()) {

				entityManager.getTransaction().begin();
				closeTransaction = true;

			}

			entityManager.merge(this.entity);
			if (entityManager.getTransaction().isActive()) {
				afterPersistence();
				if (closeTransaction) {

					entityManager.getTransaction().commit();
				}
			}
		} catch (ConstraintViolationException e) {
			if (entityManager.getTransaction().isActive()) {

				if (closeTransaction) {

					entityManager.getTransaction().rollback();
				}

			}

			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();

			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {

					entityManager.getTransaction().rollback();
				}

			}
			throw e;
		} finally {
			// if (closeTransaction) {
			// logger.debug("\n*** Closing EM\n");
			// if ((entityManager != null) && (entityManager.isOpen())) {
			// entityManager.close();
			// }
			// }
		}
	}

	public void delete() throws Exception {

		initializeEntityManager();

		boolean closeTransaction = false;
		try {

			beforePersistence();
			// T encontrada = (T) manager.find(entity.getClass(),
			// entity.getId());
			if (!entityManager.getTransaction().isActive()) {

				entityManager.getTransaction().begin();
				closeTransaction = true;

			}
			if (entity instanceof AEntity) {
				entity.setDataDesativacao(Calendar.getInstance());
				entityManager.merge(this.entity);
			} else {
				entityManager.remove(entityManager.getReference(entity.getClass(), entity.getId()));
			}

			if (entityManager.getTransaction().isActive()) {
				afterPersistence();
				if (closeTransaction) {

					entityManager.getTransaction().commit();
				}

				entity.setId(0L);
			}
		} catch (Exception e) {

			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {

					entityManager.getTransaction().rollback();
				}
			}
			throw e;

		} finally {
			// if (closeTransaction) {
			// logger.debug("\n*** Closing EM\n");
			// entityManager.close();
			// }
		}

	}

}