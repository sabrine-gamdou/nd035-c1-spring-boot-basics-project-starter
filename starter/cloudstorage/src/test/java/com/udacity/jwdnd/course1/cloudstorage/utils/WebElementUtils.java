package com.udacity.jwdnd.course1.cloudstorage.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebElementUtils {

    private WebDriver driver;

    public void enterValue(WebElement webElement, String value){
        delay(webElement);
        webElement.clear();
        webElement.sendKeys(value);
    }

    public void setDriver(WebDriver webDriver){
        this.driver = webDriver;
    }

    public void clickButton(WebElement webElement){
        delay(webElement);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
    }

    public void delay(WebElement webElement){
        new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(webElement));
    }

}
