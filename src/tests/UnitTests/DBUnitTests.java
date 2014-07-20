package tests.UnitTests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tests.misc.EdusoftBasicTest;

public class DBUnitTests extends EdusoftBasicTest {

	
	@Before
	public void setup()throws Exception{
		super.setup();
	}
	
	@Test
	public void testGetResultFromQuery()throws Exception
	{
		String sql="select UserId from users where username='student6'";
		String result=dbService.getStringFromQuery(sql);
		Assert.assertNotNull(result);
	}
	
	@After
	public void tearDown()throws Exception{
		
	}

}