package drivers;

import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import services.DbService;

public class ChromeWebDriver extends GenericWebDriver {

	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		System.out.println("remote url in chrome webdriver: " + remoteUrl);
		setBrowserName("chrome");
		setInitialized(true);
		dbService = new DbService();
		// setRemoteMachine(remoteUrl);

		logsFolder = folderName;
		try {

			report.startLevel("Initializing ChromeWebDriver",
					Reporter.EnumReportLevel.CurrentPlace);

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			if (enableConsoleLog == true) {
				LoggingPreferences logPrefs = new LoggingPreferences();
				logPrefs.enable(LogType.BROWSER, Level.ALL);
				capabilities.setCapability(CapabilityType.LOGGING_PREFS,
						logPrefs);
			}

			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");

			options.addArguments("--start-maximized");
			options.addArguments("--use-fake-ui-for-media-stream");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);

			report.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

}
