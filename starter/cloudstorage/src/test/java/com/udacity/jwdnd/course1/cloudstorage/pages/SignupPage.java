package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignupPage {

    @FindBy(id ="submit-signup")
    private WebElement signup;

    @FindBy(id = "inputFirstName")
    private WebElement firstname;

    @FindBy(id = "inputLastName")
    private WebElement lastname;

    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id ="login-link")
    private WebElement login;

    public SignupPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public void signup(){
        signup.click();
    }

    public void login(){
        login.click();
    }

    public String getDisplayedFirstname(){
        return firstname.getAttribute("value");
    }

    public String getDisplayedLastname(){
        return lastname.getAttribute("value");
    }

    public String getDisplayedUsername(){
        return username.getAttribute("value");
    }

    public String getDisplayedPassword(){
        return password.getAttribute("value");
    }

    private void enterFirstname(String firstname){
        this.firstname.clear();
        this.firstname.sendKeys(firstname);
    }

    private void enterLastname(String lastname){
        this.lastname.clear();
        this.lastname.sendKeys(lastname);
    }

    private void enterUsername(String username){
        this.username.clear();
        this.username.sendKeys(username);
    }

    private void enterPassword(String password){
        this.password.clear();
        this.password.sendKeys(password);
    }

    public void signupUser(String firstname, String lastname, String username, String password){
        enterFirstname(firstname);
        enterLastname(lastname);
        enterUsername(username);
        enterPassword(password);
        signup();
    }

}
