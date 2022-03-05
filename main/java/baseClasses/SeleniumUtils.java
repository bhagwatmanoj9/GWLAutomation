package baseClasses;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;
import org.yaml.snakeyaml.Yaml;

import com.opera.core.systems.OperaDriver;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class SeleniumUtils {

	public static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();
	public static WebDriver webDriver = null;
	public static final String EMPTY_KEYWORD = "{empty}";
	public static final String SPACE_KEYWORD = "{space}";
	private final long waitForWebElementTimeOut = 35000l;
	private final long waitForWebElementNotExistTimeOut = 1l;

//	public static AndroidDriver<WebElement> driver;
//	public static AppiumDriver<WebElement> _driver;

	public SendEmail email = new SendEmail();
	static PrintWriter write = new PrintWriter(System.out, true);
	public static ReadingAndWritingTextFile file = new ReadingAndWritingTextFile();
	DesiredCapabilities capability;

	protected static GlobalVariables g = new GlobalVariables();
	private static Utilities suiteConfig = new Utilities();
	private static String gridMode = null;
	protected static String timestamp = null;
	protected static Utilities util = new Utilities();
	public static HashMap<String, String> testData = new HashMap<String, String>();

	public Connection con = null;
	public static Statement st = null;
	public static RestHighLevelClient client = null;
	public static String NBORDERID;
	public String host;
	public String port;
	public String elasticIp;
	public int elasticPort;
	public static String elasticIndex;
	public static int TotalCases;
	Properties prop = new Properties();

	SoftAssert softAssert = new SoftAssert();
	static SoftAssert sAssert = new SoftAssert();
	public String AutomationSuite;
	public String ExecutionType;
	public String BrowserStack;

	public static final ThreadLocal<WebDriver> WEB_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();
	// public static final ThreadLocal<WebDriver> WEB_DRIVER_THREAD_LOCAL1 = new
	// InheritableThreadLocal<>();

	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> setBasicInfomationForTestData(String fileName) {

		HashMap<String, Object> basicInfoDataset = null;
		try {
			// Report.LogInfo("ReadBasicInfo", "Reading basic url information", "INFO");
			FileInputStream input = new FileInputStream(new File("./Resources/URL_information.yml"));
			Yaml yaml = new Yaml();
			basicInfoDataset = ((HashMap<String, Object>) ((HashMap<String, Object>) yaml.load(input)).get(fileName));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			JOptionPane.showMessageDialog(null, "Error in YML file loading,Check the syntax");
		}
		return basicInfoDataset;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getUrlInfoDataset(HashMap<String, Object> ymlurlDataset, String urlTagName)
			throws FileNotFoundException {
		return ((HashMap<String, Object>) ymlurlDataset.get(urlTagName));
	}

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws Exception {
		String path = new File(".").getCanonicalPath();
		g.setRelativePath(path);

		InputStream inputA = new FileInputStream(g.getRelativePath() + "/configuration.properties");
		prop.load(inputA);
		System.setProperty("logback.configurationFile", g.getRelativePath() + "/logback.xml");
		AutomationSuite = prop.getProperty("AuomationSuite");
		ExecutionType = prop.getProperty("ExecutionType");
		if (AutomationSuite.equals("NMTS Number Management")) {
			g.setBrowser("IE");
		} else {

			g.setBrowser(util.getValue("Browsers", "firefox").trim().split(",")[0]);

		}

		if (ExecutionType.equalsIgnoreCase("Parallel")) {
			g.setGridMode("ON");
			g.setHubAddress("http://localhost:5555/wd/hub");
		} else {
			g.setGridMode("OFF");
		}

		gridMode = g.getGridMode();

		timestamp = "RegressionSuite_" + suiteConfig.getCurrentDatenTime("MM-dd-yyyy") + "_"
				+ suiteConfig.getCurrentDatenTime("hh-mm-ss_a");

		String resultPath = path + "//GWRResults//" + timestamp;
		String ScreenshotsPath = resultPath + "//Screenshots";

		g.setResultFolderPath(resultPath);
		g.setTestSuitePath(path + "//Resources//TestSuite.xls");

		new File(resultPath).mkdirs();
		new File(ScreenshotsPath).mkdirs();

		String SummaryReportfile = resultPath + "\\Index.html";

		Report.createSummaryReportHeader(SummaryReportfile);

		try {
			InputStream input1 = new FileInputStream(g.getRelativePath() + "/configuration.properties");
			// InputStream input2 = new
			// FileInputStream(g.getRelativePath()+"/PortalDBConfig.properties");

			// load a properties file
			prop.load(input1);
			// prop.load(input2);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.setProperty("logback.configurationFile", g.getRelativePath() + "/logback.xml");

		String dataFileName = prop.getProperty("TestDataSheet");
		File filePath = new File("./TestData/" + dataFileName);
		String testDataFile = filePath.toString();
		// String nameOfAVD = DataMiner.fngetconfigvalue(testDataFile, "Stage",
		// "NameOfAVD");
		// startEmulator(nameOfAVD);
		// Thread.sleep(100000);
	}

	@Parameters({ "ScenarioName" })
	@BeforeMethod()
	public void setUp(String ScenarioName) throws Exception {
		// st.execute("UPDATE tc_execution set STATUS=\"Inprogress\" WHERE req_id="+
		// NBORDERID + " AND tc_name='"+ScenarioName+"';");
		// st.execute("UPDATE req_summary SET Total="+TotalCases+" WHERE req_id="+
		// NBORDERID+";");

		// Return an Object of HashMap with All values stored from URL_information yml
		// file
		HashMap<String, Object> basicInfoDataset = setBasicInfomationForTestData("url_information");

		// Setting all the Required public variable with values from YML file for
		// Specific Environment
		Configuration.setUrlInfoDataSet(getUrlInfoDataset(basicInfoDataset, Configuration.getURL()));

		// file.setEmailFlag(false);
		// String Automessage=email.getExecutionStartMessage();
		// email.sendMessageWithAttachment("CTAF Automation Regression Test Run
		// Started",Automessage,"",false);

		iniliseBrowser();

	}

	@AfterMethod()
	public void tearDownWebDriver() throws Exception {
		closeWebdriver();
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception {
		/**************************
		 * Zip the Output generated HTML Files
		 ********************************/
		ZipDirectory zip = new ZipDirectory();
		File directoryToZip = new File(g.getResultFolderPath());
		List<File> fileList = new ArrayList<File>();
		System.out.println("---Getting references to all files in: " + directoryToZip.getCanonicalPath());
		zip.getAllFiles(directoryToZip, fileList);
		System.out.println("---Creating zip file");

		String zipOut = g.getResultFolderPath() + "//" + directoryToZip.getName() + ".zip";
		System.out.println(zipOut);
		zip.writeZipFile(directoryToZip, fileList, zipOut);
		System.out.println("---Done");

		// text.setEmailFlag(true);

		// file.setEmailFlag(true);
		zipOut = g.getResultFolderPath() + "//" + directoryToZip.getName() + ".zip";
		Report.createSummaryReportFooter();
		// Report.BotSummary();

		if (isProcessRunning("chromedriver.exe")) {
			System.out.println("Inside CMD KILL");
			Runtime.getRuntime().exec("taskkill /T /F /IM chromedriver.exe");
		}

		if (isProcessRunning("cmd.exe")) {
			System.out.println("Inside CMD KILL");
			Runtime.getRuntime().exec("taskkill /T /F /IM cmd.exe");
		}

		/**************************
		 * Send the mail with zip output HTML Files
		 ********************************/
		// email.sendMessageWithAttachment("CTAF Automated Regression Test Run
		// Completed",file.readEntireFile(g.getResultFolderPath()
		// +"//Index.html"),zipOut,true );
	}

	public void iniliseBrowser() throws FileNotFoundException, IOException, InterruptedException {
		System.out.println("==========" + g.getBrowser());
		openBrowser(g.getBrowser());
		// open("");
		// selenium = new WebDriverBackedSelenium(webDriver,Configuration.url);

	}

	@SuppressWarnings("deprecation")
	public void openBrowser(String browser) throws IOException, InterruptedException {
		String path = new File(".").getCanonicalPath();
		g.setRelativePath(path);

		InputStream inputA = new FileInputStream(g.getRelativePath() + "/configuration.properties");
		prop.load(inputA);

		String dataFileName = prop.getProperty("TestDataSheet");
		File path1 = new File("./TestData/" + dataFileName);
		String testDataFile = path1.toString();

		String Environment = prop.getProperty("Environment");

		String System = DataMiner.fngetconfigvalue(testDataFile, Environment, "System");
		try {

			switch (System) {
			case "Local":
				// String OS=DataMiner.fngetcolvalue(testDataFile, "Config_Sheet", "1", "1",
				// "Operating_System");
				String OS = DataMiner.fngetconfigvalue(testDataFile, Environment, "Operating_System");

				if (OS.equalsIgnoreCase("Windows")) {
					windowBrowser(browser);
				} else if (OS.equalsIgnoreCase("OS X")) {

				} else if (OS.equalsIgnoreCase("Android Real Device")) {
					localAndroidRealDevice(browser);
				} else if (OS.equalsIgnoreCase("Android Emulator Device")) {
					localAndroidEmulatorDevice(browser);
				} else if (OS.equalsIgnoreCase("iOS Real Device")) {
					// localiOSRealDevice(browser);
				} else if (OS.equalsIgnoreCase("iOS Emulator Device")) {

				}
				break;
			case "Browser stack":

				// String Br_OS=DataMiner.fngetcolvalue(testDataFile, "Config_Sheet", "1", "1",
				// "Operating_System");
				String Br_OS = DataMiner.fngetconfigvalue(testDataFile, Environment, "Operating_System");
				if (Br_OS.equalsIgnoreCase("Windows") || Br_OS.equalsIgnoreCase("OS X")) {
					BrowserStackWinMacOS(browser);
				} else if (Br_OS.equalsIgnoreCase("Android Real Device")
						|| Br_OS.equalsIgnoreCase("Android Emulator Device")) {
					BrowserStackAndroid(browser);
				} else if (Br_OS.equalsIgnoreCase("iOS Real Device") || Br_OS.equalsIgnoreCase("iOS Emulator Device")) {
					BrowserStackiOS(browser);
				}

				break;
			case "Remote":
				windowBrowser(browser);
				break;
			}

		} catch (Exception e) {
			// System.out.println("Could not obtain webdriver for browser \n" +
			// e.getMessage());
			Report.logToDashboard("Could not obtain webdriver for browser \n" + e.getMessage());
			ExtentTestManager.getTest().log(LogStatus.ERROR,
					"Could not obtain webdriver for browser \n" + e.getMessage());
		}
	}

	public void windowBrowser(String browser) throws MalformedURLException {
		if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", g.getRelativePath() + "\\Resources\\geckodriver.exe");

			File pathToBinary = new File("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");

			FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
			FirefoxProfile firefoxProfile = new FirefoxProfile();

			if (gridMode.equalsIgnoreCase("OFF")) {
				webDriver = new FirefoxDriver(ffBinary, firefoxProfile);
			} else {
				webDriver = new RemoteWebDriver(new URL(g.getHubAddress()), capability);

			}
			// webDriver.manage().window().maximize();
			WEB_DRIVER_THREAD_LOCAL.set(webDriver);
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().window().maximize();
		} else if (browser.equalsIgnoreCase("chrome")) {

			String driverPath = g.getRelativePath() + "\\Resources\\chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", driverPath);

			Map<String, Object> preferences = new HashMap<>();
			preferences.put("profile.default_content_settings.popups", 0);
			preferences.put("download.prompt_for_download", "false");
			preferences.put("download.default_directory", g.getRelativePath() + "\\TestData\\Downloads");

			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", preferences);

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setBrowserName("chrome");
			capabilities.setPlatform(Platform.WINDOWS);

			if (gridMode.equalsIgnoreCase("OFF")) {
				webDriver = new ChromeDriver(capabilities);
			} else {
				RemoteWebDriver remoDriver = new RemoteWebDriver(new URL(g.getHubAddress()), capabilities);
				// RemoteWebDriver remoDriver= new RemoteWebDriver(new
				// URL("http://localhost:5555/wd/hub"), capabilities);
				webDriver = remoDriver;

			} 

			WEB_DRIVER_THREAD_LOCAL.set(webDriver);
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().window().maximize();
		} else if (browser.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", g.getRelativePath() + "//Resources//IEDriverServer.exe");
			capability = DesiredCapabilities.internetExplorer();
			capability.setBrowserName("IE");
			capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capability.setPlatform(Platform.WINDOWS);
			capability.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
			capability.acceptInsecureCerts();
			// capability.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL,
			// Configuration.getConfig("My URL"));
			capability.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			if (gridMode.equalsIgnoreCase("OFF")) {
				webDriver = new InternetExplorerDriver(capability);
			} else {
				RemoteWebDriver RemWebDrive = new RemoteWebDriver(new URL(g.getHubAddress()), capability);
				webDriver = RemWebDrive;
			}

			WEB_DRIVER_THREAD_LOCAL.set(webDriver);
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().window().maximize();
		} else if (browser.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver", g.getRelativePath() + "\\Resources\\msedgedriver.exe");

			if (gridMode.equalsIgnoreCase("OFF")) {
				webDriver = new EdgeDriver();
			} else {

				RemoteWebDriver RemWebDrive = new RemoteWebDriver(new URL(g.getHubAddress()), capability);
				webDriver = RemWebDrive;
			}

			WEB_DRIVER_THREAD_LOCAL.set(webDriver);
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().window().maximize();
		} else if (browser.equalsIgnoreCase("safari")) {
			capability = DesiredCapabilities.safari();
			capability.setBrowserName("safari");
			capability.setJavascriptEnabled(true);
			capability.setPlatform(org.openqa.selenium.Platform.ANY);
			capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			webDriver = new SafariDriver(capability);

			WEB_DRIVER_THREAD_LOCAL.set(webDriver);
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().window().maximize();
		}

		else if (browser.equalsIgnoreCase("opera")) {
			capability = DesiredCapabilities.opera();
			capability.setCapability("opera.port", "-1");
			capability.setCapability("opera.profile", "");
			webDriver = new OperaDriver(capability);

			WEB_DRIVER_THREAD_LOCAL.set(webDriver);
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().window().maximize();
		}
	}

	public void BrowserStackWinMacOS(String browser) throws IOException, InterruptedException {
		String dataFileName = prop.getProperty("TestDataSheet");
		File path = new File("./TestData/" + dataFileName);
		String testDataFile = path.toString();

		String AUTOMATE_USERNAME = DataMiner.fngetconfigvalue(testDataFile, "Stage", "AUTOMATE_USERNAME");
		String AUTOMATE_ACCESS_KEY = DataMiner.fngetconfigvalue(testDataFile, "Stage", "AUTOMATE_ACCESS_KEY");
		String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

		// String BrowserName=DataMiner.fngetcolvalue(testDataFile, "BrowserStack", "1",
		// "31", "BrowserName");
		String OS = DataMiner.fngetconfigvalue(testDataFile, "Stage", "Operating_System");
		String RealMobile = DataMiner.fngetconfigvalue(testDataFile, "Stage", "RealMobile");
		String OS_Version = DataMiner.fngetconfigvalue(testDataFile, "Stage", "OS_Version");

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browser", browser);
		// caps.setCapability("device", DeviceName);
		caps.setCapability("realMobile", RealMobile);
		caps.setCapability("os_version", OS_Version);
		caps.setCapability("resolution", "1920x1080");
		caps.setCapability("os", OS);
		caps.setCapability("browser_version", "latest");
		// caps.setCapability("name", browserStackSCName); // test name
		caps.setCapability("build", "BStack Build Number 1"); // CI/CD job or build name
		caps.setCapability("browserstack.idleTimeout", "250");
		caps.setCapability("browserstack.console", "info");
		HashMap<String, Boolean> networkLogsOptions = new HashMap<>();
		caps.setCapability("browserstack.networkLogs", "true");
		caps.setCapability("browserstack.networkLogsOptions", networkLogsOptions);

		RemoteWebDriver RemWebDrive = new RemoteWebDriver(new URL(URL), caps);
		webDriver = RemWebDrive;
		WEB_DRIVER_THREAD_LOCAL.set(webDriver);
	}

	public void BrowserStackAndroid(String browser) throws IOException, InterruptedException {
		String dataFileName = prop.getProperty("TestDataSheet");
		File path = new File("./TestData/" + dataFileName);
		String testDataFile = path.toString();

		String AUTOMATE_USERNAME = DataMiner.fngetconfigvalue(testDataFile, "Stage", "AUTOMATE_USERNAME");
		String AUTOMATE_ACCESS_KEY = DataMiner.fngetconfigvalue(testDataFile, "Stage", "AUTOMATE_ACCESS_KEY");
		String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

		String DeviceName = DataMiner.fngetconfigvalue(testDataFile, "Stage", "DeviceName");
		String RealMobile = DataMiner.fngetconfigvalue(testDataFile, "Stage", "RealMobile");
		String OS_Version = DataMiner.fngetconfigvalue(testDataFile, "Stage", "OS_Version");

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browserName", browser);
		caps.setCapability("device", DeviceName);
		caps.setCapability("realMobile", RealMobile);
		caps.setCapability("os_version", OS_Version);
		// caps.setCapability("name", browserStackSCName); // test name
		caps.setCapability("build", "BStack Build Number 1"); // CI/CD job or build name
		caps.setCapability("browserstack.idleTimeout", "250");
		caps.setCapability("browserstack.console", "info");
		HashMap<String, Boolean> networkLogsOptions = new HashMap<>();
		caps.setCapability("browserstack.networkLogs", "true");
		caps.setCapability("browserstack.networkLogsOptions", networkLogsOptions);

		RemoteWebDriver RemWebDrive = new RemoteWebDriver(new URL(URL), caps);
		webDriver = RemWebDrive;
		WEB_DRIVER_THREAD_LOCAL.set(webDriver);
	}

	public void BrowserStackiOS(String browser) throws IOException, InterruptedException {
		String dataFileName = prop.getProperty("TestDataSheet");
		File path = new File("./TestData/" + dataFileName);
		String testDataFile = path.toString();

		String AUTOMATE_USERNAME = DataMiner.fngetconfigvalue(testDataFile, "Stage", "AUTOMATE_USERNAME");
		String AUTOMATE_ACCESS_KEY = DataMiner.fngetconfigvalue(testDataFile, "Stage", "AUTOMATE_ACCESS_KEY");
		String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

		// String BrowserName=DataMiner.fngetcolvalue(testDataFile, "BrowserStack", "1",
		// "2", "BrowserName");
		String DeviceName = DataMiner.fngetconfigvalue(testDataFile, "Stage", "DeviceName");
		String RealMobile = DataMiner.fngetconfigvalue(testDataFile, "Stage", "RealMobile");
		String OS_Version = DataMiner.fngetconfigvalue(testDataFile, "Stage", "OS_Version");

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browserName", browser);
		caps.setCapability("device", DeviceName);
		caps.setCapability("realMobile", RealMobile);
		caps.setCapability("os_version", OS_Version);
		// caps.setCapability("name", browserStackSCName); // test name
		caps.setCapability("build", "BStack Build Number 1"); // CI/CD job or build name
		caps.setCapability("browserstack.idleTimeout", "250");
		caps.setCapability("browserstack.console", "info");
		HashMap<String, Boolean> networkLogsOptions = new HashMap<>();
		caps.setCapability("browserstack.networkLogs", "true");
		caps.setCapability("browserstack.networkLogsOptions", networkLogsOptions);
		caps.setCapability("browserstack.preventCrossSiteTracking", "true");
		RemoteWebDriver RemWebDrive = new RemoteWebDriver(new URL(URL), caps);
		webDriver = RemWebDrive;
		WEB_DRIVER_THREAD_LOCAL.set(webDriver);
	}

	public void localAndroidRealDevice(String browser) throws IOException, InterruptedException {
		startAppiumServer();
		String dataFileName = prop.getProperty("TestDataSheet");
		File path = new File("./TestData/" + dataFileName);
		String testDataFile = path.toString();

		String DeviceName = DataMiner.fngetconfigvalue(testDataFile, "Stage", "DeviceName");
		String OS_Version = DataMiner.fngetconfigvalue(testDataFile, "Stage", "OS_Version");

		DesiredCapabilities capabilities = DesiredCapabilities.android();
		capabilities.setCapability("deviceName", DeviceName);
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		capabilities.setCapability(CapabilityType.VERSION, OS_Version);
		capabilities.setCapability("noReset", "true");
		capabilities.setCapability("deviceReadyTimeout", "5");
		// webDriver= new RemoteWebDriver(new URL("http://0.0.0.0:4723/wd/hub"),
		// capabilities);
		System.out.println(service.getUrl());
		URL serverUrl = service.getUrl();
		webDriver = new RemoteWebDriver(serverUrl, capabilities);
		WEB_DRIVER_THREAD_LOCAL.set(webDriver);
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
	}

	public void localAndroidEmulatorDevice(String browser) throws IOException, InterruptedException {

		startAppuimServerByWindows();
		// startAppiumServer();
		String dataFileName = prop.getProperty("TestDataSheet");
		File path = new File("./TestData/" + dataFileName);
		String testDataFile = path.toString();

		String DeviceName = DataMiner.fngetconfigvalue(testDataFile, "Stage", "DeviceName");
		String OS_Version = DataMiner.fngetconfigvalue(testDataFile, "Stage", "OS_Version");

		System.out.println("DeviceName------------" + DeviceName);
		System.out.println("OS_Version------------" + OS_Version);

		DesiredCapabilities capabilities = DesiredCapabilities.android();
		capabilities.setCapability("deviceName", DeviceName);
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
		capabilities.setCapability(CapabilityType.VERSION, OS_Version);
		capabilities.setCapability("noReset", "true");
		capabilities.setCapability("automationName", "uiautomator2");
		capabilities.setCapability("adbExecTimeout", 50000);
		// capabilities.setCapability("deviceReadyTimeout", "200");
		// webDriver= new RemoteWebDriver(new URL("http://0.0.0.0:4723/wd/hub"),
		// capabilities);
		System.out.println("============================" + service.getUrl());
		URL serverUrl = service.getUrl();
		webDriver = new RemoteWebDriver(serverUrl, capabilities);
		WEB_DRIVER_THREAD_LOCAL.set(webDriver);
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
	}

	AppiumDriverLocalService service;

	public void startAppiumServer() {

		AppiumServiceBuilder builder = new AppiumServiceBuilder();
		builder.usingAnyFreePort();

		HashMap<String, String> environment = new HashMap();
		environment.put("PATH", "/usr/local/bin:" + System.getenv("PATH"));
		builder.withEnvironment(environment);

		service = AppiumDriverLocalService.buildService(builder.withIPAddress("0.0.0.0")
				// .usingPort(4728));
				.usingAnyFreePort());
		service.start();

		// String Node = "C:/Program Files/nodejs/node.exe";
		// String AppiumMainPath =
		// "C:/Users/user/AppData/Roaming/npm/node_modules/appium/build/lib/main.js";

		/*
		 * service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
		 * .usingDriverExecutable(new File(Node)) .withAppiumJS(new
		 * File(AppiumMainPath)) .withIPAddress("0.0.0.0") //.usingPort(4728));
		 * .usingAnyFreePort()); service.start();
		 */

	}

	public void startAppuimServerByWindows() throws InterruptedException {
		String Node = "C:\\Program Files\\nodejs\\node.exe";
		String AppiumMainPath = "C:\\Program Files\\Appium Server GUI\\resources\\app\\node_modules\\appium\\build\\lib\\main.js";
		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

		service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().usingDriverExecutable(new File(Node))
				.withAppiumJS(new File(AppiumMainPath)).withIPAddress("0.0.0.0")
				// .usingPort(4723));
				.usingAnyFreePort());
		// .usingAnyFreePort().withArgument(() ->
		// "--allow-insecure","chromedriver_autodownload"));
		// .withLogFile(new File("E:\\Appuim\\AppiumLog.txt")) );
		service.start();

		Thread.sleep(10000);

		/*
		 * Build the Appium service CommandLine cmd = new
		 * CommandLine("C:\\Program Files\\nodejs\\node.exe"); cmd.
		 * addArgument("C:\\Program Files\\Appium Server GUI\\resources\\app\\node_modules\\appium\\build\\lib\\appium.js"
		 * ); cmd.addArgument("--address"); cmd.addArgument("127.0.0.1");
		 * cmd.addArgument("--port"); cmd.addArgument("4723");
		 * 
		 * DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
		 * DefaultExecutor executor = new DefaultExecutor(); executor.setExitValue(1);
		 * try { executor.execute(cmd, handler); Thread.sleep(10000);
		 * System.out.println("appuim server start"); } catch (IOException |
		 * InterruptedException e) { e.printStackTrace(); }
		 */
	}

	public void closeWebdriver() {
		if (webDriver == null) {

			return;
		}
		quit();
		// service.stop();
	}

	public void startEmulator(String nameOfAVD) {
		/*
		 * String sdkPath = "C:\\Users\\User\\android-sdks\\"; String adbPath = sdkPath
		 * + "platform-tools" + File.separator + "adb"; String emulatorPath = sdkPath +
		 * "tools" + File.separator + "emulator";
		 * 
		 * 
		 * 
		 * 
		 * //String test = emulatorPath + " -avd " +nameOfAVD;
		 * System.out.println("****************"+emulatorPath+"******************");
		 * 
		 * System.out.println("Starting emulator for '" + nameOfAVD + "' ..."); String[]
		 * aCommand = new String[] { emulatorPath, "-avd", nameOfAVD };
		 */
		try {
			Process process = Runtime.getRuntime().exec("cmd /c start cmd.exe /K emulator -avd " + nameOfAVD);
			Thread.sleep(50000);
			System.out.println("Emulator launched successfully!");

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void info(String message) {
		write.println(message);
		// System.out.println(message);
	}

	public WebDriver getWebDriver() {

		return SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get();
	}

	public void setWebDriver(WebDriver webDriver) {
		SeleniumUtils.webDriver = webDriver;
	}

	/**
	 * This method will open the url given in param
	 * 
	 * @param url
	 * @throws Exception
	 */
	/*
	 * public void open(String url) { try { if(url.endsWith("/")) {
	 * SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().get(Configuration.C4C_URL+url); }
	 * else {
	 * SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().get(Configuration.C4C_URL+url); }
	 * setImplicitWaitTimeout(50000);
	 * ExtentTestManager.getTest().log(LogStatus.PASS, "Launched application: " +
	 * Configuration.C4C_URL); Report.logToDashboard("Launched application: " +
	 * Configuration.C4C_URL); } catch(RuntimeException e) { //throw new
	 * Exception("WebDriver unable to handle the request to open url [url='" + url +
	 * "']: "+e.getMessage()); }
	 * 
	 * }
	 */
	public static void openurl(String url) {

		try {
			// SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().deleteAllCookies();
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().navigate().to(url);
			// SeleniumUtils.webDriver.navigate().to(url);
			ExtentTestManager.getTest().log(LogStatus.PASS, "Launched application: " + url);
			Report.logToDashboard("Launched application: " + url);
		} catch (RuntimeException e) {
			// throw new Exception("WebDriver unable to handle the request to open url
			// [url='" + url + "']: "+e.getMessage());
			ExtentTestManager.getTest().log(LogStatus.ERROR, "Runtime exception in launching application: " + url);
			Report.logToDashboard("Runtime exception in launching application: " + url);
		}
	}

	public void goBack() {
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().navigate().back();
		ExtentTestManager.getTest().log(LogStatus.INFO, "Browser back action performed");
		Report.logToDashboard("Browser back action performed");
	}

	public void refreshPage() {
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().navigate().refresh();
	}

	public String getTitleOfAnotherWindow(String currentHandle) {
		for (String handle : SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getWindowHandles()) {

			if (!currentHandle.equals(handle)) {
				return SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().window(handle).getTitle();
			}
		}
		return currentHandle;
	}

	public void close() {
		try {
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().close();
		} catch (RuntimeException e) {
			// throw new Exception("WebDriver unable to handle the request to close :
			// "+e.getMessage());
		}
	}

	/**
	 * @throws Exception
	 * @see #quit()
	 */
	public void quit() {

		if (webDriver == null) {

			return;
		}

		try {
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().quit();
		} catch (RuntimeException e) {
			// throw new Exception("WebDriver unable to handle the request to close
			// browser!"+e.getMessage());
		}
	}

	/**
	 * @see Browser#setImplicitWaitTimeout(int)
	 */
	public void setImplicitWaitTimeout(long milliseconds) {
		if (milliseconds > 0) {
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().timeouts().implicitlyWait(milliseconds,
					TimeUnit.MILLISECONDS);
		} else {
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		}
	}

	/**
	 * MouseHover on specified locator Example
	 * 
	 * @param locator
	 * @throws IOException
	 * @throws Exception
	 */
	public void mouseOverAction(String locator) throws IOException {
		WebElement element = findWebElement(locator);

		new Actions(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()).moveToElement(element).build().perform();
		// log.debug("Move mouse on locator [element='"+element+"']");
		// info("Move mouse on locator [locator='"+locator+"']");
		// log.trace("Exiting method mouseOver");
	}

	public void mouseOver(String locator) throws IOException {
		try {
			WebElement element = findWebElement(locator);
			// Locatable hoverItem = (Locatable) element;
			Report.LogInfo("MouseOver", "Mouse over on " + locator + " is done Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, "Mouse over on " + locator + " is done Successfully");

		} catch (Exception e) {
			Report.LogInfo("MouseOver", locator + " Is not Present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, locator + " Is not Present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + " Is not Present on Screen");
		}
	}

	/**
	 * click on specified locator Example
	 * 
	 * @param locator
	 * @throws IOException
	 * @throws Exception
	 */
	public void click(String locator) throws IOException {
		try

		{
			WebElement element = findWebElement(locator);
			if (g.getBrowser().trim().equalsIgnoreCase("edge")) {
				((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
						.executeScript("arguments[0].click();", element);

			} else {
				element.click();
			}
			locator = element.getAttribute("id");
			Report.LogInfo("Click", "\"" + locator + "\" Is Clicked Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, locator + " Is Clicked Successfully");
		} catch (Exception e) {
			Report.LogInfo("Click", "<font color=red>" + locator + " Is not Present on Screen</font>", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, locator + " Is not Present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
		}
	}

	public void javaScriptclick(String locator) throws IOException {
		try {
			WebElement element = findWebElement(locator);
			if (g.getBrowser().equalsIgnoreCase("firefox")) {
				element.click();
			} else {
				((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
						.executeScript("arguments[0].click();", element);
			}

			Report.LogInfo("Click", "\"" + locator + "\" Is Clicked Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, locator + " Is Clicked Successfully");
		} catch (Exception e) {
			Report.LogInfo("Click", locator + " Is not Present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, locator + " Is not Present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + " Is not Present on Screen");
		}
	}

	public void javaScriptclick(String locator, String Object) throws IOException {

		try {
			WebElement element = findWebElement(locator);
			if (g.getBrowser().equalsIgnoreCase("firefox")) {
				element.click();
			} else if (g.getBrowser().equalsIgnoreCase("chrome")) {
				element.click();
			} else {
				((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
						.executeScript("arguments[0].click();", element);
			}

			Report.LogInfo("Click", "\"" + Object + "\" Is Clicked Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, Object + " Is Clicked Successfully");
		} catch (Exception e) {
			Report.LogInfo("Click", Object + " Is not Present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, Object + " Is not Present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(Object + " Is not Present on Screen");
		}
	}

	public void javaScriptclick1(String locator, String Object) throws IOException {

		try {
			WebElement element = findWebElement(locator);
			((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()).executeScript("arguments[0].click();",
					element);
			Report.LogInfo("Click", "\"" + Object + "\" Is Clicked Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, Object + " Is Clicked Successfully");
		} catch (Exception e) {
			Report.LogInfo("Click", Object + " Is not Present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, Object + " Is not Present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + " Is not Present on Screen");
			throw new SkipException("<b><i>" + locator + "</i></b> Element not found so skipping this exception");

		}
	}

	public void javaScriptClickWE(WebElement locator, String Object) throws IOException {

		try {
			// WebElement element = findWebElement(locator);
			((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()).executeScript("arguments[0].click();",
					locator);
			Report.LogInfo("Click", "\"" + locator + "\" Is Clicked Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, Object + " Is Clicked Successfully");
		} catch (Exception e) {
			Report.LogInfo("Click", locator + " Is not Present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, Object + " Is not Present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + " Is not Present on Screen");
			throw new SkipException("<b><i>" + locator + "</i></b> Element not found so skipping this exception");

		}
	}

	static String sException = null;
	static int j = 0;

	public void scrollToViewNClick(String locator, String Object) throws IOException {
		WebElement element = findWebElement(locator);

		try {
			element.click();

		} catch (Exception e1) {
			for (int i = 0; i <= 10; ++i) {
				try {
					Thread.sleep(3000);
					waitToElementVisible(locator);
					scrollIntoView(element);
					Thread.sleep(250);
					element.click();
					break;
				} catch (Exception e) {
					sException = e.toString();
					j = i + 1;
					continue;
				}
			}
		}
		if (j > 10) {

		}

	}

	public void ScrollIntoViewByString(String locator) throws IOException {
		WebElement element = findWebElement(locator);
		scrollIntoView(element);
	}

	public static boolean scrollIntoView(WebElement webElement) {
		boolean status = false;

		try {
			String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
					+ "var elementTop = arguments[0].getBoundingClientRect().top;"
					+ "window.scrollBy(0, elementTop-(viewPortHeight/2));";
			((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()).executeScript(scrollElementIntoMiddle,
					webElement);
			isElementEnabled(webElement);
			Thread.sleep(250);
			status = true;
		} catch (Exception e) {

		}

		return status;
	}

	public void click(String locator, String ObjectName) throws IOException {
		try {
			WebElement element = findWebElement(locator);
			WebDriverWait wait = new WebDriverWait(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get(), 30);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			if (g.getBrowser().trim().equalsIgnoreCase("EDGE")) {
				JavascriptExecutor js = (JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get();
				((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
						.executeScript("arguments[0].click();", element);
				Report.LogInfo("click", "<b><i>" + ObjectName + "</i></b> Is Clicked Successfully", "INFO");
				ExtentTestManager.getTest().log(LogStatus.INFO, ObjectName + " Is Clicked Successfully");
			} else {

				try {
					element.click();

				} catch (Exception e1) {
					for (int i = 0; i <= 10; ++i) {
						try {
							Thread.sleep(3000);
							waitForElementToBeVisible(locator, 5);
							scrollIntoView(element);
							Thread.sleep(250);
							element.click();
							break;
						} catch (Exception e) {
							sException = e.toString();
							j = i + 1;
							continue;
						}
					}
				}
				Report.LogInfo("click", "<b><i>" + ObjectName + "</i></b> Is Clicked Successfully", "INFO");
				ExtentTestManager.getTest().log(LogStatus.INFO, ObjectName + " Is Clicked Successfully");
			}

		} catch (Exception e) {
			Report.LogInfo("click", "<font color=red" + ObjectName + " Is not Present on Screen</font>", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.ERROR,
					ObjectName + " Element not found so skipping this exception");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			throw new SkipException("<b><i>" + ObjectName + "</i></b> Element not found so skipping this exception");

		}
	}

	public boolean isClickable(String locator) {
		try {
			WebElement element = findWebElement(locator);

			if (element.isDisplayed() && element.isEnabled()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	public void clickAndWait(String locator) throws IOException {
		try {
			WebElement element = findWebElement(locator);
			element.click();
			javaScriptWait();
			Report.LogInfo("click", locator + " Is Clicked Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, locator + " Is Clicked Successfully");
		} catch (Exception e) {
			Report.LogInfo("Exception", "Exception in clickAndWait " + e.getMessage(), "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, "Exception in clickAndWait " + e.getMessage());
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail("Exception in clickAndWait " + e.getMessage());
		}
	}

	/**
	 * doubleClick on specified locator Example
	 * 
	 * @param locator
	 * @throws IOException
	 * @throws Exception
	 */
	public void doubleClick(String locator) throws IOException {
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().timeouts().setScriptTimeout(waitForWebElementTimeOut,
				TimeUnit.MILLISECONDS);
		// log.trace("Entering method doubleClick [locator="+locator+"]");
		Report.LogInfo("dobuleClick", locator + " Is Double Clicked Successfully", "INFO");
		ExtentTestManager.getTest().log(LogStatus.INFO, locator + " Is Double Clicked Successfully");
		WebElement element = findWebElement(locator);

		// log.debug("Found element for locator [element='"+element+"']");
		new Actions(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()).moveToElement(element).doubleClick();
		// info("Click on locator [locator='"+locator+"']");
		// log.trace("Exiting method doubleClick");
	}

	/**
	 * submit on specified locator Example
	 * 
	 * @param locator
	 * @throws IOException
	 * @throws Exception
	 */
	public void submit(String locator) throws IOException {
		try {
			WebElement element = findWebElement(locator);
			element.submit();
			Report.LogInfo("submit", locator + " Is Submitted Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, locator + " Is Submitted Successfully");

		} catch (Exception e) {
			Report.LogInfo("submit", locator + " Is not Submitted", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, locator + " Is not Submitted");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + " Is not Submitted");
		}
	}

	public void submitAndWait(String locator) throws IOException {
		try {
			WebElement element = findWebElement(locator);
			element.submit();
			javaScriptWait();
			Report.LogInfo("submit", locator + " Is Submitted Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, locator + " Is Submitted Successfully");

		} catch (Exception e) {
			Report.LogInfo("submit", locator + " Is not Submitted Successfully", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, locator + " Is not Submitted");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + " Is not Submitted Successfully");
		}
	}

	/**
	 * Select one option from a single select drop down list Single Selection:
	 * Select option
	 * 
	 * @param locator (@id=option) && xpath
	 * @param field   (@id)
	 * @throws IOException
	 * @throws Exception
	 */
	public void select(String locator, String field) throws IOException {
		try { 
			Select element = new Select(findWebElement(locator));
			selectValue(element, field);
			Report.LogInfo("dropdown", locator + " Is Selected Successfully with option " + field, "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, locator + " Is Selected Successfully with option " + field);
		} catch (Exception e) {
			Report.LogInfo("dropdown", locator + " Is not Selected Successfully with option " + field, "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					locator + " Is not Selected Successfully with option " + field);
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + " Is not Selected Successfully with option " + field);
		}
	}

	public void selectByIndex(String locator, int index, String ObjectName) throws IOException {
		try {
			Select element = new Select(findWebElement(locator));
			element.selectByIndex(index);
			Report.LogInfo("dropDown", index + " item is Selected in " + ObjectName + " successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					index + " item is Selected in " + ObjectName + " successfully");

		} catch (Exception e) {
			Report.LogInfo("dropDown", ObjectName + " Not present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, ObjectName + " Not present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(ObjectName + " Not present on Screen");
		}
	}

	public void selectByValue(String locator, String field, String ObjectName) throws IOException {
		try {
			Select element = new Select(findWebElement(locator));
			element.selectByValue(field);
			Report.LogInfo("dropdown", ObjectName + " Is Selected Successfully with option " + field, "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ObjectName + " Is Selected Successfully with option " + field);
		} catch (Exception e) {
			Report.LogInfo("dropdown", ObjectName + " Is not Selected Successfully with option " + field, "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					ObjectName + " Is not Selected Successfully with option " + field);
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(ObjectName + " Is not Selected Successfully with option " + field);
		}
	}

	public void selectByVisibleText(String locator, String field, String ObjectName) throws IOException {
		try {
			Select element = new Select(findWebElement(locator));
			element.selectByVisibleText(field);
			Report.LogInfo("dropdown", ObjectName + " Is Selected Successfully with option " + field, "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ObjectName + " Is Selected Successfully with option " + field);
		} catch (Exception e) {
			Report.LogInfo("dropdown", ObjectName + " Is not Selected Successfully with option " + field, "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					ObjectName + " Is not Selected Successfully with option " + field);
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(ObjectName + " Is not Selected Successfully with option " + field);
		}
	}

	/**
	 * @param singleSelect
	 * @param field
	 * @throws IOException
	 * @throws Exception
	 */
	protected void selectValue(Select singleSelect, String field) throws IOException {
		try {
			if (field.startsWith("@value")) {
				String value = removeStart(field, "@value=");
				if (!isBlankOrNull(value)) {
					singleSelect.selectByValue(removeStart(field, "@value="));
					Report.LogInfo("SelectOptionByValue", field + "  value is selected in dropdown", "INFO");
					ExtentTestManager.getTest().log(LogStatus.INFO, field + "  value is selected in dropdown");
				}
			} else if (field.startsWith("@index")) {
				String index = removeStart(field, "@index=");
				if (!isBlankOrNull(index)) {
					singleSelect.selectByIndex(Integer.parseInt(index));
					Report.LogInfo("SelectOptionByIndex", field + " index is selected in dropdown", "INFO");
					ExtentTestManager.getTest().log(LogStatus.INFO, field + " index is selected in dropdown");
				}
			} else if (field.startsWith("@visibleText")) {
				String text = removeStart(field, "@visibleText=");
				if (!isBlankOrNull(text)) {
					singleSelect.selectByVisibleText(removeStart(field, "@visibleText="));
					Report.LogInfo("SelectOptionByVisibleText", field + " visible text is selected in dropdown",
							"INFO");
					ExtentTestManager.getTest().log(LogStatus.INFO, field + " visible text is selected in dropdown");
				}
			} else {
				// throw new Exception("Invalid format of the field [field='" + field+"']");
			}
		} catch (Exception e) {
			Report.LogInfo("SelectValue", field + "is not selected in " + singleSelect, "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, field + "is not selected in " + singleSelect);
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(field + "is not selected in " + singleSelect);
		}
		// log.trace("Exiting method selectValue");
	}

	/**
	 * @throws IOException
	 * @throws Exception
	 * @see selectFromMultipleSelect
	 */
	public void selectMultiple(String locator, String values) throws IOException {
		// log.trace("Entering method selectMultiple with parameters
		// [locator='"+locator+"', values='"+values+"']");
		if (isBlankOrNull(locator)
				|| (!locator.startsWith("/") && !locator.startsWith("@name=") && !locator.startsWith("@id="))) {
		}
		Select element = new Select(findWebElement(locator));
		if (isValueEmpty(values)) {
			element.deselectAll();
			return;
		}
		String[] args = values.split(",");
		for (String field : args) {
			selectValue(element, field);
		}
	}

	/**
	 * @param locator
	 * @param value
	 * @throws IOException
	 * @throws Exception
	 */
	public void selectRadio(String locator, String value) throws IOException {

		if (isBlankOrNull(locator) || (!locator.startsWith("@xpath=") && !locator.startsWith("@name="))) {
			// throw new Exception("Invalid locator format [locator='"+locator+"']");
		}

		List<WebElement> webElements = findWebElements(locator);
		if (value.startsWith("@id=")) {
			String id = removeStart(value, "@id=");
			for (WebElement element : webElements) {
				if (id.equals(element.getAttribute("id"))) {
					element.click();
					// log.debug("Element to select identified with id [value='"+value+"']");
				}
			}
		} else if (value.startsWith("@value=")) {
			String id = removeStart(value, "@value=");
			for (WebElement element : webElements) {
				if (id.equals(element.getAttribute("value"))) {
					element.click();
					// log.debug("Element to select identified with value [value='"+value+"']");
				}
			}
		} else {
			// throw new Exception("Invalid value format [value='"+value+"']");
		}
	}

	/**
	 * @param locator
	 * @param value
	 * @throws IOException
	 * @throws Exception
	 */
	public void check(String locator, String value) throws IOException {
		// log.trace("Entering method check [locator='"+locator+"',
		// value='"+value+"']");
		clickCheckBox(locator, value, true);
		// info("Checked checkbox [locator='"+locator+"', value='"+value+"']");
		// log.trace("Exiting method check");
	}

	public void checkbox(String locator, Keys key) throws IOException {

		if (verifyExists(locator)) {
			findWebElement(locator).sendKeys(key);
			Report.LogInfo("isChecked", locator, "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, locator + " is checked.");
		} else {
			Report.LogInfo("isChecked", locator + "is not found", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, locator + " is not found");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + "is not found");
		}

	}

	/**
	 * @param locator
	 * @param value
	 * @throws IOException
	 * @throws Exception
	 */
	public boolean isSelected(String locator, String infoMessage) throws IOException {

		Report.LogInfo("isChecked", infoMessage, "Info");

		if (verifyExists(locator)) {
			return (findWebElement(locator).isSelected());
		} else

		{
			Report.LogInfo("isChecked", locator + "is not found", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, locator + "is not found");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + "is not found");
			return false;
		}

	}

	/**
	 * @param locator
	 * @param value
	 * @throws IOException
	 * @throws Exception
	 */
	public void uncheck(String locator, String value) throws IOException {
		clickCheckBox(locator, value, false);

	}

	/**
	 * @param isChecked
	 * @param element
	 * @return
	 */
	protected boolean canClickElement(boolean isCheckCommand, WebElement element) {
		boolean canClick = (isCheckCommand && !element.isSelected()) || (!isCheckCommand && element.isSelected());
		return canClick;
	}

	protected void clickCheckBox(String locator, String value, boolean isCheckCommand) throws IOException {
		if (isBlankOrNull(locator) || (!locator.startsWith("/") && !locator.startsWith("@name="))) {
			// throw new Exception("Invalid locator format [locator='"+locator+"']");
		}

		List<WebElement> webElements = findWebElements(locator);
		if ("all".equalsIgnoreCase(value)) {
			for (WebElement element : webElements) {
				if (canClickElement(isCheckCommand, element)) {
					// log.debug("Checkbox clicked [value='"+value+"']");
					element.click();
				}
			}
		} else if (value.startsWith("@id=")) {
			String id = removeStart(value, "@id=");
			for (WebElement element : webElements) {
				if (id.equals(element.getAttribute("id")) && canClickElement(isCheckCommand, element)) {
					// log.debug("Checkbox clicked [value='"+value+"']");
					element.click();
					break;
				}
			}
		} else if (value.startsWith("@value=")) {
			String valueToSet = removeStart(value, "@value=");
			for (WebElement element : webElements) {
				if (valueToSet.equals(element.getAttribute("value")) && canClickElement(isCheckCommand, element)) {
					// log.debug("Checkbox clicked [value='"+value+"']");
					element.click();
					break;
				}
			}
		} else {
			// throw new Exception("Invalid Value format [value='"+value+"']");
		}
	}

	/**
	 * The method returns true, if the value just has the empty keyword.
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	protected boolean isValueEmpty(String value) {
		if (StringUtils.containsIgnoreCase(value, EMPTY_KEYWORD)) {
			if (EMPTY_KEYWORD.equalsIgnoreCase(value.trim())) {
				return true;
			} else {
				// throw new Exception("Invalid empty value format! [value='" + value + "']");
			}
		}
		return false;
	}

	/**
	 * The method replaces ${space} with " " and returns the string. The end result
	 * after processing should be all spaces if not, throws an Exception.
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	protected String processSpaceValues(String value) {
		if (StringUtils.containsIgnoreCase(value, SPACE_KEYWORD)) {
			value = value.replace(SPACE_KEYWORD, " ");
			value = value.replace(SPACE_KEYWORD.toUpperCase(), " ");
			if ("".equals(value.trim())) {
				// info("Set the value in text box as [value='"+value+"']");
				// log.trace("Exit method processSpaceValues");
				return value;
			} else {
				// throw new Exception("Invalid space value format! [value='" + value + "']");
			}
		}
		return value;
	}

	/**
	 * Verify if the page contains specified element
	 * 
	 * @param elementLocator
	 * @Modified on
	 * @Modified By
	 */
	public boolean verifyExists(String locator) {
		if (!isBlankOrNull(locator)) {
			try {
				setImplicitWaitTimeout(2);
				WebElement element = findWebElement(locator);
				setImplicitWaitTimeout(50000);
				if (element != null) {
					// Report.LogInfo("verifyExists","verify locator \""+locator+"\" is present",
					// "PASS");
					return true;
				} else {
					// Report.LogInfo("verifyExists","verify locator \""+locator+"\" is not
					// present", "FAIL");
					// softAssert.fail("verify locator \""+locator+"\" is not present");
					return false;
				}

			} catch (Exception e) {

				setImplicitWaitTimeout(50000);
				// Report.LogInfo("verifyExists","verify locator \""+locator+"\" is not
				// present", "FAIL");
				// softAssert.fail("verify locator \""+locator+"\" is not present");
				return false;
			}
		} else {

			Report.LogInfo("verifyExists", "verify locator \"" + locator + "\" is not present", "INFO");
			// ExtentTestManager.getTest().log(LogStatus.INFO, "verify locator
			// \""+locator+"\" is not present", "INFO");
			return false;
		}

	}

	public boolean verifyExists(String locator, String Object) throws InterruptedException, IOException {
		if (g.getBrowser().equalsIgnoreCase("edge") || g.getBrowser().equalsIgnoreCase("firefox")) {
			waitForElementToAppear(locator, 5);
		}
		if (!isBlankOrNull(locator)) {
			try {
				waitForElementToAppear(locator, 15);
				WebElement element = findWebElement(locator);
				WebDriverWait wait = new WebDriverWait(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get(), 30);
				wait.until(ExpectedConditions.visibilityOf(element));
				wait.until(ExpectedConditions.elementToBeClickable(element));
				if (element != null) {
					Report.LogInfo("verifyExists", "<b><i>" + Object + "<i></b> is present on screen", "PASS");
					ExtentTestManager.getTest().log(LogStatus.PASS, Object + " is present on screen");
					return true;
				} else {
					Report.LogInfo("verifyExists", "<b><i>" + Object + "</i></b> is not present on screen", "FAIL");
					ExtentTestManager.getTest().log(LogStatus.FAIL, Object + " is not present on screen");
					ExtentTestManager.getTest().log(LogStatus.INFO,
							ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
					softAssert.fail(Object + " is not present on screen");
					return false;
				}
			} catch (Exception e) {

				setImplicitWaitTimeout(20000);
				Report.LogInfo("getTextFrom", "<b><i>'" + Object + "'</i></b> is not present on Screen", "FAIL");
				Report.LogInfo("verifyExists", "<b><i>" + Object + "</i></b> is not present on screen", "FAIL");
				ExtentTestManager.getTest().log(LogStatus.ERROR,
						Object + " Element not found so skipping the execution");
				ExtentTestManager.getTest().log(LogStatus.INFO,
						ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
				softAssert.fail(Object + " is not present on screen");
				throw new SkipException("<b><i>" + Object + "</i></b> Element not found so skipping the execution");
				// return false;
			}
		} else {

			Report.LogInfo("verifyExists", "<b><i>" + Object + "</i></b> is not present on screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.ERROR, Object + " Element not found so skipping the execution");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(Object + " is not present on screen");
			throw new SkipException("<b><i>" + Object + "</i></b> Element not found so skipping this exception");
			// return false;
		}
	}

	/**
	 * Set page to accept/reject all confirm boxes
	 * 
	 * @param elementLocator
	 */
	public void clickConfirmBox(String option) {
		Alert alert = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().alert();
		if (!isBlankOrNull(option)) {
			if (option.equalsIgnoreCase("OK")) {
				alert.accept();
			} else {
				alert.dismiss();
			}
		}
	}

	/**
	 * Set window as current active window for specified title
	 * 
	 * @param title -
	 * @throws Exception
	 */
	public void setWindow(String title) {
		String currentHandle = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getWindowHandle();

		for (String handle : SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getWindowHandles()) {

			if (title.equals(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().window(handle).getTitle())) {
				return;
			}
		}

		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().window(currentHandle);
	}

	/**
	 * This method will find the webElement based on id/name/xpath/linkText
	 * 
	 * @param locator
	 * @param webElement
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	protected WebElement findWebElement(String locator) throws IOException {

		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);

		WebElement webElement = null;
		if (!isBlankOrNull(locator)) {
			try {

				// Report.LogInfo("findWebElement","Web element '"+locator+ "' is finding",
				// "INFO");
				if (locator.startsWith("@id")) { // e.g @id='elementID'
					// Find the text input element by its id
					webElement = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElement(By.id(removeStart(locator, "@id=")));
				} else if (locator.startsWith("@name")) {
					// Find the text input element by its name

					webElement = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElement(By.name(removeStart(locator, "@name=")));

				} else if (locator.startsWith("@linkText")) {
					// Find the text input element by its link text

					webElement = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElement(By.linkText(removeStart(locator, "@linkText=")));

				} else if (locator.startsWith("@partialLinkText")) {
					// Find the text input element by its link text

					webElement = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElement(By.partialLinkText(removeStart(locator, "@partialLinkText=")));

				} else if (locator.startsWith("@xpath")) {
					// using XPATH locator.

					webElement = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElement(By.xpath(removeStart(locator, "@xpath=")));

				} else if (locator.startsWith("@css")) {
					// Find the text input element by its css locator

					webElement = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElement(By.cssSelector(removeStart(locator, "@css=")));

				} else if (locator.startsWith("@className")) {
					// Find the text input element by its class Name

					webElement = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElement(By.className(removeStart(locator, "@className=")));

				} else if (locator.startsWith("@tagName")) {
					// Find the text input element by its class Name
					webElement = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElement(By.className(removeStart(locator, "@tagName=")));
				}

			} catch (NoSuchElementException e) {
				// Report.LogInfo("findWebElement", "Exception encountered while trying to find
				// element [locator='"+locator+"']: "+e.getMessage(), "FAIL");
			} catch (RuntimeException e) {
				// Report.LogInfo("findWebElement", "Element does not exist
				// [locator='"+locator+"']: "+e.getMessage(), "FAIL");
				((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
						.executeScript("arguments[0].style.border='3px solid red'", webElement);
			}
		}

		((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
				.executeScript("arguments[0].style.border='3px solid green'", webElement);

		// log.trace("Exiting method findWebElement");
		return webElement;
	}

	/**
	 * This method will find the webElement based on id/name/xpath/linkText
	 * 
	 * @param locator
	 * @param webElement
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	protected List<WebElement> findWebElements(String locator) throws IOException {

		// log.trace("Entering method findWebElements [locator='"+locator+"']");
		//// info("Find web elements [locator='"+locator+"']");
		List<WebElement> webElements = null;
		if (!isBlankOrNull(locator)) {
			try {
				if (locator.startsWith("@id")) { // e.g @id='elementID'
					// Find the text input element by its id

					webElements = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElements(By.id(removeStart(locator, "@id=")));

				} else if (locator.startsWith("@name")) {
					// Find the text input element by its name

					webElements = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElements(By.name(removeStart(locator, "@name=")));

				} else if (locator.startsWith("@linkText")) {
					// Find the text input element by its link text

					webElements = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElements(By.linkText(removeStart(locator, "@linkText=")));

				} else if (locator.startsWith("@partialLinkText")) {
					// Find the text input element by its link text

					webElements = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElements(By.partialLinkText(removeStart(locator, "@partialLinkText=")));

				} else if (locator.startsWith("@xpath")) {
					// using XPATH locator.

					webElements = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElements(By.xpath(removeStart(locator, "@xpath=")));

				} else if (locator.startsWith("@css")) {
					// Find the text input element by its link text

					webElements = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElements(By.cssSelector(removeStart(locator, "@css=")));

				} else if (locator.startsWith("@className")) {
					// Find the text input element by its class Name

					webElements = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()
							.findElements(By.className(removeStart(locator, "@className=")));

				}
			} catch (NoSuchElementException e) {
				// log.trace("Did not find specified HTML element [locator="+locator+"] \n
				// "+e.getMessage());
				// throw new Exception("Element does not exist [locator='"+locator+"']:
				// "+e.getMessage());
			} catch (RuntimeException e) {
				// log.trace(e.getMessage());
				// throw new Exception("Exception encountered while trying to find elements
				// [locator='"+locator+"']: "+e.getMessage());
			}
		}
		if (webElements == null) {
			// throw new Exception("Element not found [locator='"+locator+"']");
		}
		// log.trace("Exiting method findWebElements ");
		return webElements;
	}

	/**
	 * @return boolean
	 * @throws Exception
	 */
	public boolean verifyDoesNotExist(String locator) {
		// log.trace("Entering method verifyDoesNotExist [locator='"+locator+"']");
		boolean result = false;
		if (!isBlankOrNull(locator)) {
			try {
				setImplicitWaitTimeout(waitForWebElementNotExistTimeOut);
				findWebElement(locator);
			} catch (Exception e) {
				if (e.getCause().getClass().equals(NoSuchElementException.class)) {
					result = true;
				} else {
					// throw e;
				}
			} finally {
				setImplicitWaitTimeout(waitForWebElementTimeOut);
			}
			// info("Verify field exists [locator='"+locator+"']");

		}
		// log.trace("Exiting method verifyDoesNotExist");
		return result;
	}

	/**
	 * @throws Exception
	 * 
	 */
	public String getAttributeFrom(String locator, String attributeName) {
		// log.trace("Entering method getAttributeValue [locator='"+locator+"']");
		String returnValue = null;
		if (!isBlankOrNull(locator) && !isBlankOrNull(attributeName)) {
			try {

				WebElement webElement = findWebElement(locator);
				returnValue = webElement.getAttribute(attributeName).trim();// This should be parameter
				Report.LogInfo("getAttributeValue", "Attribute <B>'" + attributeName + "'</B> in locator : <I>'"
						+ locator + "'</I> is :" + returnValue, "INFO");
				ExtentTestManager.getTest().log(LogStatus.INFO,
						"Attribute " + attributeName + " in locator : " + locator + " is :" + returnValue);
			} catch (NoSuchElementException e) {
				// log.trace("Did not find specified HTML element [locator="+locator+"] \n
				// "+e.getMessage());
				// throw new Exception("Element does not exist [locator='"+locator+"']:
				// "+e.getMessage());
			} catch (Exception e) {
				// throw new Exception("Exception encountered while trying to getAttributeValue
				// [locator='"+locator+"']: "+e.getMessage(), e);
			}
		}
		return returnValue;
	}

	public String getAttributeFrom(String locator, String attributeName, String objectName) throws IOException {
		String returnValue = null;
		if (!isBlankOrNull(locator) && !isBlankOrNull(attributeName)) {
			try {
				WebElement webElement = findWebElement(locator);
				returnValue = webElement.getAttribute(attributeName).trim();// This should be parameter
				Report.LogInfo("getAttributeValue", "Value of attribute <B><i>'" + attributeName
						+ "'</i></B> of Object '" + objectName + "' is :'" + returnValue + "'", "INFO");
				ExtentTestManager.getTest().log(LogStatus.INFO, "Value of attribute '" + attributeName + "' of Object '"
						+ objectName + "' is :'" + returnValue + "'");
			} catch (NoSuchElementException e) {
			} catch (Exception e) {
				Report.LogInfo("Exception", "Exception in getAttributeFrom " + e.getMessage(), "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL, "Exception in getAttributeFrom " + e.getMessage());
				ExtentTestManager.getTest().log(LogStatus.INFO,
						ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
				softAssert.fail("Exception in getAttributeFrom " + e.getMessage());
			}
		}
		return returnValue;
	}

	public String getTextFrom(String locator) throws IOException {
		String returnText = null;
		setImplicitWaitTimeout(2);
		if (!isBlankOrNull(locator)) {

			WebElement webElement = findWebElement(locator);

			try {

				returnText = webElement.getText().trim();
				Report.LogInfo("getTextFrom", "getting text from locator : ' " + locator + " is " + returnText, "INFO");
				ExtentTestManager.getTest().log(LogStatus.INFO,
						"getting text from locator : ' " + locator + " is " + returnText);
			} catch (NoSuchElementException e) {
				Report.LogInfo("getTextFrom", "Text isn't found for locator: ' " + locator, "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL, "Text isn't found for locator: ' " + locator);
				ExtentTestManager.getTest().log(LogStatus.INFO,
						ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
				softAssert.fail("Text isn't found for locator: ' " + locator);
				// throw new Exception("Element does not exist [locator='"+locator+"']:
				// "+e.getMessage());
			} catch (Exception e) {
				Report.LogInfo("getTextFrom", "Text isn't found for locator: ' " + locator, "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL, "Text isn't found for locator: ' " + locator);
				ExtentTestManager.getTest().log(LogStatus.INFO,
						ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
				softAssert.fail("Text isn't found for locator: ' " + locator);
				// throw new Exception("Exception encountered while trying to getText
				// [locator='"+locator+"']: "+e.getMessage(), e);
			}
		}
		return returnText;
	}

	public String getTextFrom(String locator, String Object) throws IOException {
		String returnText = null;
		setImplicitWaitTimeout(2);
		if (!isBlankOrNull(locator)) {
			try {

				WebElement webElement = findWebElement(locator);

				returnText = webElement.getText().trim();
				Report.LogInfo("getTextFrom",
						"Text from object : '<b><i> " + Object + "</i></b>' is '<i>" + returnText + "</i>'", "INFO");
				ExtentTestManager.getTest().log(LogStatus.INFO,
						"Text from object : ' " + Object + "' is '" + returnText + "'");
			} catch (NoSuchElementException e) {
				Report.LogInfo("getTextFrom", "<b><i>'" + Object + "'</i></b> is not present on Screen", "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL, "'" + Object + "' is not present on Screen");
				ExtentTestManager.getTest().log(LogStatus.INFO,
						ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
				softAssert.fail(Object + " is not present on Screen");
				// log.trace("Did not find specified HTML element [locator="+locator+"] \n
				// "+e.getMessage());
				// throw new Exception("Element does not exist [locator='"+locator+"']:
				// "+e.getMessage());
			} catch (Exception e) {
				Report.LogInfo("getTextFrom", "<b><i>'" + Object + "'</i></b> is not present on Screen", "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL, "'" + Object + "' is not present on Screen");
				ExtentTestManager.getTest().log(LogStatus.INFO,
						ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
				softAssert.fail(Object + " is not present on Screen");
				// throw new Exception("Exception encountered while trying to getText
				// [locator='"+locator+"']: "+e.getMessage(), e);
			}
		}

		return returnText;
	}

	public void javaScriptWait() {
		String str;
		try {
			do {
				JavascriptExecutor js = (JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get();
				str = (String) js.executeScript("return document.readyState");

			} while (!str.equals("complete"));
		} catch (Exception e) {
			Report.LogInfo("Exception", e.getMessage(), "FAIL");
			softAssert.fail(e.getMessage());
		}
	}

	public boolean isElementPresent(By locator) {
		String loc = null;

		try {
			setImplicitWaitTimeout(1);
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().findElement(locator);
			return true;

			// log.trace("Exiting method isTextPreset returning result [TRUE]");
		} catch (NoSuchElementException e) {
			// log.trace("Exiting method isTextPreset returning result [FALSE]");
			// Report.LogInfo("isElementPresent","verify locator : \""+locator+"\" is not
			// present", "INFO");
			return false;
			// throw e;
		} catch (Exception e) {
			// log.trace("Exiting method isTextPreset returning result [FALSE]");
			// Report.LogInfo("isElementPresent","verify locator : \""+loc+"\" is not
			// present", "INFO");
			return false;
			// throw e;
		}

	}

	public boolean isElementPresent(By locator, String object) {
		String loc = null;

		try {
			setImplicitWaitTimeout(1);
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().findElement(locator);

			// loc = webDriver.findElement(locator).getAttribute("id");
			Report.LogInfo("isElementPresent", object + ": is present on screen", "PASS");
			ExtentTestManager.getTest().log(LogStatus.PASS, object + ": is present on screen");
			return true;

			// log.trace("Exiting method isTextPreset returning result [TRUE]");
		} catch (NoSuchElementException e) {
			// log.trace("Exiting method isTextPreset returning result [FALSE]");
			Report.LogInfo("isElementPresent", "verify locator : \"" + locator + "\" is not present", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, "verify locator : \"" + locator + "\" is not present");
			return false;
			// throw e;
		} catch (Exception e) {
			// log.trace("Exiting method isTextPreset returning result [FALSE]");
			Report.LogInfo("isElementPresent", object + ": is not present on screen", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, "verify locator : \"" + locator + "\" is not present");
			return false;
			// throw e;
		}

	}

	public void clearTextBox(String locator) throws IOException {
		WebElement webElement = findWebElement(locator);
		webElement.clear();
	}

	public void clear(String locator) throws IOException {
		WebElement webElement = findWebElement(locator);
		webElement.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.BACK_SPACE));

	}

	public void sendKeys(String locator, String textValue) throws IOException {

		WebElement webElement = null;
		if (!isBlankOrNull(locator)) {
			webElement = findWebElement(locator);
			if (isBlankOrNull(textValue)) {
				return;
			}

			if (isValueEmpty(textValue)) {
				// info("Clear the field contents [text='"+textValue+"',
				// locator='"+locator+"']");
				webElement.clear();
				return;
			}

			textValue = processSpaceValues(textValue);

			webElement.clear();
			webElement.sendKeys(textValue);
			Report.LogInfo("sendKeys", textValue + " Entered into " + locator, "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, textValue + " Entered into " + locator);
			// info("Type text into field [text='"+textValue+"', locator='"+locator+"']");
		} else {
			// throw new Exception("Invalid locator format [locator='"+locator+"']");
		}
	}

	public void sendKeys1(String locator, String textValue) throws IOException {

		WebElement webElement = null;
		if (!isBlankOrNull(locator)) {
			webElement = findWebElement(locator);
			if (isBlankOrNull(textValue)) {
				return;
			}

			if (isValueEmpty(textValue)) {
				// info("Clear the field contents [text='"+textValue+"',
				// locator='"+locator+"']");
				webElement.clear();
				return;
			}

			textValue = processSpaceValues(textValue);

			// webElement.clear();
			webElement.sendKeys(textValue);
			Report.LogInfo("sendKeys", textValue + " Entered into " + locator, "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, textValue + " Entered into " + locator);
		} else {
			// throw new Exception("Invalid locator format [locator='"+locator+"']");
		}
	}

	public void sendKeys(String locator, String textValue, String message) throws IOException {
		try {
			WebElement webElement = null;
			if (!isBlankOrNull(locator)) {
				webElement = findWebElement(locator);
				if (isBlankOrNull(textValue)) {
					return;
				}

				if (isValueEmpty(textValue)) {
					// info("Clear the field contents [text='"+textValue+"',
					// locator='"+locator+"']");
					webElement.clear();
					return;
				}

				textValue = processSpaceValues(textValue);

				webElement.clear();
				webElement.sendKeys(textValue);
				Report.LogInfo("sendKeys", "<i>" + textValue + "</i> entered in :<b><i>" + message + "</i></b>",
						"INFO");
				ExtentTestManager.getTest().log(LogStatus.INFO, textValue + " entered in : " + message);
				// info("Type text into field [text='"+textValue+"', locator='"+locator+"']");
			} else {
				// throw new Exception("Invalid locator format [locator='"+locator+"']");
			}
		} catch (Exception e) {
			Report.LogInfo("sendKeys", locator + "Not present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, locator + " is not present on screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(locator + " Not present on Screen");
		}
	}

	public void sendKeysByJS(String locator, String value) throws InterruptedException, IOException {
		JavascriptExecutor js = null;
		WebElement webElement = findWebElement(locator);

		js = (JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get();
		js.executeScript("arguments[0].value='" + value + "';", webElement);
	}

	public ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {
		return new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				WebElement toReturn = driver.findElement(locator);
				if (toReturn.isDisplayed()) {
					return toReturn;
				}
				return null;
			}
		};
	}

	public boolean isVisible(String locator) throws NoSuchElementException {
		boolean result = true;
		/*
		 * WebElement element=findWebElement(locator); WebDriverWait wait = new
		 * WebDriverWait(webDriver, 20);
		 * wait.until(ExpectedConditions.elementToBeClickable(element));
		 */
		if (!isBlankOrNull(locator)) {
			try {

				result = findWebElement(locator).isDisplayed();

			} catch (Exception ignored) {
				result = false;
				// throw e;
			}
		} else {
			result = false;
		}
		// info("Exiting method isVisible returning result="+result);
		// log.trace("Exiting method isVisible returning result="+result);
		return result;
	}

	public boolean isVisible(By locator) {
		// log.trace("Entering method isVisible [locator='"+locator+"']");
		boolean result = true;

		try {

			result = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().findElement(locator).isDisplayed();

		} catch (Exception e) {
			result = false;
			// throw e;
		}
		// info("Exiting method isVisible returning result="+result);
		// log.trace("Exiting method isVisible returning result="+result);
		return result;
	}

	public void waitForElementToAppear(String locator, int waitTimeInSeconds) throws InterruptedException {
		for (int i = 0; i < waitTimeInSeconds; i++) {
			if (isVisible(locator)) {
				break;
			} else {
			}
		}

	}

	public void waitForElementToPresent(By locator, int waitTimeInSeconds) {
		for (int i = 0; i < waitTimeInSeconds; i++) {
			if (isElementPresent(locator)) {
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void waitForElementToAppear(String locator, int waitTimeInSeconds, int sleepTimeInMillSeconds) {
		for (int i = 0; i < waitTimeInSeconds; i++) {
			if (isVisible(locator)) {
				break;
			} else {
				sleep(sleepTimeInMillSeconds);
			}
		}
	}

	public void waitRefreshForElementToAppear(String locator, int waitTimeInSeconds, int sleepTimeInMillSeconds) {
		for (int i = 0; i < waitTimeInSeconds; i++) {
			if (isVisible(locator)) {
				break;
			} else {
				sleep(sleepTimeInMillSeconds);
				refreshPage();
			}
		}
	}

	public void sleep(int sleepTimeInMillSeconds) {
		try {
			Thread.sleep(sleepTimeInMillSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isAlertPresent() {
		try {
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().alert();
			return true;
		} // try
		catch (NoAlertPresentException Ex) {
			return false;
		} // catch
	}

	public void waitForElementToDisappear(String locator, int waitTimeInSeconds) {
		for (int i = 0; i < waitTimeInSeconds; i++) {
			if (!isVisible(locator)) {
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void waitForElementToDisappear(By locator, int waitTimeInSeconds) {
		for (int i = 0; i < waitTimeInSeconds; i++) {
			if (!isVisible(locator)) {
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public boolean waitForElementToBeVisible(String locator, int timeOut) throws IOException {
		boolean status = false;

		WebElement webElement = findWebElement(locator);
		try {
			FluentWait<WebDriver> fluentWait = new FluentWait<>(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
					.withTimeout(timeOut, TimeUnit.SECONDS).pollingEvery(1000, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);
			fluentWait.until(ExpectedConditions.elementToBeClickable(webElement));
			status = true;
		} catch (Exception e) {

		}

		return status;
	}

	public static void assertTrue(boolean condition, String message) {
		Assert.assertTrue(condition, message);
	}

	public static void assertFalse(boolean condition, String message) {
		Assert.assertFalse(condition, message);
	}

	public static void assertEquals(Object actual, Object expected, String message) {
		Assert.assertEquals(actual, expected, message);
	}

	public static void verifyTrue(boolean condition, String message) {
		try {
			assertTrue(condition, message);
			Report.LogInfo("verifyTrue", message, "PASS");
			ExtentTestManager.getTest().log(LogStatus.PASS, message);
		} catch (Throwable e) {
			addVerificationFailure(e);
			Report.LogInfo("verifyTrue", message, "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, message);
			sAssert.fail(message);
			failureLog(message, "verifyTrue: " + condition);
		}
	}

	public static void verifyFalse(boolean condition) {
		verifyFalse(condition, "");
	}

	public static void verifyFalse(boolean condition, String message) {
		try {
			assertFalse(condition, message);
			Report.LogInfo("verifyFalse", message, "PASS");
			ExtentTestManager.getTest().log(LogStatus.PASS, message);
		} catch (Throwable e) {
			addVerificationFailure(e);
			failureLog(message, "verifyFalse: " + condition);
			Report.LogInfo("verifyFalse", message, "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, message);
			sAssert.fail(message);
		}
	}

	public static void verifyEquals(boolean actual, boolean expected) throws IOException {
		verifyEquals(actual, expected, "");
	}

	public static void verifyEquals(Object actual, Object expected) throws IOException {
		verifyEquals(actual, expected, "");
	}

	public static void verifyEquals(Object actual, Object expected, String message) throws IOException {
		try {
			// log.trace("Entering method verifyEquals [actual object='"+actual
			// +"'][expected object='"+expected +"']");
			assertEquals(actual, expected, message);
			if (message.isEmpty()) {

				Report.LogInfo("verifyEquals",
						"ActualDisplay text <i>'" + actual + "'</i> equal to Expected Text '<i>" + expected + "</i>'",
						"PASS");
				ExtentTestManager.getTest().log(LogStatus.PASS,
						"ActualDisplay text '" + actual + "' equal to Expected Text '" + expected + "'");
			} else {
				Report.LogInfo("verifyEquals",
						"ActualDisplay text <i>'" + actual + "'</i> equal to Expected Text '<i>" + expected + "</i>'",
						"PASS");
				ExtentTestManager.getTest().log(LogStatus.PASS,
						"ActualDisplay text '" + actual + "' equal to Expected Text '" + expected + "'");
			}
		} catch (Throwable e) {
			// addVerificationFailure(e);
			if (message.isEmpty()) {

				Report.LogInfo("verifyEquals", "ActualDisplay text <i>'" + actual
						+ "'</i> not equal to Expected Text '<i>" + expected + "</i>'", "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL,
						"ActualDisplay text '" + actual + "' not equal to Expected Text '" + expected + "'");
				ExtentTestManager.getTest().log(LogStatus.INFO,
						ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
				sAssert.fail("ActualDisplay text " + actual + " not equal to Expected Text " + expected);
			} else {
				Report.LogInfo("verifyEquals", "ActualDisplay text <i>'" + actual
						+ "'</i> not equal to Expected Text '<i>" + expected + "</i>'", "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL,
						"ActualDisplay text '" + actual + "' not equal to Expected Text '" + expected + "'");
				ExtentTestManager.getTest().log(LogStatus.INFO,
						ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
				sAssert.fail("ActualDisplay text " + actual + " not equal to Expected Text " + expected);
			}
			failureLog(message, "verifyEquals: " + actual + " NOT EQUAL to " + expected);
		}
		// log.trace("Exiting method verifyEquals [actual object='"+actual +"'][expected
		// object='"+expected +"']");
	}

	public static void verifyEquals(Object[] actual, Object[] expected) throws IOException {
		verifyEquals(actual, expected, "");
	}

	public static void verifyEquals(Object[] actual, Object[] expected, String message) throws IOException {
		try {
			assertEquals(actual, expected, "");
			Report.LogInfo("verifyEquals", actual + " Object Is verified with " + expected, "PASS");
			ExtentTestManager.getTest().log(LogStatus.PASS, actual + " Object Is verified with " + expected);
		} catch (Throwable e) {
			String failureReason = "verifyEquals: " + actual + " NOT EQUAL to " + expected;
			Report.LogInfo("verifyEquals", actual + " Object Is not verified with " + expected, "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, actual + " Object Is not verified with " + expected);
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			sAssert.fail(actual + " Object Is not verified with " + expected);

			failureReason += "\n\t\tExpected:";
			for (int i = 0; i < expected.length; i++) {
				failureReason += (String) expected[i] + "|";
			}

			failureReason += "\n\t\tActual  : |";
			for (int i = 0; i < actual.length; i++) {
				failureReason += (String) actual[i] + "|";
			}
			addVerificationFailure(e);
			failureLog(message, failureReason);
		}
	}

	public static void logVerificationFailure(Exception e, String message) {

		addVerificationFailure(e);
		failureLog(message, "Failure due to exception: " + e.getMessage());

	}

	public static void fail(String message) {
		Assert.fail(message);
	}

	public static List<Throwable> getVerificationFailures() {
		List<Throwable> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<Throwable>() : verificationFailures;
	}

	public static void addVerificationFailure(Throwable e) {

		try {
			List<Throwable> verificationFailures = getVerificationFailures();
			verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);

			verificationFailures.add(e);
			failureLog(Reporter.getCurrentTestResult().getName(),
					"Verification Failure # " + verificationFailures.size());
			verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);

			// takeSnapshot(Reporter.getCurrentTestResult().getName() + " Failure # " +
			// verificationFailures.size());
		} catch (Exception ex) {
			ex.getMessage();
		}
	}

	protected static void failureLog(String customMessage, String failureReason) {

		if (customMessage == "") {
			// log.debug("\n\t" + failureReason);
			info("\n\t" + failureReason);
			// log.trace("\n\t" + failureReason);
		} else {
			// log.debug("\n\t" + customMessage + "\n\t\t" + failureReason);

			info("\n\t" + customMessage + "\n\t\t" + failureReason);
			// log.trace("\n\t" + customMessage + "\n\t\t" + failureReason);
		}
	}

	public String getFirstSelectedOptionFromDropdown(String locator) throws IOException {

		// log.trace("Entering method getFirstSelectedOptionFromDropdown
		// [locator="+locator+"]");

		WebElement element = findWebElement(locator);

		// log.debug("Found element for locator [element='"+element+"']");

		Select select = new Select(element);
		String getSelectedOptionText = select.getFirstSelectedOption().toString();

		info("Select locator [locator='" + locator + "']");
		// log.trace("Exiting method getFirstSelectedOptionFromDropdown");

		return getSelectedOptionText;
	}

	public int getXPathCount(String locator) throws IOException {

		setImplicitWaitTimeout(2);
		return findWebElements(locator).size();
	}

	public static String removeStart(String str, String remove) {
		// log.trace((new StringBuilder()).append("Entering method removeStart
		// [str=").append(str).append(",
		// remove=").append(remove).append("]").toString());
		String returnStr = "";
		if (isBlankOrNull(str) || isBlankOrNull(remove)) {
			// log.debug((new StringBuilder()).append("Returned value is
			// [str='").append(str).append("']").toString());
			returnStr = str;
		}
		if (str.startsWith(remove))
			returnStr = str.substring(remove.length());
		// log.trace((new StringBuilder()).append("Exiting method removeStart
		// [returnStr='").append(returnStr).append("']").toString());
		return returnStr;

	}

	public static boolean isEmptyOrNull(String str) {
		// log.debug((new StringBuilder()).append("Inside isEmptyOrNull
		// [str=").append(str).append("]").toString());
		return str == null || str.length() == 0;
	}

	public static boolean isFieldNone(String str) {
		// log.debug((new StringBuilder()).append("Inside isEmptyOrNull
		// [str=").append(str).append("]").toString());
		return str.equalsIgnoreCase("None");
	}

	public static boolean isBlankOrNull(String str) {
		// log.debug((new StringBuilder()).append("Inside isBlankOrNull
		// [str=").append(str).append("]").toString());
		return str == null || str.trim().length() == 0;
	}

	public static boolean isAlphanumeric(String str) {
		// log.trace((new StringBuilder()).append("Entering method isAlphanumeric
		// [str=").append(str).append("]").toString());
		if (isBlankOrNull(str)) {
			// log.debug("returning false");
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++)
			if (!Character.isLetterOrDigit(str.charAt(i))) {
				// log.debug("returning false");
				return false;
			}

		// log.trace("Exiting method isAlphanumeric");
		return true;
	}

	public static boolean validateT(Object spattern, String text, WebElement webElement) {

		String patternToBeMatched = (String) spattern;
		Pattern pattern = Pattern.compile(patternToBeMatched);

		Matcher matcher = pattern.matcher(text);
		if (!matcher.matches()) {
			// System.out.println("FAIL");
			((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
					.executeScript("arguments[0].style.border='3px solid red'", webElement);
			Report.LogInfo("validateStringPatternMatch",
					"Text \"" + text + "\" is not matched with pattern : \"" + spattern + "\"", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					"Text \"" + text + "\" is not matched with pattern : \"" + spattern + "\"");
			sAssert.fail("Text \"" + text + "\" is not matched with pattern : \"" + spattern + "\"");
		} else {
			// System.out.println("PASS");
			((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
					.executeScript("arguments[0].style.border='3px solid green'", webElement);
			Report.LogInfo("validateStringPatternMatch",
					"Text \"" + text + "\" is matched with pattern : \"" + spattern + "\"", "PASS");
			ExtentTestManager.getTest().log(LogStatus.PASS,
					"Text \"" + text + "\" is matched with pattern : \"" + spattern + "\"");
		}
		return matcher.matches();
	}

	public static boolean validate(Object spattern, String text) {

		String patternToBeMatched = (String) spattern;
		Pattern pattern = Pattern.compile(patternToBeMatched);

		Matcher matcher = pattern.matcher(text);
		if (!matcher.matches()) {
			// System.out.println("FAIL");
			Report.LogInfo("validateStringPatternMatch",
					"Text \"" + text + "\" is not matched with pattern : \"" + spattern + "\"", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					"Text \"" + text + "\" is not matched with pattern : \"" + spattern + "\"");
			sAssert.fail("Text \"" + text + "\" is not matched with pattern : \"" + spattern + "\"");
		} else {
			// System.out.println("PASS");
			Report.LogInfo("validateStringPatternMatch",
					"Text \"" + text + "\" is matched with pattern : \"" + spattern + "\"", "PASS");
			ExtentTestManager.getTest().log(LogStatus.PASS,
					"Text \"" + text + "\" is matched with pattern : \"" + spattern + "\"");
		}
		return matcher.matches();
	}

	public static boolean isNumeric(String str) {
		// log.trace((new StringBuilder()).append("Entering method isNumeric
		// [str=").append(str).append("]").toString());
		if (str == null) {
			// log.debug("returning false");
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++)
			if (!Character.isDigit(str.charAt(i))) {
				// log.debug("returning false");
				return false;
			}

		// log.debug("returning true");
		// log.trace("Exiting method isNumeric");
		return true;
	}

	public boolean switchWindow(String urlContent, String windowName) {
		boolean flag = false;
		Set<String> handlers = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getWindowHandles();
		if (SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getWindowHandles().size() >= 1) {
			for (String handler : handlers) {
				SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().window(handler);
				if (SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getCurrentUrl().contains(urlContent)) {
					Report.LogInfo("switchWindow", "Window switched to " + windowName, "INFO");
					ExtentTestManager.getTest().log(LogStatus.INFO, "Window switched to " + windowName);
					flag = true;
					break;
				}
			}
		}

		return flag;

	}

	public void setWin(String title) {

		String currentHandle = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getWindowHandle();

		for (String handle : SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getWindowHandles()) {

			if (title.equals(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().window(handle).getTitle())) {
				return;
			}
		}
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().window(currentHandle);

	}

	public String getTitle(String currentHandle) {
		for (String handle : SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getWindowHandles()) {

			if (!currentHandle.equals(handle)) {
				return SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().window(handle).getTitle();
			}
		}
		return currentHandle;
	}

	public void isChecked(String locator, String Object) throws IOException {

		boolean checked = findWebElement(locator).isSelected();

		if (checked != true) {
			Report.LogInfo("Unchecked", Object + " Is unchecked", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, Object + " Is unchecked");
		} else {
			Report.LogInfo("Checked", Object + " Is checked", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, Object + " Is checked");
		}
	}

	public void theSearch(String value, String locator) throws Exception {
		// String xpathExpression = "//*[starts-with(@id,'searchResultsTable:')]";
		List<WebElement> elementTable = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().findElements(By.xpath(locator));

		for (WebElement listofElement : elementTable) {
			String theElement = listofElement.getText();

			if (theElement.contains(value)) {
				Assert.assertEquals(value, theElement);
				// System.out.println("The Expected Value " + value + " Equals the actual " +
				// theElement);;
			}

		}

	}

	public String getURLFromPage() {
		// WebDriver driver = new WebDriver();
		String url = SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().getCurrentUrl();
		return url;
	}

	public void waitToElementVisible(String locator1) throws InterruptedException {
		waitForElementToAppear(locator1, 20000);
	}

	public void waitToPageLoad() {
		JavascriptExecutor js = (JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get();
		js.executeScript("return document.readyState").toString().equals("complete");
	}

	public void waitToFrameVisible() throws IOException {
		try {
			sleep(8000);
			WebDriverWait driverWait = new WebDriverWait(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get(), 20);
			driverWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(0));
			Thread.sleep(2000);
			// webDriver.switchTo().frame(0);
			Report.LogInfo("Element is available", "Mouse over on  is done Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, "Mouse over on is done Successfully");

		} catch (Exception e) {
			Report.LogInfo("Element is", "Is not Present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, "Element is not present on screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail("Is not Present on Screen");
		}
	}

	public void mouseMoveOn(String locator) throws IOException {
		// log.trace("Entering method mouseOver [locator="+locator+"]");
		sleep(2000);
		WebElement element1 = findWebElement(locator);
		Actions builder = new Actions(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get());
		// builder.build().perform();
		builder.moveToElement(element1).perform();
		sleep(2000);
	}

	public void keyPressOn(String locator) throws IOException {

		WebElement element1 = findWebElement(locator);
		Actions builder = new Actions(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get());

		// builder.build().perform();
		builder.keyDown(element1, Keys.CONTROL).perform();

	}

	public void keyPress(String locator, Keys command) throws IOException {

		WebElement element1 = findWebElement(locator);
		element1.sendKeys(command);

	}

	public void clickHiddenElement(String locator) {
		JavascriptExecutor js = (JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get();

		WebElement hiddenEle = (WebElement) js.executeScript("return document.getElementBy.Xpath(locator).mouseOver()");

		hiddenEle.click();
	}

	public static String toToggleCase(String inputString) {
		String result = "";
		for (int i = 0; i < inputString.length(); i++) {
			char currentChar = inputString.charAt(i);
			if (Character.isUpperCase(currentChar)) {
				char currentCharToLowerCase = Character.toLowerCase(currentChar);
				result = result + currentCharToLowerCase;
			} else {
				char currentCharToUpperCase = Character.toUpperCase(currentChar);
				result = result + currentCharToUpperCase;
			}
		}
		return result;
	}

	public void scrollUp() {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get();
			jse.executeScript("scroll(0, -250);");
		} catch (Exception e) {
			Report.LogInfo("ScrollUp", "Page not able to scroll up" + e, "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, "Page not able to scroll up");
		}
	}

	public void scrollDown(String locator) throws IOException {
		sleep(2000);
		WebElement webElement = findWebElement(locator);

		/*
		 * if(g.getBrowser().equalsIgnoreCase("edge")
		 * ||(g.getBrowser().equalsIgnoreCase("firefox"))) {
		 */
		((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
				.executeScript("arguments[0].scrollIntoView(false);", webElement);
		/*
		 * } else { ((JavascriptExecutor)
		 * webDriver).executeScript("arguments[0].scrollIntoView(true);", webElement); }
		 */
	}

	public boolean isEnable(String locator) throws NoSuchElementException {
		boolean result = true;
		if (!isBlankOrNull(locator)) {
			try {

				result = findWebElement(locator).isEnabled();

			} catch (Exception ignored) {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

	@SuppressWarnings({ "resource", "deprecation" })
	public void uploadFile(String url, File file) throws IOException {
		HttpResponse response = null;
		Header[] cookie = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		httppost.addHeader("Accept", "*/*");
		httppost.addHeader("Content-type", "application/json");

		// File fileToUse = new File("/path_to_file/YOLO.jpg"); //e.g.
		// /temp/dinnerplate-special.jpg
		FileBody data = new FileBody(file);
		String file_type = "JPG";

		MultipartEntity reqEntity = new MultipartEntity();
		/*
		 * reqEntity.addPart("file_name", new StringBody( fileToUse.getName() ) );
		 * reqEntity.addPart("folder_id", new StringBody(folder_id));
		 * reqEntity.addPart("description", new StringBody(description));
		 * reqEntity.addPart("source", new StringBody(source));
		 */
		reqEntity.addPart("file_type", new StringBody(file_type));
		reqEntity.addPart("data", data);

		httppost.setEntity(reqEntity);
		cookie = (Header[]) response.getHeaders("Set-Cookie");
		for (int h = 0; h < cookie.length; h++) {
			System.out.println(cookie[h]);
		}
		httpclient.execute(httppost);
		System.out.println(response);

		HttpEntity resEntity = response.getEntity();
		System.out.println(resEntity);
		System.out.println(EntityUtils.toString(resEntity));

		EntityUtils.consume(resEntity);
		httpclient.getConnectionManager().shutdown();
	}

	public void waitForAjax() {

		try {
			WebDriverWait driverWait = new WebDriverWait(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get(), 10);

			ExpectedCondition<Boolean> expectation;
			expectation = new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver driverjs) {

					JavascriptExecutor js = (JavascriptExecutor) driverjs;
					return js.executeScript("return((window.jQuery != null) && (jQuery.active === 0))").equals("true");
				}
			};
			driverWait.until(expectation);
		} catch (TimeoutException exTimeout) {

			// fail code
		} catch (WebDriverException exWebDriverException) {

			// fail code
		}
		return;
	}

	public static boolean ClickonElementByString(String xPath, int timeOut) throws IOException, InterruptedException {

		boolean status = false;
		boolean sFlag = false;

		int i = 1;
		Thread.sleep(2500);

		try {
			while (SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().findElement(By.xpath(xPath)).isDisplayed()) {
				if (i > timeOut) {
					status = false;
					break;
				}
				Thread.sleep(2000);
				SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().findElement(By.xpath(xPath)).isEnabled();
				scrollIntoView(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().findElement(By.xpath(xPath)));
				SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().findElement(By.xpath(xPath)).click();
//	                      System.out.println("Waiting for an element "+xPath+" to get clicked");
				sFlag = true;
				i = i + 1;
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "Exception in ClickonElementByString");
			status = true;
		}

		return status;
	}

	public static boolean fluentWaitForElementToBeVisible(WebElement webElement, int timeOut) {
		boolean status = false;

		try {
			FluentWait<WebDriver> fluentWait = new FluentWait<>(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
					.withTimeout(timeOut, TimeUnit.SECONDS).pollingEvery(1000, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);
			fluentWait.until(ExpectedConditions.elementToBeClickable(webElement));
			status = true;
		} catch (Exception e) {

		}

		return status;
	}

	public static boolean isElementEnabled(WebElement webElement) {
		boolean status = false;

		try {
			status = webElement.isEnabled();
		} catch (Exception e) {
		}

		return status;
	}

	public void clickByJS(String locator) throws IOException {
		boolean status = false;
		JavascriptExecutor js = null;

		WebElement webElement = findWebElement(locator);

		try {
			js = (JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get();
			js.executeScript("arguments[0].setAttribute('style', 'border: 2px solid blue;');", webElement);
			js.executeScript("arguments[0].removeAttribute('style', 'border: 2px solid blue;');", webElement);
			js.executeScript("arguments[0].click();", webElement);
			status = true;

		} catch (Exception e) {
		}

	}

	public static String Capturefullscreenshot() throws IOException {
		String screenshot2;
		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(screenshot.getImage(), "jpg", bos);
		byte[] imageBytes = bos.toByteArray();
		screenshot2 = "data:image/png;base64," + Base64.getMimeEncoder().encodeToString(imageBytes);
		bos.close();
		return screenshot2;
	}

	public static boolean switchToDefaultFrame() {
		boolean status = false;
		try {
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().defaultContent();
			status = true;
		} catch (Exception e) {

		}

		return status;
	}

	public void selectByValueDIV(String mainElement, String listElement, String value)
			throws InterruptedException, IOException {

		WebElement element = findWebElement(mainElement);
		WebElement Listelement = findWebElement(listElement);

		scrollIntoView(element);
		element.click();
		Thread.sleep(1000);
		isVisible(listElement);
		List<WebElement> options = Listelement.findElements(By.tagName("li"));
		for (WebElement option : options) {
			if (option.getText().equals(value)) {
				scrollIntoView(option);
				option.click();
				Thread.sleep(1250);
				break;
			}
		}

	}

	public static boolean scrollIntoTop() {
		boolean status = false;

		try {
			((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
					.executeScript("window.scrollTo(document.body.scrollHeight, 0)");
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.ERROR, "Unable to scroll to top of the page, please verify");
			System.out.println("Unable to scroll to top of the page, please verify");
		}

		return status;
	}

	public boolean clickByAction(String locator) throws IOException {
		boolean status = false;

		waitForElementToBeVisible(locator, 60);
		WebElement webElement = findWebElement(locator);

		try {
			Actions build = new Actions(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get());
			build.moveToElement(webElement).click().build().perform();
			status = true;

		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.ERROR,
					"Unable to ActionClick the webelement: " + webElement.toString() + "due to " + e.toString());
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			System.out.println("Unable to ActionClick webelement: " + webElement.toString() + "due to " + e.toString());

		}

		return status;
	}

	public static void switchToFrame(String idNameIndex) throws IOException {
		boolean status = false;
		try {
			SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().frame(idNameIndex);
			status = true;
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.ERROR,
					"Unable to switch to frame: " + idNameIndex + "due to " + e.toString());
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			System.out.println("Unable to switch to frame: " + idNameIndex + "due to " + e.toString());
		}
	}

	public static void switchToFrameByNameTag(String name) {
		// webDriver.switchTo().frame(name);
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().frame(name);
	}

	public static void switchToFrameByIndex(int index) {
		// webDriver.switchTo().frame(index);
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().frame(index);
	}

	public static void switchToFrameByElement(WebElement element) {
		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().switchTo().frame(element);
	}

	public static void switchToParentFrame() {
		webDriver.switchTo().parentFrame();
	}

	public String storeByValueDIV(String mainElement, String listElement) throws IOException {
		String listValues = "";
		WebElement mainEle = findWebElement(mainElement);
		WebElement subElements = findWebElement(listElement);
		try {
			ScrollIntoViewByString(mainElement);
			Actions build = new Actions(webDriver);
			build.moveToElement(mainEle).click().build().perform();
			waitForElementToAppear(listElement, 15);
			List<WebElement> options = subElements.findElements(By.tagName("li"));
			for (WebElement option : options) {
				String value = option.getText();
				listValues = listValues + value + "|";
			}

		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.ERROR,
					"Unable to select the value from listbox: " + listElement.toString() + "due to " + e.toString());
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			System.out.println(
					"Unable to select the value from listbox: " + listElement.toString() + "due to " + e.toString());
			throw new SkipException("Skipping this test");
		}
		return listValues;
	}

	public boolean waitForInvisibilityOfElement(String locator, int timeOut) throws IOException {
		boolean status = false;

		WebElement webElement = findWebElement(locator);

		try {
			FluentWait<WebDriver> fluentWait = new FluentWait<>(WEB_DRIVER_THREAD_LOCAL.get())
					.withTimeout(timeOut, TimeUnit.SECONDS).pollingEvery(1000, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class);
			fluentWait.until(ExpectedConditions.invisibilityOf(webElement));
			status = true;

		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.ERROR,
					"Webelement was still Visible: " + webElement.toString() + "due to " + e.toString());
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			System.out.println("Webelement was still visible: " + webElement.toString() + "due to " + e.toString());
		}

		return status;
	}

	public static void pause(Integer waitTime) {
		try {
			Thread.sleep(waitTime);
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.ERROR, "Unable to wait the execution");
		}
	}

	public boolean isPresent(String locator, int timeOut) throws IOException {
		boolean status = false;

		WebElement webElement = findWebElement(locator);

		waitForElementToBeVisible(locator, timeOut);
		try {
			status = webElement.isDisplayed();
			status = true;
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.ERROR,
					"Webelement is not present: " + webElement.toString() + "due to " + e.toString());
			System.out.println("Webelement is not present: " + webElement.toString() + "due to " + e.toString());

		}

		return status;
	}

	public void MoveToElement(String locator) throws IOException {
		WebElement element = findWebElement(locator);
		Actions actions = new Actions(webDriver);
		actions.moveToElement(element);
		actions.perform();
	}

	//////// nmts
	public void WaitForAjax() {

		try {
			WebDriverWait driverWait = new WebDriverWait(SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get(), 5);

			ExpectedCondition<Boolean> expectation;
			expectation = new ExpectedCondition<Boolean>() {

				public Boolean apply(WebDriver driverjs) {

					JavascriptExecutor js = (JavascriptExecutor) driverjs;
					return js.executeScript("return((window.jQuery != null) && (jQuery.active === 0))").equals("true");
				}
			};
			driverWait.until(expectation);
		} catch (TimeoutException exTimeout) {

			// fail code
		} catch (WebDriverException exWebDriverException) {

			// fail code
		}
		return;
	}

	public void javaScriptDoubleclick(String locator, String Object) throws IOException {

		try {
			WebElement element = findWebElement(locator);
			if (g.getBrowser().equalsIgnoreCase("firefox")) {
				element.click();
			} else if (g.getBrowser().equalsIgnoreCase("chrome")) {
				element.click();
			} else {
				scrollIntoView(element);
				sleep(1000);
				((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
						.executeScript("arguments[0].fireEvent('ondblclick');", element);

			}

			Report.LogInfo("doubleClick", "\"" + Object + "\" Is Clicked Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, Object + " Is Clicked Successfully");
		} catch (Exception e) {
			Report.LogInfo("DoubleClick", Object + " Is not Present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, Object + " Is not Present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(Object + " Is not Present on Screen");
		}
	}

	public static void AcceptAlert() throws AWTException {

		Robot robot = new Robot();
		robot.delay(3000);
		robot.keyPress(KeyEvent.VK_ENTER);
		// robot.keyPress(KeyEvent.);

		robot.delay(5000);

	}

	public void AcceptJavaScriptMethod() throws InterruptedException {
		Thread.sleep(1000);
		Alert alert = WEB_DRIVER_THREAD_LOCAL.get().switchTo().alert();
		alert.accept();
		WEB_DRIVER_THREAD_LOCAL.get().switchTo().defaultContent();
	}

	public void javaScriptclick2(String locator, String Object) throws IOException {

		try {
			WebElement element = findWebElement(locator);
			if (g.getBrowser().equalsIgnoreCase("firefox")) {
				element.click();
			} else if (g.getBrowser().equalsIgnoreCase("chrome")) {
				element.click();
			} else {
				// Thread.sleep(15000);
				((JavascriptExecutor) SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get())
						.executeScript("arguments[0].click();", element);
				Thread.sleep(20000);
				// ((JavascriptExecutor)SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()).executeScript("arguments[0].click();",
				// element);
				// ((JavascriptExecutor)SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get()).executeScript("arguments[0].fireEvent.click();",element);

			}

			Report.LogInfo("Click", "\"" + Object + "\" Is Clicked Successfully", "INFO");
			ExtentTestManager.getTest().log(LogStatus.INFO, Object + " Is Clicked Successfully");
		} catch (Exception e) {
			Report.LogInfo("Click", Object + " Is not Present on Screen", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, Object + " Is not Present on Screen");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
			softAssert.fail(Object + " Is not Present on Screen");
		}
	}

	// ============================= APT Reusables
	// ====================================
	public void compareText_InViewPage(String labelname, String expectedVal) throws IOException {
		WebElement element = null;
		String el1 = "//div[div[label[contains(text(),'";
		String el2 = "')]]]/div[2]";
		// element =
		// findWebElement("//div[div[label[contains(text(),'"+labelname+"')]]]/div[2]");
		element = webDriver.findElement(By.xpath(el1 + labelname + el2));
		String actualVal = element.getText().toString();

		if (actualVal.contains(expectedVal)) {
			Report.LogInfo("CompareText", "Text match on View page", "PASS");
		} else {
			Report.LogInfo("CompareText", "Text not match on View page", "Failed");
		}
	}

	public void compareText_InViewPage1(String labelname, String expectedVal) throws IOException {
		WebElement element = null;
		String el1 = "//div[div[label[contains(text(),'";
		String el2 = "')]]]/div[2]";
		// element =
		// findWebElement("//div[div[label[contains(text(),'"+labelname+"')]]]/div[2]");
		element = webDriver.findElement(By.xpath(el1 + labelname + el2));
		String actualVal = element.getText().toString();

		if (expectedVal.contains(actualVal)) {
			Report.LogInfo("CompareText", "Text match on View page", "PASS");
		} else {
			Report.LogInfo("CompareText", "Text not match on View page", "Failed");
		}

	}

	public void compareText_InViewPage2(String labelname, String expectedVal) throws IOException {
		WebElement element = null;
		String el1 = "(//div[div[label[contains(text(),'";
		String el2 = "')]]]//following-sibling::div[1])[1]";
		// element =
		// findWebElement("//div[div[label[contains(text(),'"+labelname+"')]]]/div[2]");
		element = webDriver.findElement(By.xpath(el1 + labelname + el2));
		String actualVal = element.getText().toString();

		if (actualVal.contains(expectedVal)) {
			Report.LogInfo("CompareText", "Text match on View page", "PASS");
		} else {
			Report.LogInfo("CompareText", "Text not match on View page", "Failed");
		}
	}

	public void edittextFields_commonMethod(String labelname, String xpathname, String expectedValueToEdit)
			throws InterruptedException, IOException {
		boolean availability = false;

		availability = isVisible(xpathname);

		if (availability) {

			if (expectedValueToEdit.equalsIgnoreCase("null")) {

				String actualvalue = getAttributeFrom(xpathname, "value");
			} else {

				clearTextBox(xpathname);
				waitForAjax();

				sendKeys(xpathname, expectedValueToEdit);
				String actualvalue = getAttributeFrom(xpathname, "value");
				Report.LogInfo("CompareText", "text field is edited", "PASS");
			}

		} else {
			Report.LogInfo("CompareText", "text field is not displaying", "FAIL");

		}

	}

	public void selectValueInsideDropdown(String xpath, String labelname, String expectedValueToAdd)
			throws IOException, InterruptedException {
		// getAllValuesInsideDropDown
		List<String> ls = new ArrayList<String>();

		// availability=getwebelement(xml.getlocator("//locators/" + application
		// + "/"+ xpath +"")).isDisplayed();
		if (isVisible(xpath)) {
			Report.LogInfo("DropDown", labelname + "dropdown is displaying", "PASS");

			Select element = new Select(findWebElement(xpath));
			String firstSelectedOption = element.getFirstSelectedOption().getText();
			List<WebElement> we = element.getOptions();

			for (WebElement a : we) {
				if (!a.getText().equals("select")) {
					ls.add(a.getText());

				}
			}

			// ExtentTestManager.getTest().log(LogStatus.PASS, "list of values
			// inside "+labelname+" dropdown is: "+ls);
			// Log.info("list of values inside "+labelname+" dropdown is: "+ls);

			if (expectedValueToAdd.equalsIgnoreCase("null")) {
				Report.LogInfo("DropDown", "No values selected under" + labelname, "PASS");
			} else {
				Select s1 = new Select(findWebElement(xpath));
				s1.selectByVisibleText(expectedValueToAdd);

				String SelectedValueInsideDropdown = element.getFirstSelectedOption().getText();
				Report.LogInfo("DropDown", labelname + "dropdown value selected as:" + SelectedValueInsideDropdown,
						"PASS");

			}
		}

	}

	public void editcheckbox_commonMethod(String expectedResult, String xpath, String labelname) throws IOException {

		if (!expectedResult.equalsIgnoreCase("null")) {
			// boolean
			// isElementSelected=getwebelement(xml.getlocator("//locators/" +
			// application + "/"+ xpath +"")).isSelected();
			boolean isElementSelected = findWebElement(xpath).isSelected();

			if (expectedResult.equalsIgnoreCase("yes")) {

				if (isElementSelected) {
					Report.LogInfo("CheckBox", "checkbox is not edited and it is already Selected while creating",
							"PASS");
					// ExtentTestManager.getTest().log(LogStatus.PASS, labelname
					// +" checkbox is not edited and it is already Selected
					// while creating");
				} else {
					click(xpath);
					Report.LogInfo("CheckBox", labelname + "checkbox is selected", "PASS");
				}
			} else if (expectedResult.equalsIgnoreCase("no")) {

				if (isElementSelected) {
					click(xpath);
					Report.LogInfo("CheckBox", labelname + "is edited and gets unselected", "PASS");
				} else {
					Report.LogInfo("CheckBox", labelname + "is not edited and it remains unselected", "PASS");
				}

			}
		} else {
			Report.LogInfo("CheckBox", "No changes made for" + labelname, "PASS");
		}
	}

	public void addCheckbox_commonMethod(String xpath, String labelname, String expectedValue)
			throws InterruptedException, IOException {

		boolean availability = false;

		availability = isVisible(xpath);
		if (availability) {
			Report.LogInfo("INFO", "checkbox is displaying as expected", "PASS");
			if (!expectedValue.equalsIgnoreCase("null")) {
				if (expectedValue.equalsIgnoreCase("yes")) {
					click(xpath, labelname);
					waitForAjax();
					boolean CPEselection = isSelected(xpath, labelname);
					if (CPEselection) {
						Report.LogInfo("INFO", "checkbox is selected as expected", "PASS");
					} else {
						Report.LogInfo("INFO", "checkbox is not selected", "FAIL");
					}
				} else {
					Report.LogInfo("INFO", "checkbox is not selected as expected", "PASS");
				}
			}
		} else {
			Report.LogInfo("INFO", "checkbox is not available", "FAIL");
		}
	}

	public void selectAndRemoveValueFromRightDropdown(String labelname, String xpath, String[] selectValue,
			String xpathForRemoveButton) {

		WebElement availability = null;
		List<String> ls = new ArrayList<String>();

		try {
			// List<WebElement> elements=
			// getwebelements(xml.getlocator("//locators/" + application + "/"+
			// xpath +""));
			List<WebElement> elements = findWebElements(xpath);
			// int element_count= elements.size();
			int element_count = getXPathCount(xpath);

			if (element_count >= 1) {

				// Print list of values inside Dropdown
				for (WebElement a : elements) {
					ls.add(a.getText());
				}
				Report.LogInfo("INFO",
						"list of values displaying inside " + labelname + " available dropdown is: " + ls, "PASS");

				// select value inside the dropdown
				for (int i = 0; i < selectValue.length; i++) {
					waitForAjax();
					for (int j = 0; j < ls.size(); j++) {
						// Log.info("ls value "+ ls.get(j));
						if (selectValue[i].equals(ls.get(j))) {
							elements.get(j).click();
							Report.LogInfo("INFO", elements.get(j) + " got selected", "PASS");
							waitForAjax();
							// WebElement
							// removeButton=getwebelement(xml.getlocator("//locators/"
							// + application + "/"+ xpathForRemoveButton
							// +"").replace("value", "<<"));
							WebElement removeButton = webDriver.findElement(
									By.xpath(removeStart(xpathForRemoveButton, "@xpath=").replace("value", "<<")));
							// WebElement removeButton =
							// findWebElement(xpathForRemoveButton).replace("value",
							// "<<");
							removeButton.click();
							// click(removeButton);
							Report.LogInfo("INFO", "clicked on remove '<<' button", "PASS");
							// ExtentTestManager.getTest().log(LogStatus.PASS,
							// "clicked on remove '<<' button");
							waitForAjax();
						}
					}
				}

			} else {
				Report.LogInfo("INFO", "No values displaying under " + labelname + " dropdown", "PASS");
			}
		} catch (Exception e) {
			Report.LogInfo("INFO", "No values displaying under " + labelname + " available dropdown", "FAIL");
		}
	}

	public void selectAndAddValueFromLeftDropdown(String labelname, String xpath, String[] selectValue,
			String xpathForAddButton) {

		WebElement availability = null;
		List<String> ls = new ArrayList<String>();

		try {
			// List<WebElement> elements=
			// getwebelements(xml.getlocator("//locators/" + application + "/"+
			// xpath +""));
			// int element_count= elements.size();
			List<WebElement> elements = findWebElements(xpath);
			// int element_count= elements.size();
			int element_count = getXPathCount(xpath);

			if (element_count >= 1) {

				// Print list of values inside Dropdown
				for (WebElement a : elements) {
					ls.add(a.getText());
				}
				Report.LogInfo("INFO",
						"list of values displaying inside " + labelname + " available dropdown is: " + ls, "PASS");
				// select value inside the dropdown
				for (int i = 0; i < selectValue.length; i++) {
					// Thread.sleep(5000);
					for (int j = 0; j < ls.size(); j++) {
						// Log.info("ls value "+ ls.get(j));
						if (selectValue[i].equals(ls.get(j))) {
							elements.get(j).click();
							// Report.LogInfo("INOF", elements.get(j) + " got selected", "PASS");
							waitForAjax();
							click(xpathForAddButton, "Add");
							waitForAjax();
						}
					}
				}

			} else {
				Report.LogInfo("INFO", "No values displaying under " + labelname + " dropdown", "PASS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Report.LogInfo("INFO", "No values displaying under " + labelname + " available dropdown", "FAIL");

		}
	}

	public void verifySelectedValuesInsideRightDropdown(String labelname, String xpath) {
		// getAllValuesInsideDropDown
		boolean availability = false;
		List<String> ls = new ArrayList<String>();

		try {

			// List<WebElement> elements=
			// getwebelements(xml.getlocator("//locators/" + application + "/"+
			// xpath +""));
			// int element_count= elements.size();
			List<WebElement> elements = findWebElements(xpath);
			int element_count = getXPathCount(xpath);

			if (element_count >= 1) {

				// Print list of values inside Dropdown
				for (WebElement a : elements) {
					ls.add(a.getText());
				}
				Report.LogInfo("INFO",
						"list of values displaying inside " + labelname + " available dropdown is: " + ls, "PASS");
			} else {
				Report.LogInfo("INFO", "No values displaying under " + labelname + " dropdown", "PASS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Report.LogInfo("INFO", "No values displaying under " + labelname + " available dropdown", "FAIL");
		}
	}

	public void waitforPagetobeenable() throws InterruptedException {
		WebElement el = webDriver.findElement(By.xpath("//body"));

		// Log.info("Start");
		while (el.getAttribute("class").contains("loading-indicator")) {
			// Log.info("Page Loading");
			Thread.sleep(5000);
		}

	}

	public void compareText(String labelname, String xpath, String ExpectedText) throws InterruptedException {

		String text = null;
		WebElement element = null;

		try {
			// element = getwebelement(xml.getlocator("//locators/" +
			// application + "/" + xpath + ""));
			element = findWebElement(xpath);
			String emptyele = getAttributeFrom(xpath, "value");
			// String emptyele = getwebelement(xml.getlocator("//locators/" +
			// application + "/" + xpath + ""))
			// .getAttribute("value");
			if (element == null) {
				Report.LogInfo("INFO", "Step:  '" + labelname + "' not found", "FAIL");
				// ExtentTestManager.getTest().log(LogStatus.FAIL, "Step: '" +
				// labelname + "' not found");
			} else if (emptyele != null && emptyele.isEmpty()) {
				Report.LogInfo("INFO", "Step : '" + labelname + "' value is empty", "PASS");
				// ExtentTestManager.getTest().log(LogStatus.PASS, "Step : '" +
				// labelname + "' value is empty");
			} else {

				text = element.getText();
				if (text.contains("-")) {

					String[] actualTextValue = text.split(" ");
					String[] expectedValue = ExpectedText.split(" ");

					if (expectedValue[0].equalsIgnoreCase(actualTextValue[0])) {
						Report.LogInfo("INFO", " The Expected value for '" + labelname + "' field '" + ExpectedText
								+ "' is same as the Acutal value '" + text + "'", "PASS");
					} else if (expectedValue[0].contains(actualTextValue[0])) {
						Report.LogInfo("INFO", "The Expected value for '" + labelname + "' field '" + ExpectedText
								+ "' is same as the Acutal value '" + text + "'", "PASS");
					} else {
						Report.LogInfo("INFO", "The Expected value for '" + labelname + "' field '" + ExpectedText
								+ "' is not same as the Acutal value '" + text + "'", "FAIL");
					}
				} else {
					if (ExpectedText.equalsIgnoreCase(text)) {
						Report.LogInfo("INFO", " The Expected value for '" + labelname + "' field '" + ExpectedText
								+ "' is same as the Acutal value '" + text + "'", "PASS");
					} else if (ExpectedText.contains(text)) {
						Report.LogInfo("INFO", "The Expected value for '" + labelname + "' field '" + ExpectedText
								+ "' is same as the Acutal value '" + text + "'", "PASS");
					} else {
						Report.LogInfo("INFO", "The Expected value for '" + labelname + "' field '" + ExpectedText
								+ "' is not same as the Acutal value '" + text + "'", "FAIL");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Report.LogInfo("INFO", labelname + " field is not displaying", "FAIL");

		}
	}

	public void addDropdownValues_commonMethod(String labelname, String xpath, String expectedValueToAdd)
			throws InterruptedException {
		boolean availability = false;
		List<String> ls = new ArrayList<String>();

		try {

			// availability=getwebelementNoWait(xml.getlocator("//locators/" +
			// application + "/"+ xpath +"")).isDisplayed();
			if (isVisible(xpath)) {
				Report.LogInfo("INFO", labelname + " dropdown is displaying", "PASS");
				if (expectedValueToAdd.equalsIgnoreCase("null")) {
					Report.LogInfo("INFO", " No values selected under " + labelname + " dropdown", "PASS");
				} else {
					webDriver.findElement(By.xpath("//div[label[text()='" + labelname + "']]//div[text()='×']"))
							.click();
					// Clickon(getwebelementNoWait("//div[label[text()='"+
					// labelname +"']]//div[text()='×']"));

					// verify list of values inside dropdown
					List<WebElement> listofvalues = webDriver
							.findElements(By.xpath("//div[@class='sc-bxivhb kqVrwh']"));

					for (WebElement valuetypes : listofvalues) {
						// Log.info("List of values : " + valuetypes.getText());
						ls.add(valuetypes.getText());
					}

					// ExtentTestManager.getTest().log(LogStatus.PASS, "list of
					// values inside "+labelname+" dropdown is: "+ls);
					// System.out.println("list of values inside "+labelname+"
					// dropdown is: "+ls);

					webDriver.findElement(By.xpath("//div[label[text()='" + labelname + "']]//input"))
							.sendKeys(expectedValueToAdd);
					// SendKeys(getwebelementNoWait("//div[label[text()='"+
					// labelname +"']]//input"), expectedValueToAdd);

					webDriver.findElement(By.xpath("(//div[label[text()='" + labelname + "']]//div[contains(text(),'"
							+ expectedValueToAdd + "')])[1]")).click();

				}
			} else {
				Report.LogInfo("INFO", labelname + " is not displaying", "FAIL");
				// ExtentTestManager.getTest().log(LogStatus.FAIL, labelname + "
				// is not displaying");
				// System.out.println(labelname + " is not displaying");
			}
		} catch (NoSuchElementException e) {
			// ExtentTestManager.getTest().log(LogStatus.FAIL, labelname + " is
			// not displaying");
			System.out.println(labelname + " is not displaying");
		} catch (Exception ee) {
			ee.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					" NOt able to perform selection under " + labelname + " dropdown");
			System.out.println(" NO value selected under " + labelname + " dropdown");
		}
	}

	public void compareText_InViewPage_ForNonEditedFields(String labelname) throws InterruptedException {

		// String text = null;
		WebElement element = null;

		try {
			Thread.sleep(1000);
			// element = getwebelement("//div[div[label[contains(text(),'" +
			// labelname + "')]]]/div[2]");
			element = webDriver.findElement(By.xpath("//div[div[label[contains(text(),'" + labelname + "')]]]/div[2]"));
			String emptyele = element.getText().toString();

			Report.LogInfo("INFO", labelname + " field is not edited. It is displaying as '" + emptyele + "'", "PASS");
			/*
			 * ExtentTestManager.getTest().log(LogStatus.PASS, labelname +
			 * " field is not edited. It is displaying as '" + emptyele + "'");
			 * Log.info(labelname + " field is not edited. It is displaying as '" + emptyele
			 * + "'");
			 */
		} catch (Exception e) {
			e.printStackTrace();
			Report.LogInfo("INFO", labelname + " field is not displaying", "FAIL");

			/*
			 * ExtentTestManager.getTest().log(LogStatus.FAIL, labelname +
			 * " field is not displaying"); Log.info(labelname +
			 * " field is not displaying");
			 */
		}

	}

	public void SendkeyusingAction(Keys k) {
		Actions keyAction = new Actions(webDriver);
		keyAction.sendKeys(k).perform();
	}

	public void CompareText(String locator, String newValueFromExcel) throws IOException {
		// WebElement webElement = findWebElement(locator);
		String value = getTextFrom(locator);

		if (value.equals(newValueFromExcel)) {
			System.out.println("Field matches new value");
		} else {
			System.out.println("Field is not edited");
		}
	}

	public boolean isElementPresent(String locator) {
		String loc = null;
		try {
			setImplicitWaitTimeout(1);
			WebElement element = findWebElement(locator);

			if (element.isDisplayed() && element.isEnabled())
				return true;

		} catch (NoSuchElementException e) {
			return false;
			// throw e;
		} catch (Exception e) {
			return false;
			// throw e;
		}
		return false;
	}

	public void SelectDropdownValueUnderSelectTag(String labelname, String dropdownToBeSelectedInTheEnd,
			String dropdownXpath) throws InterruptedException, IOException {
		{
			// SelectDropdownValueUnderSelectTag
			boolean availability = false;
			List<String> ls = new ArrayList<String>();

			try {
				// availability=getwebelement(xml.getlocator("//locators/" +
				// application + "/"+ dropdownXpath +"")).isDisplayed();
				if (isVisible(dropdownXpath)) {
					Report.LogInfo("INFO", " dropdown is displaying", "PASS");
					ExtentTestManager.getTest().log(LogStatus.PASS, labelname + " dropdown is displaying");
					// System.out.println(labelname + " dropdown is
					// displaying");

					// WebElement el =getwebelement(xml.getlocator("//locators/"
					// + application + "/"+ dropdownXpath +""));
					WebElement el = findWebElement(dropdownXpath);
					Select sel = new Select(el);

					String firstSelectedOption = sel.getFirstSelectedOption().getText();
					// ExtentTestManager.getTest().log(LogStatus.PASS, "By
					// default "+ labelname+" dropdown is displaying as:
					// "+firstSelectedOption);
					// System.out.println("By default "+ labelname+" dropdown is
					// displaying as: "+firstSelectedOption);

					List<WebElement> we = sel.getOptions();

					for (WebElement a : we) {
						if (!a.getText().equals("select")) {
							ls.add(a.getText());

						}
					}

					Report.LogInfo("INFO", "list of values inside " + labelname + " dropdown is: " + ls, "PASS");
					ExtentTestManager.getTest().log(LogStatus.PASS,
							"list of values inside " + labelname + " dropdown is: " + ls);
					// System.out.println("list of values inside "+labelname+"
					// dropdown is: "+ls);

					if (dropdownToBeSelectedInTheEnd.equalsIgnoreCase("null")) {
						Report.LogInfo("INFO", "No values selected under " + labelname + " dropdown", "PASS");
						ExtentTestManager.getTest().log(LogStatus.PASS,
								"No values selected under " + labelname + " dropdown");
					} else {
						Select s1 = new Select(el);
						s1.selectByVisibleText(dropdownToBeSelectedInTheEnd);

						String SelectedValueInsideDropdown = sel.getFirstSelectedOption().getText();
						Report.LogInfo("INFO",
								labelname + " dropdown value selected as: " + SelectedValueInsideDropdown, "PASS");
						ExtentTestManager.getTest().log(LogStatus.PASS,
								labelname + " dropdown value selected as: " + SelectedValueInsideDropdown);
						// System.out.println(labelname+" dropdown value
						// selected as: "+SelectedValueInsideDropdown);
					}
				}

			} catch (NoSuchElementException e) {
				Report.LogInfo("INFO", labelname + " Value is not displaying", "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL, labelname + " Value is not displaying");
				// System.out.println(labelname + " value is not displaying");
			} catch (Exception ee) {
				ee.printStackTrace();
				Report.LogInfo("INFO", " NOt able to perform selection under " + labelname + " dropdown", "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL,
						" NOt able to perform selection under " + labelname + " dropdown");
				// System.out.println(" NO value selected under "+ labelname + "
				// dropdown");
			}
		}
	}

	public void ClearAndEnterTextValue(String labelname, String xpath, String newValue) {
		WebElement element = null;
		try {
			// Thread.sleep(1000);
			// element= getwebelement(xml.getlocator("//locators/" + application
			// + "/"+ xpath +""));
			element = findWebElement(xpath);
			String value = element.getAttribute("value");

			if (value.isEmpty()) {
				Report.LogInfo("INFO", "Step: '" + labelname + "' text field is empty", "PASS");
				ExtentTestManager.getTest().log(LogStatus.INFO, "Step: '" + labelname + "' text field is empty");

			} else {
				element.clear();
				Thread.sleep(1000);
				element.sendKeys(newValue);
				Report.LogInfo("INFO", "Step: Entered '" + newValue + "' into '" + labelname + "' text field", "PASS");
				ExtentTestManager.getTest().log(LogStatus.PASS,
						"Step: Entered '" + newValue + "' into '" + labelname + "' text field");
			}

		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					"Not able to enter '" + newValue + "' into '" + labelname + "' text field");
			e.printStackTrace();
		}

	}

	public void openLinkInNewTab(String xpath, String linkName) throws InterruptedException, AWTException, IOException {

		WebElement el;
		el = findWebElement(xpath);
		Actions action = new Actions(webDriver);
		Robot r = new Robot();

		action.keyDown(Keys.CONTROL).click(el).build().perform();
		ExtentTestManager.getTest().log(LogStatus.PASS, "Clicked on " + linkName + " link");
		Reporter.log("Clicked on " + linkName + " link");

	}

	public void Switchtotab() throws Exception {
		String parentWinHandle = webDriver.getWindowHandle();
		Set<String> totalopenwindow = webDriver.getWindowHandles();
		for (String handle : totalopenwindow) {
			if (!handle.equals(parentWinHandle)) {
				webDriver.switchTo().window(handle);
				Thread.sleep(4000);

			}
		}
		// driver.close();
		// driver.switchTo().window(parentWinHandle);
	}

	public void CloseProposalwindow() throws InterruptedException {
		String parentWinHandle = webDriver.getWindowHandle();
		Set<String> totalopenwindow = webDriver.getWindowHandles();
		if (totalopenwindow.size() > 1) {
			for (String handle : totalopenwindow) {
				if (!handle.equals(parentWinHandle)) {
					webDriver.switchTo().window(handle);

				}
			}
			webDriver.close();
			webDriver.switchTo().window(parentWinHandle);
		} else {
			Reporter.log("Something went wrong. Proposal has not be generated");
		}
	}

	public static String genRandomUptoThousand() {

		Random ran = new Random();
		int RandomNumber = ran.nextInt(1000) + 100;

		String random = Integer.toString(RandomNumber);

		return random;
	}

	public static String genRandomUptoHundred() {

		Random ran = new Random();
		int RandomNumber = ran.nextInt(50) + 49;

		String random = Integer.toString(RandomNumber);

		return random;
	}

	public void compareText_InViewPPPconfigurationPage(String labelname, String ExpectedText)
			throws InterruptedException {

		String text = null;
		WebElement element = null;

		try {
			Thread.sleep(1000);
			element = webDriver
					.findElement(By.xpath("(//div[div[label[contains(text(),'" + labelname + "')]]]//label)[2]"));
			String emptyele = element.getText().toString();

			if (emptyele != null && emptyele.isEmpty()) {

				emptyele = "Null";

				if (emptyele.equalsIgnoreCase(ExpectedText)) {
					Report.LogInfo("INFO", " The Expected value for '" + labelname
							+ "' field is same as the Acutal value '" + text + "'", "PASS");
					ExtentTestManager.getTest().log(LogStatus.PASS, " The Expected value for '" + labelname
							+ "' field is same as the Acutal value '" + text + "'");

				} else {
					Report.LogInfo("INFO",
							"The Expected value '" + ExpectedText + "' is not same as the Acutal value '" + text + "'",
							"FAIL");
					ExtentTestManager.getTest().log(LogStatus.FAIL,
							"The Expected value '" + ExpectedText + "' is not same as the Acutal value '" + text + "'");
				}

			} else {
				text = element.getText();
				if (text.equalsIgnoreCase(ExpectedText)) {
					ExtentTestManager.getTest().log(LogStatus.PASS, " The Expected value for '" + labelname
							+ "' field '" + ExpectedText + "' is same as the Acutal value '" + text + "'");
				} else if (ExpectedText.contains(text)) {
					ExtentTestManager.getTest().log(LogStatus.PASS, "The Expected value for '" + labelname + "' field '"
							+ ExpectedText + "' is same as the Acutal value '" + text + "'");
				} else {
					Report.LogInfo("INFO", "The Expected value for '" + labelname + "' field '" + ExpectedText
							+ "' is not same as the Acutal value '" + text + "'", "FAIL");
					ExtentTestManager.getTest().log(LogStatus.FAIL, "The Expected value for '" + labelname + "' field '"
							+ ExpectedText + "' is not same as the Acutal value '" + text + "'");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Report.LogInfo("INFO", labelname + " field is not displaying", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, labelname + " field is not displaying");
		}
	}

	public void scrolltoend() throws InterruptedException {// Or Scroll Down
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

		clickOnBankPage();
		waitForAjax();
		Actions action = new Actions(webDriver);
		action.keyDown(Keys.CONTROL).sendKeys(Keys.END).keyUp(Keys.CONTROL).perform();
	}

	public void scrollToTop() throws InterruptedException, IOException {

		try {
			WebElement element = findWebElement("//ol[@class='breadcrumb']//a[text()='Home']");
			((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(false);", element);
		} catch (StaleElementReferenceException e) {
			e.printStackTrace();
		}
	}

	public void scrolltoview(WebElement element) {

		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void ClickCommon(String labelname) throws InterruptedException {
		WebElement element = null;

		try {
			// Thread.sleep(1000);
			// element = getwebelement(.getlocator("//locators/" + application + "/"+ xpath
			// +"").replace("Value", labelname));
			element = webDriver.findElement(By.xpath("//label[text()='" + labelname + "']//parent::div//div//input"));
			if (element == null) {
				Report.LogInfo("INFO", "Step:  '" + labelname + "' not found", "FAIL");
				ExtentTestManager.getTest().log(LogStatus.FAIL, "Step:  '" + labelname + "' not found");
			} else {
				element.click();
				Report.LogInfo("INFO", "Step: Clicked on '" + labelname + "' button", "PASS");
				ExtentTestManager.getTest().log(LogStatus.PASS, "Step: Clicked on '" + labelname + "' button");
			}

		} catch (Exception e) {
			Report.LogInfo("INFO", "Step: Clicking on '" + labelname + "' button is unsuccessful", "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL,
					"Step: Clicking on '" + labelname + "' button is unsuccessful");
			e.printStackTrace();
		}
	}

	public void clickOnBankPage() {
		webDriver.findElement(By.xpath("(//body)[1]")).click();
	}

	public void javascriptexecutor(String locator) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].scrollIntoView(true);", locator);
	}

	///// Added Siebel
	public void PickValue(String value) {

		WebElement el = webDriver.findElement(By.xpath("//td[text()='" + value + "']/parent::*"));
		Moveon(el);
		// scrollIntoView(el);
		SendkeyusingAction(Keys.ENTER);
	}

	public void Moveon(WebElement el) {
		Actions action = new Actions(webDriver);

		action.moveToElement(el).build().perform();
	}

	public String GetTextFrom(WebElement el) {
		String actual = el.getText().toUpperCase().toString();
		// String actual1=el.getText().toUpperCase().toString();
		return actual;
	}

	public void ScrolltoElement(String xpath) throws InterruptedException, IOException {
		WebElement element = findWebElement(xpath);
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView();", (element));
	}

	public void addDropdownValues_commonMethodDivSpan(String labelname, String xpath, String expectedValueToAdd)
			throws InterruptedException {
		boolean availability = false;
		List<String> ls = new ArrayList<String>();
		waitForAjax();
		if (expectedValueToAdd.equalsIgnoreCase("null")) {

			ExtentTestManager.getTest().log(LogStatus.PASS, " No values selected under " + labelname + " dropdown");
			System.out.println(" No values selected under " + labelname + " dropdown");
		} else {

			webDriver.findElement(By.xpath("//div[label[text()='" + labelname + "']]//div[text()='×']")).click();

			// verify list of values inside dropdown
			List<WebElement> listofvalues = webDriver.findElements(By.xpath("//span[@role='option']"));

			for (WebElement valuetypes : listofvalues) {
				ls.add(valuetypes.getText());
			}

			webDriver.findElement(By.xpath("//div[label[text()='" + labelname + "']]//input"))
					.sendKeys(expectedValueToAdd);

			webDriver.findElement(By.xpath("(//div[label[text()='" + labelname + "']]//span[contains(text(),'"
					+ expectedValueToAdd + "')])[1]")).click();

			Thread.sleep(1000);

			String actualValue = webDriver
					.findElement(By.xpath("//label[text()='" + labelname + "']/following-sibling::div//span"))
					.getText();
			Report.LogInfo("INFO", labelname + " dropdown value selected as: " + actualValue, "PASS");
			ExtentTestManager.getTest().log(LogStatus.PASS, labelname + " dropdown value selected as: " + actualValue);

		}

	}

	public void safeJavaScriptClick(WebElement element) throws Exception {
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				Reporter.log("Clicking on element with using java script click");

				((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
			} else {
				Reporter.log("Unable to click on element");
			}
		} catch (StaleElementReferenceException e) {
			Reporter.log("Element is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			Reporter.log("Element was not found in DOM " + e.getStackTrace());
		} catch (Exception e) {
			Reporter.log("Unable to click on element " + e.getStackTrace());
		}
	}

	public void getUrl(String URL) {

		SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().navigate().to(URL);

	}

	public String[] GetText(String locator) throws IOException {
		WebElement el = findWebElement(locator);
//			String text="Activation Start Confirmation [New]";

		String text = el.getText().toString();
		if (text.contains("[New][")) {
			String[] text2 = text.split(" \\[New\\]\\[");
			String[] text3 = text2[1].split("\\]");
			text2[1] = text3[0];
			Report.LogInfo("Read", "New Task name is " + text2[0] + " is read Successfully", "INFO");
			return text2;
		} else {
			String[] text2 = text.split(" \\[");
			Report.LogInfo("Read", "New Task name is " + text2[0] + " is read Successfully", "INFO");
			return text2;
		}

//			String[] text2=text;
	}

	public void SendKeys(WebElement el, String value) {
		el.sendKeys(value);
	}

	public List<WebElement> GetWebElements(final String locator) throws InterruptedException {
		List<WebElement> el = webDriver.findElements(By.xpath(locator));
		return el;
	}

	public static void windowFocus() {
		((JavascriptExecutor) webDriver).executeScript("window.focus();");
	}

	private static boolean isProcessRunning(String processName) throws IOException, InterruptedException {
		ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
		Process process = processBuilder.start();
		String tasksList = toString(process.getInputStream());

		return tasksList.contains(processName);
	}

	// http://stackoverflow.com/a/5445161/3764804
	private static String toString(InputStream inputStream) {
		Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
		String string = scanner.hasNext() ? scanner.next() : "";
		scanner.close();

		return string;
	}

	public int convertStringToInt(String val) {
		int value = Integer.parseInt(val);
		return value;
	}

	public String convertIntToString(int val) {
		String value = String.valueOf(val);
		return value;
	}

	public float convertStringtoFloat(String val) {
		float value = Float.parseFloat(val);
		return value;
	}

	public String convertFloattoString(float val) {
		String value = String.valueOf(val);
		return value;
	}

	public double convertStringtoDouble(String val) {
		double value = Double.parseDouble(val);
		return value;
	}

	public String convertDoubletoString(double val) {
		String value = String.valueOf(val);
		return value;
	}

}