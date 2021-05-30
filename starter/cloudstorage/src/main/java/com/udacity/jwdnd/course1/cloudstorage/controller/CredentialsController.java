package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.utils.FeedbackMessageWriter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {

    private final CredentialService credentialService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    private String credentialError = null;
    private String credentialErrorMessage = null;
    private String credentialSuccess = null;
    private String credentialSuccessMessage = null;

    private FeedbackMessageWriter feedbackMessageWriter;

    public CredentialsController(CredentialService credentialService, UserService userService, EncryptionService encryptionService){
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
        feedbackMessageWriter = new FeedbackMessageWriter();
    }

    @PostMapping
    public String createCredential(@ModelAttribute Credential credential, RedirectAttributes redirectAttributes,
                                   Authentication authentication){

        getUserAndPassword(credential, authentication);

        int rowsAdded = credentialService.createCredential(credential);
        if( rowsAdded < 0 ){
            this.credentialErrorMessage = "There was an error creating the credential, please try again.";
        }
        if(credentialError == null){
           feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialSuccess",
                    "Credential created successfully!");
        }else{
            feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialError",
                    this.credentialErrorMessage);
        }

        return "redirect:/home";
    }

    @PutMapping
    public String updateCredential(@ModelAttribute Credential credential, RedirectAttributes redirectAttributes,
                                   Authentication authentication){

        getUserAndPassword(credential, authentication);

        int rowsAdded = credentialService.updateCredential(credential);

        if( rowsAdded < 0 ){
            this.credentialErrorMessage = "There was an error updating the credential, please try again.";
        }
        if(credentialError == null){
            feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialSuccess",
                    "Credential updated successfully!");
        }else{
            feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialError",
                    this.credentialErrorMessage);
        }
        return "redirect:/home";
    }

    @DeleteMapping
    public String deleteCredential(@ModelAttribute Credential credential, RedirectAttributes redirectAttributes,
                                   Authentication authentication){

        this.credentialError = null;
        this.credentialErrorMessage = null;
        this.credentialSuccess = null;
        this.credentialSuccessMessage = null;

        credential.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = credentialService.deleteCredential(credential.getCredentialId());
        if( rowsAdded < 0 ){
            this.credentialErrorMessage = "There was an error deleting the credential, please try again.";
        }
        if(credentialError == null){
            feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialSuccess",
                    "Credential deleted successfully!");
        }else{
            feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialError",
                    this.credentialErrorMessage);
        }
        return "redirect:/home";
    }

    private void getUserAndPassword(@ModelAttribute Credential credential, Authentication authentication) {

        this.credentialError = null;
        this.credentialErrorMessage = null;
        this.credentialSuccess = null;
        this.credentialSuccessMessage = null;

        credential.setUserId(userService.getUser(authentication.getName()).getUserId());

        String key = credentialService.generateKey();
        credential.setKey(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setPassword(encryptedPassword);
    }

}
