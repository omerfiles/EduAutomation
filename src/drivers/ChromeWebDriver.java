package drivers;

import java.net.URL;
import java.util.Arrays;

import jsystem.framework.report.Reporter;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import services.DbService;

public class ChromeWebDriver extends GenericWebDriver {
	
	@Override
	public void init(String remoteUrl, String folderName) throws Exception {
		setBrowserName("chrome");
		setInitialized(true);
		dbService = new DbService();
		report.report("Remote url from pom file is: " + remoteUrl);
		// sutUrl = configuration.getProperty("sut.url");
		logsFolder = folderName;
		try {
			if (remoteUrl == null) {
				// remoteUrl = configuration.getProperty("remote.machine");
			}
			report.startLevel("Initializing ChromeWebDriver",
					Reporter.EnumReportLevel.CurrentPlace);

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			capabilities.setCapability("chrome.switches",
					Arrays.asList("--start-maximized"));
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			// capabilities.setCapability("platform", "Windows 2003");
			webDriver = new RemoteWebDriver(new URL(remoteUrl + "/wd/hub"),
					capabilities);
			// webDriver = new RemoteWebDriver( capabilities);

			report.stopLevel();
		} catch (Exception e) {
			logger.error("Cannot register node or start the remote driver! ", e);
		}
	}

}
