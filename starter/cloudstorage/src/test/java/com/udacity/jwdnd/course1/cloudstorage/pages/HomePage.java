package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    /*2. Write Tests for Note Creation, Viewing, Editing, and Deletion.
        Write a test that creates a note, and verifies it is displayed.
        Write a test that edits an existing note and verifies that the
        changes are displayed.
        Write a test that deletes a note and verifies that the
        note is no longer displayed.*/
    @FindBy(css = "#logout")
    private WebElement logout;

    /* Notes */
    @FindBy(css ="#note-title")
    private WebElement noteTitle;

    @FindBy(css ="#note-description")
    private WebElement noteDescription;

    @FindBy(id = "note-submit")
    private WebElement noteSubmit;

    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(css = "#displayed-note-title")
    private WebElement displayedNoteTitle;

    @FindBy(css = "#displayed-note-description")
    private WebElement displayedNoteDescription;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout(){
        delay(this.logout);
        clickButton(logout);
    }

    public void createNote(String title, String description){
        clickButton(notesTab);
        clickButton(addNoteButton);
        enterNoteTitle(title);
        enterNoteDescription(description);
        clickButton(noteSubmit);
        clickButton(notesTab);
    }

    public String getDisplayedNoteTitle(){
        delay(this.displayedNoteTitle);
        return this.displayedNoteTitle.getText();
    }

    public String getDisplayedNoteDescription(){
        delay(this.displayedNoteDescription);
        return this.displayedNoteDescription.getText();
    }

    private void enterNoteTitle(String noteTitle){
        delay(this.noteTitle);
        this.noteTitle.clear();
        this.noteTitle.sendKeys(noteTitle);
    }

    private void enterNoteDescription(String noteDescription){
        delay(this.noteDescription);
        this.noteDescription.clear();
        this.noteDescription.sendKeys(noteDescription);
    }

    private void clickButton(WebElement webElement){
        delay(webElement);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
    }

    private void delay(WebElement webElement){
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(webElement)).click();
    }

}
