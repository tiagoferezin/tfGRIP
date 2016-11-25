package br.com.tfgrid.test.dal;

import java.lang.reflect.ParameterizedType;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public abstract class ATestBeanDao<T> implements ITestDao {
	protected T objectTest;
	
	
	public Class<T> getClassT() throws Exception {
		ParameterizedType superclass = (ParameterizedType) getClass()
				.getGenericSuperclass();

		return (Class<T>) superclass.getActualTypeArguments()[0];
	}

	protected T getInstanceT() throws Exception {

		Class<T> classT = getClassT();

		return classT.newInstance();
	}


	
	@Override
	@BeforeTest
	public void beforeTest() throws Exception {
		objectTest = getInstanceT();
		//((ABeanDao)objectTest).setUserName(this.getUserName());

	}
	
	@Override
	@AfterTest
	public void afterTest() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openehr.cw.test.ITestDao#getObjectTest()
	 */
	@Override
	public Object getObjectTest() {
		return this.objectTest;
	}
	



}
