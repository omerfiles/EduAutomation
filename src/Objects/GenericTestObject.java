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
	// protected Configuration configuration;
	protected DbService dbService;
	protected NetService netService;
	protected TestResultService testResultService;
	protected TextService textService;
	protected Configuration configuration;

	public ClassPathXmlApplicationContext ctx;

	@Before
	public void setup() throws Exception {
		try {
			
			System.out.println("******    Test details and used configuration  **********");
			
			ctx = new ClassPathXmlApplicationContext("beans.xml");
			configuration = (Configuration) ctx.getBean("configuration");
			report = (services.Reporter) ctx.getBean("Reporter");
			// configuration=(Configuration)ctx.getBean("configuration");
			textService = (TextService) ctx.getBean("TextSerivce");
			dbService = (DbService) ctx.getBean("DbService");
			testResultService = (TestResultService) ctx
					.getBean("testResultService");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Failed during GenericTestObject setup: "
					+ e.toString());
		}

	}

}
