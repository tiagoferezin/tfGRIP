/**
 * 
 */
package br.com.tfgrid.dal;

import java.security.MessageDigest;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import org.apache.commons.codec.digest.Md5Crypt;

import br.com.tfgrid.model.acesso.Usuario;
import br.com.tfgrid.model.ancestral.AEntity;
import br.com.tfgrid.utils.NameValuePair;

/**
 * @author Tiago Ferezin (Tiago, 10 de jun de 2016) Funcionalidade da Classe:
 */
public class DaoFactory {

	public void create(AEntity entity, EntityManager entityManager) throws ConstraintViolationException, Exception {

		Dao result = new Dao(entity, entityManager);
		result.create();

	}

	public void create(Usuario entity, String userName, EntityManager entityManager)
			throws ConstraintViolationException, Exception {

		String nameSalt1 = Md5Crypt.apr1Crypt(entity.getUsername());
		String nameSalt2 = Md5Crypt.apr1Crypt(entity.getEmail());
		String nameSalt;
		nameSalt = nameSalt1 + nameSalt2;
		nameSalt = nameSalt.toLowerCase();
		nameSalt = Normalizer.normalize(nameSalt, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		pattern.matcher(nameSalt).replaceAll("");

		String senha = entity.getSenha();
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		byte messageDigest[] = algorithm.digest((senha + nameSalt).getBytes("UTF-8"));
		StringBuilder hexString = new StringBuilder();
		for (byte b : messageDigest) {
			hexString.append(String.format("%02X", 0xFF & b));
		}

		String senha2 = hexString.toString();

		entity.setSenha(senha2);
		entity.setSalt(nameSalt);
		this.create(entity, userName, entityManager);
	}

	public AEntity read(AEntity entity, String userName, EntityManager entityManager, Long id) throws Exception {
		List<AEntity> list = new ArrayList<AEntity>();

		String where = "(t.id = :id)";

		List<NameValuePair> whereParameters = new ArrayList<NameValuePair>();

		whereParameters.add(new NameValuePair("id", id));

		list = read(entity, entityManager, where, whereParameters, 0, 0, "", "");

		AEntity result = entity.getClass().newInstance();

		if ((list != null) && (list.size() > 0)) {
			result = list.get(0);
		}

		return result;

	}

	public List<AEntity> read(AEntity entity, EntityManager entityManager, String where,
			List<NameValuePair> whereParameters) throws Exception {
		List<AEntity> list = new ArrayList<AEntity>();

		list = read(entity, entityManager, where, whereParameters, 0, 0, null, null, null);

		return list;

	}

	public List<AEntity> read(AEntity entity, String userName, EntityManager entityManager, String where,
			List<NameValuePair> whereParameters, List<NameValuePair> orderBy) throws Exception {
		List<AEntity> list = new ArrayList<AEntity>();

		list = read(entity, entityManager, where, whereParameters, 0, 0, orderBy, true);

		return list;

	}

	public List<AEntity> read(AEntity entity, EntityManager entityManager, String where,
			List<NameValuePair> whereParameters, Boolean registrosAtivos) throws Exception {
		List<AEntity> list = new ArrayList<AEntity>();

		list = read(entity, entityManager, where, whereParameters, 0, 0, null, null, registrosAtivos);

		return list;

	}

	public List<AEntity> read(AEntity entity, EntityManager entityManager, String where,
			List<NameValuePair> whereParameters, Integer initialRecord, Integer amountRecord, String orderColumn,
			String orderDirection) throws Exception {
		List<AEntity> list = new ArrayList<AEntity>();
		list = read(entity, entityManager, where, whereParameters, initialRecord, amountRecord, orderColumn,
				orderDirection, true);
		return list;
	}

	public List<AEntity> read(AEntity entity, EntityManager entityManager, String where,
			List<NameValuePair> whereParameters, Integer initialRecord, Integer amountRecord, String orderColumn,
			String orderDirection, Boolean registrosAtivos) throws Exception {

		List<AEntity> list = new ArrayList<AEntity>();

		List<NameValuePair> orderBy = new ArrayList<NameValuePair>();

		if ((orderColumn != null) && (!orderColumn.isEmpty())) {
			if ((orderDirection == null) || (orderDirection.isEmpty())) {
				orderDirection = "asc";
			}

			orderBy.add(new NameValuePair(orderColumn, orderDirection));
		}

		list = read(entity, entityManager, where, whereParameters, initialRecord, amountRecord, orderBy, true);

		return list;

	}

	// Metodo responsável por acessar efetivamente a Dao [FB]
	public List<AEntity> read(AEntity entity, EntityManager entityManager, String where,
			List<NameValuePair> whereParameters, Integer initialRecord, Integer amountRecord,
			List<NameValuePair> orderBy, Boolean registrosAtivos) throws Exception {

		Dao dao = new Dao(entity, entityManager);
		List<AEntity> list = dao.read(entity, entityManager, where, whereParameters, initialRecord, amountRecord,
				orderBy, true);

		return list;

	}

	public void update(AEntity entity, String userName, EntityManager entityManager)
			throws ConstraintViolationException, Exception {
		Dao result = new Dao(entity, entityManager);
		result.update();
	}

	public void delete(AEntity entity, String userName, EntityManager entityManager)
			throws ConstraintViolationException, Exception {
		Dao result = new Dao(entity, entityManager);
		result.delete();
	}

}
