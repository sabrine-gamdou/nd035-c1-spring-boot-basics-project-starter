package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.*;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private CredentialService credentialService;

	@Autowired
	private EncryptionService encryptionService;

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

		NotesPage notesPage = new NotesPage(driver);
		notesPage.createNote(title,description);
		assertEquals(title,notesPage.getDisplayedNoteTitle());
		assertEquals(description,notesPage.getDisplayedNoteDescription());

	}

	/*
	* A test that edits an existing note and verifies that the
	* */
	@Test
	@Order(5)
	public void testEditExistingNoteAndVerifyDisplayedValues(){
		String newTitle = "Lights off";
		String newDescription = "Don't forget to turn the lights off!";

		NotesPage notesPage = new NotesPage(driver);
		notesPage.editNote(newTitle,newDescription);
		assertEquals(newTitle,notesPage.getDisplayedNoteTitle());
		assertEquals(newDescription,notesPage.getDisplayedNoteDescription());
	}

	/*
	* A test that deletes a note and verifies that the note is no longer displayed
	* */
	@Test
	@Order(6)
	public void testDeleteExistingNoteAndVerifyItIsNotDisplayed(){
		NotesPage notesPage = new NotesPage(driver);
		notesPage.deleteNote();
		assertThrows(TimeoutException.class,notesPage::getDisplayedNoteTitle);
		assertThrows(TimeoutException.class,notesPage::getDisplayedNoteDescription);
	}


	/*
	* A test that creates a set of credentials,verifies that they
	* are displayed, and verifies that the displayed
	* password is encrypted.
	* */
	@Test
	@Order(7)
	public void testCreatingCredentialAndVerifyCredentialDisplayedAndIfPasswordIsEncrypted(){
		String url = "https://www.google.com/";
		String username = "admin";
		String password = "adminPassword";

		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.createCredential(url,username,password);

		String encodedKey = credentialService.getCredentialById(1).getKey();
		String encryptedPassword =  encryptionService.encryptValue(password, encodedKey);

		assertEquals(url,credentialsPage.getDisplayedCredentialUrl());
		assertEquals(username,credentialsPage.getDisplayedCredentialUsername());
		assertEquals(encryptedPassword,credentialsPage.getDisplayedCredentialPassword());

	}


	/*
	 * A test that views an existing set of credentials,
	 * verifies that the viewable password is unencrypted,
	 * edits the credentials, and verifies that the changes are displayed.
	 * */
	@Test
	@Order(8)
	public void testVerifyViewablePasswordIsUnencryptedAndEditCredentialAndVerifyChangedAreDisplayed(){
		String newUrl = "https://stackoverflow.com/";
		String newUsername = "admin1";
		String newPassword = "admin2";

		CredentialsPage credentialsPage = new CredentialsPage(driver);

		String encodedKey = credentialService.getCredentialById(1).getKey();
		String decryptedPassword = encryptionService.decryptValue(credentialsPage.getDisplayedCredentialPassword(),encodedKey);

		assertEquals(decryptedPassword,credentialsPage.getDisplayedDecryptedCredentialPassword());
		assertNotEquals(credentialsPage.getDisplayedCredentialPassword(),credentialsPage.getDisplayedDecryptedCredentialPassword());

		credentialsPage.editCredential(newUrl,newUsername,newPassword);
		String encryptedPassword = encryptionService.encryptValue(newPassword,credentialService.getCredentialById(1).getKey());

		assertEquals(newUrl,credentialsPage.getDisplayedCredentialUrl());
		assertEquals(newUsername,credentialsPage.getDisplayedCredentialUsername());
		assertEquals(encryptedPassword,credentialsPage.getDisplayedCredentialPassword());

	}

	/*
	* A test that deletes an existing set of credentials and verifies
	* that the credentials are no longer displayed.
	* */
	@Test
	@Order(9)
	public void testDeleteExistingCredentialAndVerifyCredentialsNotDisplayed(){
		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.deleteCredential();

		assertThrows(TimeoutException.class,credentialsPage::getDisplayedCredentialUrl);
		assertThrows(TimeoutException.class,credentialsPage::getDisplayedCredentialUsername);
		assertThrows(TimeoutException.class,credentialsPage::getDisplayedCredentialPassword);
	}


}
