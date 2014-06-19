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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

import services.DbService;

@Service
public class FirefoxWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		this.timeout = 30;
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
			report.startLevel("Initializing FirefoxWebDriver",
					Reporter.EnumReportLevel.CurrentPlace);
			ProfilesIni profile = new ProfilesIni();
			FirefoxProfile firefoxProfile = profile.getProfile("automation");
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);
			// webDriver = new RemoteWebDriver( capabilities);

			report.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

	@Override
	public void waitForElementAndClick(String idValue, String byType)
			throws Exception {
		waitForElement(idValue, byType, this.timeout, true)
				.sendKeys(Keys.ENTER);
	}

	@Override
	public void clickOnElement(WebElement td) {
		// td.sendKeys(Keys.ENTER);
		JavascriptExecutor executor = (JavascriptExecutor) webDriver;
		executor.executeScript("arguments[0].click();", td);

	}

	// }

}
