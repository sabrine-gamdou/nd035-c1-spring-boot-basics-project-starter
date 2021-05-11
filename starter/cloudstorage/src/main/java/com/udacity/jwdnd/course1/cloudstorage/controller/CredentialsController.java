package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("/credentials")
public class CredentialsController {

    private final CredentialService credentialService;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public String credentialError = null;
    public String credentialSuccess = null;

    public CredentialsController(CredentialService credentialService, UserService userService, EncryptionService encryptionService){
        this.credentialService = credentialService;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String createCredential(@ModelAttribute Credential credential, Model model,
                                   Authentication authentication){

        getUserAndPassword(credential, authentication);

        int rowsAdded = credentialService.createCredential(credential);
        if( rowsAdded < 0 ){
            credentialError = "There was an error creating the credential, please try again.";
        }
        if(credentialError == null){
            model.addAttribute("credentialSuccess", "Credential created successfully!");
        }else{
            model.addAttribute("credentialError", credentialError);
        }
        return "redirect:/home";
    }

    @PutMapping
    public String updateCredential(@ModelAttribute Credential credential, Model model,
                                   Authentication authentication){

        getUserAndPassword(credential, authentication);

        int rowsAdded = credentialService.updateCredential(credential);
        if( rowsAdded < 0 ){
            credentialError = "There was an error updating the credential, please try again.";
        }
        if(credentialError == null){
            model.addAttribute("credentialSuccess", "Credential updated successfully!");
        }else{
            model.addAttribute("credentialError", credentialError);
        }
        return "redirect:/home";
    }

    @DeleteMapping
    public String deleteCredential(@ModelAttribute Credential credential, Model model,
                                   Authentication authentication){

        this.credentialSuccess = null;
        this.credentialError = null;
        credential.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = credentialService.deleteCredential(credential.getCredentialId());
        if( rowsAdded < 0 ){
            credentialError = "There was an error deleting the credential, please try again.";
        }
        if(credentialError == null){
            model.addAttribute("credentialSuccess", "Credential deleted successfully!");
        }else{
            model.addAttribute("credentialError", credentialError);
        }
        return "redirect:/home";
    }

    private void getUserAndPassword(@ModelAttribute Credential credential, Authentication authentication) {
        this.credentialSuccess = null;
        this.credentialError = null;

        credential.setUserId(userService.getUser(authentication.getName()).getUserId());

        String key = credentialService.generateKey();
        credential.setKey(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setPassword(encryptedPassword);
    }



}
