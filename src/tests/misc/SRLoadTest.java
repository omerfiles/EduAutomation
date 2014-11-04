package tests.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;

import pageObjects.EdoHomePage;
import pageObjects.RecordPanel;
import services.AudioService;
import Enums.AutoParams;
import Enums.ByTypes;
import drivers.ChromeWebDriver;
import drivers.ThreadedWebDriver;

public class SRLoadTest extends EdusoftBasicTest {

	int numberOfInstances = 8;
	List<ChromeWebDriver> webDriverList = new ArrayList<ChromeWebDriver>();
	List<RecordPanel> recordPanels = new ArrayList<RecordPanel>();

	ThreadGuard threadGuard = new ThreadGuard();

	@Test
	public void testMultipleBrowserInstances() throws Exception {

	String slaveName=	configuration.getAutomationParam(null,"slaveNameCMD");
	System.out.println("Slave name is:"+slaveName);	
	if(slaveName==null){
		slaveName="devMachine";
	}
	
	netService.updateSlaveStatus(slaveName, "not ready");
	AudioService audioService = new AudioService();

		for (int i = 0; i < numberOfInstances; i++) {
			ChromeWebDriver driver = new ChromeWebDriver();
			driver.init(
					configuration.getAutomationParam(
							AutoParams.remoteMachine.toString(), "machine"),
					null);
			driver.setReporter(report);
			driver.setTestResultService(testResultService);
			webDriverList.add(driver);
		}

		for (int i = 0; i < numberOfInstances; i++) {
			webDriverList.get(i).openUrl(
					"http://edonov14.prod.com/automation.aspx");
			webDriverList
					.get(i)
					.addValuesToCookie(
							"Student",
							"^StudentID*3000025000321^Language*spa^LangSupLevel*3^Courses*0^FName*TMS^LName*Domain^SID*53638^CMode*L^UserType*da^Type*2^LCE*");

			webDriverList
					.get(i)
					.openUrl(
							"http://edonov14.prod.com/Runtime/ViewComponents.aspx?id=3000025000321&courseId=8&unitId=78&componentId=284&skill=Speaking&componentName=How%20Awful!&componentType=1&level=component");
			EdoHomePage edoHomePage = new EdoHomePage(webDriverList.get(i),
					testResultService);

			edoHomePage.clickOnSeeScript();
			sleep(2);
			edoHomePage.selectTextFromContainer(1);

			RecordPanel recordPanel = edoHomePage.clickOnRecordYourself();
			recordPanel.setWebDriver(webDriverList.get(i));
			recordPanels.add(recordPanel);
			sleep(4);
			edoHomePage.switchToFrameByClassName("cboxIframe");
			//Wait for speak
			
		

		}
		
		netService.updateSlaveStatus(slaveName, "ready");
		netService.checkAllSlaveStatus();
		// click on all record buttons
		for (int i = 0; i < numberOfInstances; i++) {
			recordPanels.get(i).clickOnRecordButton();
			sleep(2);
			String status = recordPanels.get(i).getRecordPanelStatus();
			testResultService.assertEquals("SPEAK", status);
			

		}

		// play the file
		audioService.sendSoundToVirtualMic(new File(
				"files/audioFiles/TheBeatMe16000_16.wav"), 16000.0F);
		for (int i = 0; i < numberOfInstances; i++) {
			recordPanels.get(i).checkErrorMessageDoesNotExist();
		}
		System.out.println("Playing ended");
		netService.updateSlaveStatus(slaveName, "not ready");
	}

	public void webdriverThreadGuard() throws Exception {
		// ChromeWebDriver webDriver=new ChromeWebDriver();
		// webDriver.init(configuration.getProperty("remote.machine"), null);
		//
		// ThreadGuard threadGuard=new ThreadGuard();
		//
		// threadGuard.protect(webDriver.getWebDriver());
		for (int i = 0; i < numberOfInstances; i++) {
			ChromeWebDriver driver = new ChromeWebDriver();
			driver.init(configuration.getProperty("remote.machine"), null);
			webDriverList.add(driver);
		}

		// TableThread tableThread = new TableThread(driver, "Thread " + i);
		for (int i = 0; i < numberOfInstances; i++) {
			ExecutorService threadExecutor = Executors
					.newSingleThreadExecutor();
			ThreadedWebDriver threadedWebDriver = new ThreadedWebDriver(
					webDriverList.get(i));
			threadExecutor.execute(threadedWebDriver);
		}

	}

	@After
	public void tearDown() throws Exception {
		for (int i = 0; i < numberOfInstances; i++) {
			webDriverList.get(i).quitBrowser();
		}
	}

}
