/**
 * 
 */
package br.com.tfgrid.test.model;

import java.util.Calendar;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import br.com.tfgrid.enumarator.EEstado;
import br.com.tfgrid.enumarator.EPais;
import br.com.tfgrid.model.Municipio;
import br.com.tfgrid.test.dal.ATestDaoNew;

/**
 * @author Tiago Ferezin (Tiago, 24 de nov de 2016) Funcionalidade da Classe:
 */
public class MunicipioTestNG extends ATestDaoNew<Municipio> {

	private Municipio municipio;
	String nome = "Sertãozinho";

	@BeforeTest
	@Override
	public void beforeTest() throws Exception {
		// TODO Auto-generated method stub
		super.beforeTest();
	}

	@Test
	@Override
	public void criacao() {
		// TODO Auto-generated method stub
		String erro = "";

		try {

			municipio = new Municipio();
			Calendar cal = Calendar.getInstance();
			municipio.setDataCriacao(cal);
			municipio.setNome(nome.toUpperCase());
			municipio.setEstado(EEstado.SP);
			municipio.setPais(EPais.BRASIL);
			daoFactory.create(municipio,this.getUserName(), entityManager);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void consulta() {
		// TODO Auto-generated method stub

	}

	@Override
	public void atualizacao() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delecao() {
		// TODO Auto-generated method stub

	}

}
