package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	public String baseURL;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + port;
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
			this.driver = null;
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get("http://localhost:" + this.port + "/signup");
		assertEquals("Signup", driver.getTitle());
	}

	/*
	* A test that signs up a new user, logs in, verifies that the home page is accessible,
	* logs out, and verifies that the home page is no longer accessible.
	* */
	@Test
	public void testUserSignupLoginAndHomeAccess(){
		/*Test User Data*/
		String firstname = "mango";
		String lastname = "kawazaki";
		String username = "admin";
		String password = "admin";

		/* Signup */
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signupUser(firstname,lastname,username,password);

		/* Login */
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginUser(username,password);
		assertEquals("Home", driver.getTitle());

		/* Logout */
		HomePage homePage = new HomePage(driver);
		homePage.logout();
		assertNotEquals("Home", driver.getTitle());
		assertEquals("Login", driver.getTitle());

	}

	/*
	* a test that verifies that an unauthorized user can only access the login and signup pages
	* */
	@Test
	public void testUnauthorizedUserAccess(){
		driver.get(baseURL+"/home");
		assertEquals("Login",driver.getTitle());

		driver.get(baseURL+"/logout");
		assertEquals("Login",driver.getTitle());

		driver.get(baseURL+"/files");
		assertEquals("Login",driver.getTitle());

		driver.get(baseURL+"/credentials");
		assertEquals("Login",driver.getTitle());

		driver.get(baseURL+"/notes");
		assertEquals("Login",driver.getTitle());
	}

}
