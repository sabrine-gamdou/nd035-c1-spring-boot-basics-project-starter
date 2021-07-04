package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.service.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.utils.FeedbackMessageWriter;
import com.udacity.jwdnd.course1.cloudstorage.utils.FeedbackMessages;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notes")
public class NotesController {

    private final NoteService noteService;
    private final UserService userService;

    private String noteError = null;
    private String noteErrorMessage = null;
    private String noteSuccess = null;
    private String noteSuccessMessage = null;

    private FeedbackMessageWriter feedbackMessageWriter;

    public NotesController(NoteService noteService, UserService userService){
        this.noteService = noteService;
        this.userService = userService;
        feedbackMessageWriter = new FeedbackMessageWriter();
    }

    /* It creates a new model object. */
    @PostMapping
    public String createNote(@ModelAttribute Note note, RedirectAttributes redirectAttributes,
                             Authentication authentication){
        this.noteError = null;
        this.noteErrorMessage = null;
        this.noteSuccess = null;
        this.noteSuccessMessage = null;
        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int descriptionLength = note.getNoteDescription().length();
        int rowsAdded = -1;
        if(note.getNoteDescription().length() > 1000){
            this.noteErrorMessage = FeedbackMessages.NOTE_DESCRIPTION_LENGTH_LIMIT_EXCEEDED;
            feedbackMessageWriter.redirectMessages(redirectAttributes,"isLongDescription",this.noteErrorMessage);
        }else if(!noteService.isNoteAvailable(note.getNoteTitle())){
            this.noteErrorMessage = FeedbackMessages.NOTE_TITLE_USED;
            feedbackMessageWriter.redirectMessages(redirectAttributes,"isLongDescription",this.noteErrorMessage);
        }else{
            rowsAdded = noteService.createNote(note);
        }
        if( rowsAdded < 0 ){
            this.noteErrorMessage = FeedbackMessages.NOTE_CREATION_ERROR;
        }
        if(noteErrorMessage == null && descriptionLength <= 1000){
            feedbackMessageWriter.redirectMessages(redirectAttributes,"noteSuccess",
                    FeedbackMessages.NOTE_CREATED_SUCCESSFULLY);
        }else{
            feedbackMessageWriter.redirectMessages(redirectAttributes,"noteError",
                    this.noteErrorMessage);
        }
        return "redirect:/home";
    }

    /* It updates model object. */
    @PutMapping
    public String updateNote(@ModelAttribute Note note, RedirectAttributes redirectAttributes,
                            Authentication authentication){
        this.noteError = null;
        this.noteErrorMessage = null;
        this.noteSuccess = null;
        this.noteSuccessMessage = null;

        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsUpdated = noteService.updateNote(note);
        if (rowsUpdated < 0){
            this.noteErrorMessage = FeedbackMessages.NOTE_UPDATE_ERROR;
        }
        if(noteError == null){
            feedbackMessageWriter.redirectMessages(redirectAttributes,"noteSuccess",
                    FeedbackMessages.NOTE_UPDATED_SUCCESSFULLY);
        }else{
            feedbackMessageWriter.redirectMessages(redirectAttributes,"noteError",
                    this.noteErrorMessage);
        }
        return "redirect:/home";
    }

    /* It deletes model object. */
    @DeleteMapping
    public String deleteNote(@ModelAttribute Note note, RedirectAttributes redirectAttributes,
                             Authentication authentication){
        this.noteError = null;
        this.noteErrorMessage = null;
        this.noteSuccess = null;
        this.noteSuccessMessage = null;

        note.setUserId(userService.getUser(authentication.getName()).getUserId());
        int noteId = note.getNoteId();
        int rowsUpdated = noteService.deleteNote(noteId);
        if (rowsUpdated < 0){
            this.noteErrorMessage = FeedbackMessages.NOTE_DELETION_ERROR;
        }
        if(noteError == null){
            feedbackMessageWriter.redirectMessages(redirectAttributes, "noteSuccess",
                    FeedbackMessages.NOTE_DELETED_SUCCESSFULLY);
        }else{
            feedbackMessageWriter.redirectMessages(redirectAttributes,"noteError",
                    this.noteErrorMessage);
        }

        return "redirect:/home";
    }


}
