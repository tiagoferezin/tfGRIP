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
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import br.com.tfgrid.model.ancestral.AEntity;
import br.com.tfgrid.utils.NameValuePair;

/**
 * @author Tiago Ferezin (Tiago, 10 de jun de 2016) Funcionalidade da Classe:
 * @param <AEntity>
 */
public class Dao {

	final static Logger logger = Logger.getLogger(Dao.class);

	protected EntityManager entityManager;
	protected AEntity entity;
	protected Validator validator;
	protected String userName;
	protected DaoFactory daoFactory;

	Dao(AEntity entity, String userName, EntityManager entityManager) {
		daoFactory = new DaoFactory();

		setEntity(entity);
		setEntityManager(entityManager);

		if ((userName == null) || (userName.isEmpty())) {
			this.userName = "usuario@anonimo.com";
		} else {
			this.userName = userName;
		}
	}

	AEntity getEntity() {
		return entity;
	}

	void setEntity(AEntity object) {
		this.entity = object;
	}

	EntityManager getEntityManager() {
		return entityManager;
	}

	void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	String getUserName() {
		return userName;
	}

	void setUserName(String userName) {
		this.userName = userName;
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

	Validator getValidator() {
		if (this.validator == null) {
			ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
			validator = validatorFactory.getValidator();
		}

		return validator;
	}

	void create() throws Exception, ConstraintViolationException {

		entity.setDataCriacao(Calendar.getInstance());

		boolean closeTransaction = false;
		try {

			validate();

			beforePersistence();
			if (!entityManager.getTransaction().isActive()) {
				logger.debug("\n*** Starting transaction \n");
				entityManager.getTransaction().begin();
				closeTransaction = true;
			}

			entityManager.persist(this.entity);

			if (entityManager.getTransaction().isActive()) {
				afterPersistence();
				if (closeTransaction) {
					logger.debug("\n*** Commiting transaction \n");
					entityManager.getTransaction().commit();
				}
			}
		} catch (ConstraintViolationException e) {
			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {
					logger.debug("\n*** Rollback transaction\n");
					entityManager.getTransaction().rollback();
				}
			}

			e.printStackTrace();

			throw e;

		} catch (Exception e) {

			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {
					logger.debug("\n*** Rollback transaction\n");
					entityManager.getTransaction().rollback();
				}
			}

			e.printStackTrace();

			throw e;

		} finally {
			// if (closeTransaction) {
			// logger.debug("\n*** Closing EM\n");
			// entityManager.close();
			// }
		}
	}

	void update() throws Exception, ConstraintViolationException {
		initializeEntityManager();

		boolean closeTransaction = false;

		try {
			validate();
			// T encontrada = (T) manager.find(entity.getClass(),
			// entity.getId());

			beforePersistence();
			if (!entityManager.getTransaction().isActive()) {
				logger.debug("\n*** Starting transaction \n");
				entityManager.getTransaction().begin();
				closeTransaction = true;

			}

			entityManager.merge(this.entity);
			if (entityManager.getTransaction().isActive()) {
				afterPersistence();
				if (closeTransaction) {
					logger.debug("\n*** Commiting transaction \n");
					entityManager.getTransaction().commit();
				}
			}
		} catch (ConstraintViolationException e) {
			if (entityManager.getTransaction().isActive()) {

				if (closeTransaction) {
					logger.debug("\n*** Rollback transaction\n");
					entityManager.getTransaction().rollback();
				}

			}

			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();

			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {
					logger.debug("\n*** Rollback transaction\n");
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

	private void initializeEntityManager() throws Exception {
		if ((entityManager == null) || (!entityManager.isOpen())) {
			throw new Exception(
					"O entityManager do acesso a dados da " + entity.getClass() + " está fechado ou é nulo.");
		}
	}

	void delete() throws Exception {

		initializeEntityManager();

		boolean closeTransaction = false;
		try {

			beforePersistence();
			// T encontrada = (T) manager.find(entity.getClass(),
			// entity.getId());
			if (!entityManager.getTransaction().isActive()) {
				logger.debug("\n*** Starting transaction \n");
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
					logger.debug("\n*** Commiting transaction \n");
					entityManager.getTransaction().commit();
				}

				entity.setId(0L);
			}
		} catch (Exception e) {

			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {
					logger.debug("\n*** Rollback transaction\n");
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

	Long count(AEntity entity, String userName, EntityManager entityManager) throws Exception {
		String hQl = "select count(*) from " + entity.getClass().getSimpleName() + " t";

		return readAtomic(entity, userName, entityManager, hQl);
	}

	Long readAtomic(AEntity entity, String userName, EntityManager entityManager, String hQl) throws Exception {
		Long result = 0L;

		initializeEntityManager();

		Query query = entityManager.createQuery(hQl);

		result = (Long) query.getSingleResult();

		return result;

	}

	List<AEntity> read(AEntity entity, String userName, EntityManager entityManager, String where,
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
				logger.debug("\n*** Starting transaction \n");
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

					order += orderColumn.getName();

					if (orderColumn.getValue() != null) {
						order += " " + orderColumn.getValue();
					}

				}

				order = " order by " + order;

			}

			hQl += order;
			System.out.println(hQl);
			logger.debug("HQL: ");
			logger.debug(hQl);

			Query query = entityManager.createQuery(hQl);

			if ((where != null) && (!where.trim().isEmpty())) {
				if (whereParameters != null) {
					for (NameValuePair par : whereParameters) {
						logger.debug("Set attribute: " + par.getName());

						query.setParameter(par.getName(), par.getValue());
					}
				}
			}

			if (initialRecord > 0) {
				query.setFirstResult(initialRecord);
			}

			if (amountRecord > 0) {
				query.setMaxResults(amountRecord);
			}

			logger.debug("Query: " + query.toString());

			list = query.getResultList();

			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {
					logger.debug("\n*** Commiting transaction \n");
					entityManager.getTransaction().commit();
				}
			}

		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				if (closeTransaction) {
					logger.debug("\n*** Rollback transaction\n");
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

	Integer executeUpdate(List<NameValuePair> setAttributes, String where, List<NameValuePair> whereParameters)
			throws Exception {

		Integer result = 0;

		if (((setAttributes != null) && (setAttributes.size() > 0)) && ((where != null) && (!where.trim().isEmpty()))) {

			initializeEntityManager();

			boolean closeTransaction = false;
			try {
				if (!entityManager.getTransaction().isActive()) {
					logger.debug("\n*** Starting transaction \n");
					entityManager.getTransaction().begin();
					closeTransaction = true;
				}

				String hQl = "update " + entity.getClass().getSimpleName() + " as t " + " set ";

				String attributes = "";

				for (NameValuePair nameValuePair : setAttributes) {

					if (!attributes.isEmpty()) {
						attributes += ", ";
					}

					attributes += nameValuePair.getName() + " = :" + nameValuePair.getName();
				}

				hQl += attributes;
				hQl += " where " + where;
				System.out.println(hQl);
				logger.debug("HQL: ");
				logger.debug(hQl);

				Query query = entityManager.createQuery(hQl);

				for (NameValuePair nameValuePair : setAttributes) {
					logger.debug("Set attribute: " + nameValuePair.getName());

					query.setParameter(nameValuePair.getName(), nameValuePair.getValue());
				}

				if (whereParameters != null) {
					for (NameValuePair par : whereParameters) {
						logger.debug("Set parameter: " + par.getName());

						query.setParameter(par.getName(), par.getValue());
					}
				}

				logger.debug("executeUpdate: " + query.toString());

				result = query.executeUpdate();

				if (entityManager.getTransaction().isActive()) {
					if (closeTransaction) {
						logger.debug("\n*** Commiting transaction \n");
						entityManager.getTransaction().commit();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();

				if (entityManager.getTransaction().isActive()) {
					if (closeTransaction) {
						logger.debug("\n*** Rollback transaction\n");
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

		return result;
	}

}