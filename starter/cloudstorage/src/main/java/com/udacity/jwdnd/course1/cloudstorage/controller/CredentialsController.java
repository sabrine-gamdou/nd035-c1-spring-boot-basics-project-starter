package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import com.udacity.jwdnd.course1.cloudstorage.utils.FeedbackMessageWriter;
import com.udacity.jwdnd.course1.cloudstorage.utils.FeedbackMessages;
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

        int rowsAdded = -1;
        if( !credentialService.isCredentialUsernameAvailable(credential.getUsername())){
            this.credentialErrorMessage = FeedbackMessages.CREDENTIAL_USERNAME_USED;
        }else{
            rowsAdded = credentialService.createCredential(credential);
        }
        if( rowsAdded < 0 && credentialErrorMessage == null){
            this.credentialErrorMessage = FeedbackMessages.CREDENTIAL_CREATION_ERROR;
        }
        if(credentialErrorMessage == null){
           feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialSuccess",
                   FeedbackMessages.CREDENTIAL_CREATED_SUCCESSFULLY );
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
            this.credentialErrorMessage = FeedbackMessages.CREDENTIAL_UPDATE_ERROR;
        }
        if(credentialError == null){
            feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialSuccess",
                    FeedbackMessages.CREDENTIAL_UPDATED_SUCCESSFULLY);
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
            this.credentialErrorMessage = FeedbackMessages.CREDENTIAL_DELETION_ERROR;
        }
        if(credentialError == null){
            feedbackMessageWriter.redirectMessages(redirectAttributes,"credentialSuccess",
                    FeedbackMessages.CREDENTIAL_DELETED_SUCCESSFULLY);
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
