package tests.misc;

import javax.tools.StandardJavaFileManager;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.junit.Test;

public class JmeterTest {

	
	@Test
	public void testJmeter(){
		StandardJMeterEngine engine=new StandardJMeterEngine();
		
//		  JMeterUtils.loadJMeterProperties("/path/to/your/jmeter/bin/jmeter.properties");
//	        JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
//	        JMeterUtils.initLocale();

	        // JMeter Test Plan, basic all u JOrphan HashTree
	        HashTree testPlanTree = new HashTree();

	        // HTTP Sampler
	        HTTPSampler httpSampler = new HTTPSampler();
	        httpSampler.setDomain("http://tmsnov14.prod.com/");
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
	        threadGroup.setNumThreads(1);
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
		
	}

}
