package drivers;

import java.net.URL;
import java.util.Arrays;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

import services.DbService;

@Service
public class FirefoxWebDriver extends GenericWebDriver {
	
	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		setBrowserName("firefox");
		setInitialized(true);
		dbService = new DbService();
		report.report("Remote url from pom file is: " + remoteUrl);
		// sutUrl = configuration.getProperty("sut.url");
		logsFolder = folderName;
		try {
			if (remoteUrl == null) {
				// remoteUrl = configuration.getProperty("remote.machine");
			}
			report.startLevel("Initializing WebDriver",
					Reporter.EnumReportLevel.CurrentPlace);

//			 DesiredCapabilities capabilities = new DesiredCapabilities("firefox", "29.0.1", Platform.WINDOWS);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					DesiredCapabilities.firefox());
			// webDriver = new RemoteWebDriver( capabilities);

			report.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}
	@Override
	public void waitForElementAndClick(String idValue, String byType)throws Exception{
		 waitForElement(idValue, byType, timeout, true).sendKeys(Keys.ENTER);
	}
	
	@Override
	public void clickOnElement(WebElement td) {
//		td.sendKeys(Keys.ENTER);
		JavascriptExecutor executor = (JavascriptExecutor) webDriver;
		executor.executeScript("arguments[0].click();", td);
		
	}
	
//	@Override
//	public void swithcToFrameAndSendKeys(String xpathExpression, String keys,
//			boolean clear, String frameId) throws Exception {
//		String currentWindow = webDriver.getWindowHandle();
//		webDriver.switchTo().frame(frameId);
//		// webDriver.findElement(By.xpath(xpathExpression)).click();
//		// webDriver.findElement(By.xpath(xpathExpression)).sendKeys(keys);
//
//		WebElement element = webDriver.findElement(By.xpath(xpathExpression));
//
//		element.click();
//		if (clear == true) {
//			element.clear();
//		}
//		JavascriptExecutor executor = (JavascriptExecutor)webDriver;
//		executor.executeScript("arguments[0].innerHTML = '<h1>"+keys+"</h1>'", element);
//		
////
////		sendKey(keys);
////		element.sendKeys(keys);
//		
//		webDriver.switchTo().window(currentWindow);
//	}
	

}
