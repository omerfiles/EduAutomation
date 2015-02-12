package Objects;

import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import services.Configuration;
import services.DbService;
import services.NetService;
import services.Reporter;
import services.TestResultService;
import services.TextService;
import junit.framework.TestCase;

public class GenericTestObject extends TestCase {

	protected Reporter report;
//	protected Configuration configuration;
	protected DbService dbService;
	protected NetService netService;
	protected TestResultService testResultService;
	protected TextService textService;
	
	public ClassPathXmlApplicationContext ctx;

	@Before
	public void setup() throws Exception {
		ctx = new ClassPathXmlApplicationContext("beans.xml");
		System.out.println(ctx.toString());

		report = (services.Reporter) ctx.getBean("Reporter");
//		configuration=(Configuration)ctx.getBean("configuration");
		textService = (TextService) ctx.getBean("TextSerivce");
		dbService = (DbService) ctx.getBean("DbService");
		testResultService = (TestResultService) ctx
				.getBean("testResultService");
		
		System.out.println();
		

	}

}
