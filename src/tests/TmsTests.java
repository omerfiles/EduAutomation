package tests;

import org.junit.After;
import org.junit.Before;


import org.junit.Test;

import pageObjects.EdoLoginPage;
import Objects.Teacher;

public class TmsTests extends EdusoftWebTest {
	
	@Before
	public void setup()throws Exception
	{
		super.setup();
	}
	
	@Test
	public void testLoginToTms()throws Exception{
		
	}
	@Test
	public void testLoginToEdoAsTeacher()throws Exception{
		Teacher teacher=new Teacher();
		teacher.setUserName(config.getProperty("teacher.username"));
		teacher.setPassword(config.getProperty("teacher.password"));
		EdoLoginPage edoLoginPage=new EdoLoginPage(webDriver);
		edoLoginPage.OpenPage(getSutAndSubDomain());
		edoLoginPage.login(teacher);
		
		
		
	}
	
	
	@After
	public void tearDown()throws Exception{
		super.tearDown();
	}

}
