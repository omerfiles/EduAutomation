package drivers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jsystem.framework.report.Reporter;
import jsystem.framework.report.Reporter.EnumReportLevel;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import services.DbService;

@Service
public class IEWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		this.timeout = 30;
		setBrowserName("Internet Exporer");
		setInitialized(true);
		dbService = new DbService();
		report.report("Remote url from pom file is: " + remoteUrl);
		// sutUrl = configuration.getProperty("sut.url");
		logsFolder = folderName;
		try {
			if (remoteUrl == null) {
				// remoteUrl = configuration.getProperty("remote.machine");
			}
			report.startLevel("Initializing IEWebDriver",
					Reporter.EnumReportLevel.CurrentPlace);

			DesiredCapabilities capabilities = DesiredCapabilities
					.internetExplorer();
			capabilities.setCapability(
					CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION,
					true);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);
			// webDriver = new RemoteWebDriver( capabilities);

			report.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

	// @Override
	// public WebElement waitForElement(String idValue, String byType,
	// int timeout, boolean isMandatory) throws Exception {
	// WebElement element = null;
	// int elapsedTime = 0;
	// try {
	// while (elapsedTime < timeout && element == null)
	//
	// {
	// try {
	//
	// if (byType.equals("id")) {
	// element = webDriver.findElement(By.id(idValue));
	// elapsedTime = timeout;
	// } else {
	// if (byType.equals("xpath")) {
	// element = webDriver.findElement(By.xpath(idValue));
	// elapsedTime = timeout;
	// } else {
	// if (byType.equals("class")) {
	// element = webDriver.findElement(By
	// .className(idValue));
	// elapsedTime = timeout;
	// } else {
	// if (byType.equals("name")) {
	// element = webDriver.findElement(By
	// .name(idValue));
	// elapsedTime = timeout;
	// } else {
	// if (byType.equals("linkText")) {
	// element = webDriver.findElement(By
	// .linkText(idValue));
	// elapsedTime = timeout;
	// }
	// }
	//
	// }
	//
	// }
	// }
	// }// end of try
	// catch (Exception e) {
	// report.report("Element not found. sleeping for 1000ms");
	// sleep(1000);
	// elapsedTime += 1;
	// continue;
	// }
	//
	//
	//
	// } // end of while loop
	// } catch (Exception e) {
	// if (isMandatory == true) {
	// this.addFailCause("element " + idValue + " not found");
	// Assert.fail();
	// // printScreen();
	// }
	//
	// }
	// if (element == null && isMandatory == true) {
	// this.addFailCause("element " + idValue + " not found");
	// report.startLevel("element " + idValue +
	// " not found",EnumReportLevel.MainFrame);
	// printScreen("element " + idValue + " not found", null);
	// // Assert.fail(this.failCause.toString());
	//
	// // printScreen();
	// }
	//
	// return element;
	//
	// }
	@Override
	public void waitForElementAndClick(String idValue, String byType)
			throws Exception {
		waitForElement(idValue, byType, this.timeout, true).click();
	}

	@Override
	public String switchToNewWindow(int windowId) throws Exception {
		Thread.sleep(10000);
		Set<String> winhandles = webDriver.getWindowHandles();
		List<String> windows = new ArrayList<String>();
		windows.addAll(winhandles);
		System.out.println("before switch: " + webDriver.getWindowHandle());
		String oldWindow = webDriver.getWindowHandle();
		webDriver.switchTo().window(windows.get(windowId));
		System.out.println("after switch: " + webDriver.getWindowHandle());
		return oldWindow;

	}
}
