package drivers;

import java.net.URL;
import java.util.Arrays;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
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

import Enums.ByTypes;
import services.DbService;

@Service
public class FirefoxWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		this.timeout = 10;
		setBrowserName("firefox");
		setInitialized(true);
		dbService = new DbService();
		reporter.report("Remote url from pom file is: " + remoteUrl);

		logsFolder = folderName;
		try {
			if (remoteUrl == null) {
				// remoteUrl = configuration.getProperty("remote.machine");
			}
			// report.startLevel("Initializing FirefoxWebDriver",
			// Reporter.EnumReportLevel.CurrentPlace);
			ProfilesIni profile = new ProfilesIni();
//			System.out.println("setting firefix profile to allow media");
			// FirefoxProfile firefoxProfile = profile.getProfile("automation");
			FirefoxProfile firefoxProfile = new FirefoxProfile();

			firefoxProfile.setPreference("media.navigator.permission.disabled",
					true);

			// FirefoxProfile.setPreference(
			// "media.navigator.permission.disabled", true);
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);
			// webDriver = new RemoteWebDriver( capabilities);
			setPageLoadTimeOut();
			setScriptLoadTimeOut();
			reporter.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ",
					e.toString());
		}
	}

	@Override
	public void waitForElementAndClick(String idValue, ByTypes byType)
			throws Exception {
		try {
			waitForElement(idValue, byType, this.timeout, true).click();
		} catch (InvalidElementStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clickOnElement(WebElement element) {
		// td.sendKeys(Keys.ENTER);
		JavascriptExecutor executor = (JavascriptExecutor) webDriver;
		executor.executeScript("arguments[0].click();", element);

	}

	// }

}
