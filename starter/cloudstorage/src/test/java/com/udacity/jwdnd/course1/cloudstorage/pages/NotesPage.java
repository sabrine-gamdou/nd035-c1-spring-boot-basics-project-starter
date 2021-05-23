package com.udacity.jwdnd.course1.cloudstorage.pages;

import com.udacity.jwdnd.course1.cloudstorage.service.utils.WebElementUtilsService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class NotesPage {

    /* Notes */
    /* Notes home tab */
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    /* Title Elements on create and edit tabs*/
    @FindBy(css = "#note-title")
    private WebElement noteTitle;

    @FindBy(css = "#edit-note-title")
    private WebElement editNoteTitle;

    /* Description Elements on create and edit windows*/
    @FindBy(css = "#note-description")
    private WebElement noteDescription;

    @FindBy(css = "#edit-note-description")
    private WebElement editNoteDescription;

    /* Add,edit,delete buttons on the notes home tab*/
    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "edit-note-button")
    private WebElement editNoteButton;

    @FindBy(id = "delete-note-button")
    private WebElement deleteNoteButton;

    /* Displayed notes data on the notes home tab*/
    @FindBy(css = "#displayed-note-title")
    private WebElement displayedNoteTitle;

    @FindBy(css = "#displayed-note-description")
    private WebElement displayedNoteDescription;

    /* Submit buttons on create,edit,delete windows */
    @FindBy(id = "note-submit")
    private WebElement noteSubmit;

    @FindBy(css = "#edit-note-submit")
    private WebElement editNoteSubmit;

    @FindBy(css = "#delete-note-submit")
    private WebElement deleteNoteSubmit;

    @Autowired
    private WebElementUtilsService webElementUtilsService;

    private final WebDriver driver;

    public NotesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.webElementUtilsService = new WebElementUtilsService();
        this.webElementUtilsService.setDriver(this.driver);
    }

    public void createNote(String title, String description){
        webElementUtilsService.clickButton(notesTab);
        webElementUtilsService.clickButton(addNoteButton);
        webElementUtilsService.enterValue(this.noteTitle,title);
        webElementUtilsService.enterValue(this.noteDescription,description);
        webElementUtilsService.clickButton(noteSubmit);
        webElementUtilsService.clickButton(notesTab);
    }

    public void editNote(String title, String description){
        webElementUtilsService.clickButton(notesTab);
        webElementUtilsService.clickButton(editNoteButton);
        webElementUtilsService.enterValue(this.editNoteTitle,title);
        webElementUtilsService.enterValue(this.editNoteDescription,description);
        webElementUtilsService.clickButton(editNoteSubmit);
        webElementUtilsService.clickButton(notesTab);
    }

    public void deleteNote(){
        webElementUtilsService.clickButton(notesTab);
        webElementUtilsService.clickButton(deleteNoteButton);
        webElementUtilsService.clickButton(deleteNoteSubmit);
        webElementUtilsService.clickButton(notesTab);
    }

    public String getDisplayedNoteTitle(){
        webElementUtilsService.delay(this.displayedNoteTitle);
        return this.displayedNoteTitle.getText();
    }

    public String getDisplayedNoteDescription(){
        webElementUtilsService.delay(this.displayedNoteDescription);
        return this.displayedNoteDescription.getText();
    }

}
