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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private String baseURL;

	private static WebDriver driver;

	/*Test User Data*/
	private String firstname = "mango";
	private String lastname = "kawazaki";
	private String username = "admin";
	private String password = "admin";

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + port;
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

	public void signup(){
		/* Signup */
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signupUser(firstname,lastname,username,password);
	}

	public void login(){
		/* Login */
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginUser(username,password);
	}

	/*
	* A test that verifies that an unauthorized user can only access the login and signup pages
	* */
	@Test
	@Order(1)
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

	/*
	 * A test that signs up a new user, logs in, verifies that the home page is accessible,
	 * logs out, and verifies that the home page is no longer accessible.
	 * */
	@Test
	@Order(2)
	public void testUserSignupLoginAndHomeAccess(){
		signup();
		login();
		assertEquals("Home", driver.getTitle());
	}

	@Test
	@Order(3)
	public void testAccessAfterLogout(){
		HomePage homePage = new HomePage(driver);
		assertEquals("Home", driver.getTitle()); //make sure home is accessible at this point

		/* Logout */
		homePage.logout();
		assertNotEquals("Home", driver.getTitle());
		assertEquals("Login", driver.getTitle());
	}


	/*
	* Notes
	* */
	/*
	*  A test that creates a note, and verifies it is displayed
	* */
	@Test
	@Order(4)
	public void testCreateNoteAndVerifyDisplayedValues(){
		String title = "Example Note Title";
		String description = "Example Note Description";

		login();

		HomePage homePage = new HomePage(driver);
		homePage.createNote(title,description);
		assertEquals(title,homePage.getDisplayedNoteTitle());
		assertEquals(description,homePage.getDisplayedNoteDescription());

	}


}
