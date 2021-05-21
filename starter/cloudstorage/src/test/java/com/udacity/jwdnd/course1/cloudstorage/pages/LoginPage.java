package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "submit-button")
    private WebElement submit;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void login(){
        submit.click();
    }

    private void enterUsername(String username){
        this.username.clear();
        this.username.sendKeys(username);
    }

    private void enterPassword(String password){
        this.password.clear();
        this.password.sendKeys(password);
    }

    public void loginUser(String username, String password){
        enterUsername(username);
        enterPassword(password);
        login();
    }
}
