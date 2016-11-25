/**
 * 
 */
package br.com.tfgrid.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * @author Tiago Ferezin(tiagoferezin, Data: 15/02/2016) Funcionalidade da
 *         Classe: Manipulacao com as datas
 */
public class VerificacaoData {

	public static int getIdade(String data) {
		int retorno = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		Calendar dataString = Calendar.getInstance();

		try {
			dataString.setTime(sdf.parse(data));
			retorno = getIdade(dataString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public static int getIdade(Calendar data) {

		Calendar dataAtual = Calendar.getInstance();
		int retorno = dataAtual.get(Calendar.YEAR) - data.get(Calendar.YEAR);

		data.add(Calendar.YEAR, retorno);

		if (dataAtual.before(data)) {
			retorno--;
		}

		return retorno;
	}

	public static Boolean isDataInseridaMaiorQueDataAtual(
			Calendar dataInserida, Calendar dataAtual) {
		Boolean retorno = false;

		try {
			DateTime data1 = new DateTime(dataInserida);
			DateTime data2 = new DateTime(dataAtual);

			Integer qtdDias = Days.daysBetween(data1, data2).getDays();
			if (qtdDias < 0) {
				retorno = true;
			} else {
				retorno = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return retorno;
	}
	
	public static Integer qtdDiasEntreDatas(Calendar dataInicial, Calendar dataFinal){
		Integer retorno = 0;
		
		try {
			DateTime data1 = new DateTime(dataInicial);
			DateTime data2 = new DateTime(dataFinal);
			retorno = Days.daysBetween(data1, data2).getDays();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return retorno;
	}

}
