package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Objects.Institution;

public class ObjectTests extends EdusoftWebTest {
	
	@Before
	public void setup()throws Exception{
		super.setup();
	}
	
	
	@Test
	public void createStudent()throws Exception{
		Institution institution=new Institution();
		institution.setId(config.getProperty("institution.id"));
		institution.setName("institution.name");
		
	}
	
	@After
	public void tearDown()throws Exception{
		super.tearDown();
	}

}
