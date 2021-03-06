package com.finstro.automation.appium.driver;

import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.finstro.automation.report.HtmlReporter;
import com.finstro.automation.report.Log;
import com.finstro.automation.utility.FilePaths;
import com.finstro.automation.utility.PropertiesLoader;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class AppiumIOsDriver extends AppiumBaseDriver{


	private static Properties ios_configuration;
	private static Properties browser_configuration;
	private static Properties appium_configuration;

	public AppiumIOsDriver() throws Exception {
		
		ios_configuration = PropertiesLoader.getPropertiesLoader().ios_configuration;
		appium_configuration = PropertiesLoader.getPropertiesLoader().appium_configuration;
		browser_configuration = PropertiesLoader.getPropertiesLoader().browser_configuration;
		
	}
	
	private void setAppiumCapabilities(DesiredCapabilities capabilities) {
		
		String platform = appium_configuration.getProperty("appium.platform");
		String platformVersion = appium_configuration.getProperty("appium.platformVersion");
		String deviceName = appium_configuration.getProperty("appium.deviceName");
		String deviceUDID =  appium_configuration.getProperty("appium.device.udid");
		String booleanMobileNoReset = appium_configuration.getProperty("appium.noReset");
		String booleanMobileFullReset = appium_configuration.getProperty("appium.fullReset");
		String strAppiumVersion = appium_configuration.getProperty("appium.version");
		
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platform);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		capabilities.setCapability(MobileCapabilityType.NO_RESET, booleanMobileNoReset);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, booleanMobileFullReset);
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
		
		if (!(strAppiumVersion == null || strAppiumVersion.equals(""))) {
			capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, strAppiumVersion);
		}

		if(!(deviceUDID == null || deviceUDID.equals(""))) {
			capabilities.setCapability(MobileCapabilityType.UDID, deviceUDID);
		}
	}

	private void setIOSAppCapabilities(DesiredCapabilities capabilities) throws Exception {
		
		String strMobileAndroidApp = FilePaths.getResourcePath("/app/" + ios_configuration.getProperty("appium.android.app"));
		String strMobileAppPackage = ios_configuration.getProperty("appium.android.appPackage");
		String strMobileAppActivity = ios_configuration.getProperty("appium.android.appActivity");
		
		capabilities.setCapability(MobileCapabilityType.APP, strMobileAndroidApp);
		
		if (!(strMobileAppPackage == null || strMobileAppPackage.equals(""))) {
			
			capabilities.setCapability("appPackage", strMobileAppPackage);
			
		}
		
		if (!(strMobileAppActivity == null || strMobileAppActivity.equals(""))) {
			
			capabilities.setCapability("appActivity", strMobileAppActivity);
			
		}
	}
	
	private void setIOSBrowserCapabilities(DesiredCapabilities capabilities) {
		
		String browserName = browser_configuration.getProperty("appium.browser.name");
		String browserVersion = browser_configuration.getProperty("appium.browser.version");
		
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browserName);
		
		if (!(browserVersion == null || browserVersion.equals(""))) {
			
			capabilities.setCapability(MobileCapabilityType.BROWSER_VERSION, browserVersion);
			
		}
		
	}

	/**
	 * This method is used to open a appium driver, use configuration.property to
	 * create driver
	 * 
	 * @author tunn6
	 * @return AppiumDriver driver
	 * @throws Exception
	 */
	public void createDriver() throws Exception {
		
		String strAppiumServer = appium_configuration.getProperty("appium.server");
		
		try {
			
			DesiredCapabilities capabilities = new DesiredCapabilities();
			setAppiumCapabilities(capabilities);
			
			// Use SauceLab remote server instead of local Appium server if configured
			if (Boolean.getBoolean(appium_configuration.getProperty("appium.saucelab.enable"))) {
				
				String strSauceIOsApp = appium_configuration.getProperty("appium.saucelab.iOSApp");
				String strSauceRemoteUrl = appium_configuration.getProperty("appium.saucelab.remoteUrl");
				String strSauceUserName = appium_configuration.getProperty("appium.saucelab.username");
				String strSauceAccessKey = appium_configuration.getProperty("appium.saucelab.accessKey");
				strAppiumServer = "https://" + strSauceUserName + ":" + strSauceAccessKey + "@" + strSauceRemoteUrl;
				capabilities.setCapability(MobileCapabilityType.APP, strSauceIOsApp);
			}

			// check if test on mobile browser
			if (Boolean.getBoolean(appium_configuration.getProperty("appium.browser.enable"))) {
				
				setIOSBrowserCapabilities(capabilities);
				
			} else {
				
				setIOSAppCapabilities(capabilities);
				
			}
			
			
			driver = new IOSDriver<WebElement>(new URL(strAppiumServer), capabilities);

			Log.info("Starting remote Android driver for: " + capabilities.toString());
			HtmlReporter.pass("Starting remote Android driver for: " + capabilities.toString());

		} catch (Exception e) {
			Log.error("Can't start the webdriver!!! : " + e);
			HtmlReporter.fail("Can't start the webdriver!!! : ", e, "");
			throw (e);
		}
	}
	
	/**
	 * This method is used to open a appium driver, use configuration.property to
	 * create driver
	 * 
	 * @author tunn6
	 * @param deviceInfo
	 * @return AppiumDriver driver
	 * @throws Exception
	 */
	public void createAWSDriver() throws Exception {


		DesiredCapabilities capabilities = null;
		try {

			driver = new IOSDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

			Log.info("Starting Android driver on AWS");
			HtmlReporter.pass("Starting Andoid driver on AWS");

		} catch (Exception e) {
			Log.error("Can't start the android driver!!! : " + e);
			HtmlReporter.fail("Can't start the android driver!!! : ", e, "");
			throw (e);
		}
	}
}
