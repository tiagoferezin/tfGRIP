package br.com.tfgrid.test.dal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JPAUtil {

	
	public static EntityManager criaEntityManager() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("postgreSQL");
		return factory.createEntityManager();
	}
	
	@Test
	public void testarConexao() {
		String erro = "";
		try {
			
			EntityManager entityManager = JPAUtil.criaEntityManager();
			
			Assert.assertNotNull(entityManager);
			
			Assert.assertEquals(entityManager.isOpen(), true);
			
		} catch (Exception e) {
			e.printStackTrace();
			erro = e.getMessage();
		}
		
		Assert.assertEquals(erro, "");
		
	}
}
