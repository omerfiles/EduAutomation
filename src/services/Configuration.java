package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
@Service
public class Configuration {
	private static Configuration configuration = new Configuration();
	public Configuration() {
		InputStream input = null;
		try {
			input = new FileInputStream("files/properties/QA/qa.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// load a properties file
		try {
			properties.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Configuration getInstance() {

        return configuration;
    }

	private static final String LIST_FORMAT = "       %1$-70s| %2$s%n";
	public static final String OVERRIDE_PROPERTIES_FILENAME = "";

	private Properties properties = new Properties();

	private static final Logger logger = LoggerFactory
			.getLogger(Configuration.class);
	/**
	 * Field service
	 */

	private static final String TEMPLATES_FOLDER = "templates";
	// public static InternetAddress FROM = null;
	// public static InternetAddress EMAIL_BILLING = null;

	public static final String SUPPORT_EMAIL = "";
	public static final String NOTIFICATIONS_EMAIL = "";
	public static final String MEMBERS_EMAIL = "";
	public static final String DO_NOT_REPLY_EMAIL = "";

	public static String HOST;

	// public static final Date STARTUP_DATE = new
	// Date(System.currentTimeMillis());
	//
	// static {
	// try {
	// // FROM = new InternetAddress("support@zao.com", "Zao Support");
	// // EMAIL_BILLING = new InternetAddress("billing@zao.com", "Zao Billing");
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }

	public void put(String key, String value) {

		Assert.notNull(key);
		Assert.notNull(value);
		properties.put(key, value);
	}

	public String getProperty(String key, String defaultValue) {

		return properties.getProperty(key, defaultValue);
	}

	public String getProperty(String key) {

		return getProperty(key, key + " not found");
	}

	public Set<Object> getKeys() {
		return properties.keySet();
	}

	public Properties getAll() {
		return properties;
	}

	public boolean isDevelopmentMode() {
		return isMode("development");
	}

	public boolean isQAMode() {
		return isMode("qa");
	}

	public String getProjectBuildVersion() {
		return getProperty("");
	}

	public String getDomain() {
		return getProperty("");
	}

	public String getCookieDomain() {
		return getProperty("");
	}

	public String getHostInformation() {
		try {
			InetAddress host = InetAddress.getLocalHost();
			return host.getHostName() + "- IP " + host.getHostAddress();
		} catch (UnknownHostException e) {
			return "Host information cannot be retrieved. " + e.getMessage();
		}
	}

	private String getHostname() {
		try {
			InetAddress host = InetAddress.getLocalHost();
			return host.getHostName();

		} catch (UnknownHostException e) {
			return "Host information cannot be retrieved. " + e.getMessage();
		}
	}

	public String getRunningHost() {
		return HOST;
	}

	public String getIpAddress() {
		try {
			InetAddress host = InetAddress.getLocalHost();
			return host.getHostAddress();
		} catch (UnknownHostException e) {
			return "Host information cannot be retrieved. " + e.getMessage();
		}
	}

	public void updateProperties(Properties updatedProperties)
			throws IOException {

		Enumeration enumeration = updatedProperties.propertyNames();
		while (enumeration.hasMoreElements()) {
			String newKey = enumeration.nextElement().toString();

			logger.info(
					"Merging property {} . value ({}) will be replaced with {} ",
					new Object[] { newKey, getProperty(newKey),
							updatedProperties.getProperty(newKey) });

			setProperty(newKey, updatedProperties.getProperty(newKey));
		}
	}

	private void setProperty(String newKey, String value) {
		properties.setProperty(newKey, value);
	}

	public String getHomeDirectory() {
		return System.getProperty("zao.home", getProperty("zao.home", "NONE"));
	}

	public boolean isNotifyAdminOnErrorEnabled() {
		return getProperty("", "true").equals("true");
	}

	public int getMaxNumOfActiveNotificationsToProcess() {
		return Integer.parseInt(getProperty("", "10"));

	}

	public boolean isTestMode() {
		return getProperty("", "").equals("test");
	}

	public String getApplicationAdminEmail() {
		return getProperty("", "");
	}

	public void updateProperty(String propertyKey, String propertyValue) {
		logger.info(
				"Merging property {} . value ({}) will be replaced with {} ",
				new Object[] { propertyKey, getProperty(propertyKey),
						propertyValue });
		setProperty(propertyKey, propertyValue);
	}

	public boolean isProductionMode() {
		return isMode("production");
	}

	public boolean isMode(String mode) {
		return getProperty("", "").equals(mode);
	}

	public String getEnvironmentConfiguration() {
		return getProperty("", "");
	}

	public String getHome() {
		return getProperty("", "");
	}

	public String getProjectBuildDate() {
		return getProperty("", "");
	}

	public String getEmailDevelopmentRecipients() {
		return getProperty("", "");
	}
}
