package tests.misc;

import javax.tools.StandardJavaFileManager;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.junit.Test;

public class JmeterTest extends AbstractJavaSamplerClient {

	@Test
	public void testJmeter() {
		try {
			StandardJMeterEngine engine = new StandardJMeterEngine();

			JMeterUtils.loadJMeterProperties("D:\\jmeter.properties");
//			JMeterUtils.initLogging();// you can comment this line out to see extra
//										// log messages of i.e. DEBUG level
			JMeterUtils.initLocale();

			// JMeter Test Plan, basic all u JOrphan HashTree
			HashTree testPlanTree = new HashTree();
//			SaveService saveService=new SaveService();
//			
//			ResultCollector resultCollector=new ResultCollector()

			// HTTP Sampler
			HTTPSampler httpSampler = new HTTPSampler();
			httpSampler.setDomain("tmsnov14.prod.com");
			httpSampler.setPort(80);
			httpSampler.setPath("/Login/login.aspx");
			httpSampler.setMethod("GET");
		

			// Loop Controller
			LoopController loopController = new LoopController();
			loopController.setLoops(1);
			loopController.addTestElement(httpSampler);
			loopController.setFirst(true);
			loopController.initialize();

			// Thread Group
			ThreadGroup threadGroup = new ThreadGroup();
			threadGroup.setNumThreads(50);
			threadGroup.setRampUp(1);
			threadGroup.setSamplerController(loopController);

			// Test Plan
			TestPlan testPlan = new TestPlan("Create JMeter Script From Java Code");

			// Construct Test Plan from previously initialized elements
			testPlanTree.add("testPlan", testPlan);
			testPlanTree.add("loopController", loopController);
			testPlanTree.add("threadGroup", threadGroup);
			testPlanTree.add("httpSampler", httpSampler);
		

			// Run Test Plan
			engine.configure(testPlanTree);
			engine.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
