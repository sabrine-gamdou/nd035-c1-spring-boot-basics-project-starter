package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
public class FilesController {

    private final FileService fileService;
    private final UserService userService;

    public String fileError = null;
    public String fileErrorMessage = null;
    public String fileSuccess = null;
    public String fileSuccessMessage = null;

    public FilesController(FileService fileService, UserService userService){
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/download/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Integer fileId) {

        File file = fileService.findFileById(fileId);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + file.getFilename());
        header.add("Cache-control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        ByteArrayResource resource = new ByteArrayResource((file.getFileData()));
        return ResponseEntity.ok()
                .headers(header)
                .body(resource);
    }

    @PostMapping("/upload")
    public String createFile(@RequestParam("fileUpload") MultipartFile multipartFile, RedirectAttributes redirectAttributes,
                             Authentication authentication) {
        this.fileError = null;
        this.fileErrorMessage = null;
        this.fileSuccess = null;
        this.fileSuccessMessage = null;

        int userId = userService.getUser(authentication.getName()).getUserId();
        if (this.fileService.isFileNameAvailable(multipartFile, userId) && !multipartFile.isEmpty()) {
            try {
                fileService.createFile(new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                        multipartFile.getSize(), userId, multipartFile.getBytes()));
                redirectAttributes.addFlashAttribute("fileSuccess", true);
                redirectAttributes.addFlashAttribute("fileSuccessMessage", "File created successfully!");
                return "redirect:/home";

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("fileError", true);
                redirectAttributes.addFlashAttribute("fileErrorMessage", e.toString());
                return "redirect:/home";
            }
        } else if (multipartFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileError", true);
            redirectAttributes.addFlashAttribute("fileErrorMessage", "File should not be empty!");
            return "redirect:/home";
        } else {
            redirectAttributes.addFlashAttribute("fileError", true);
            redirectAttributes.addFlashAttribute("fileErrorMessage", "File name already used");
            return "redirect:/home";
        }
    }

    @DeleteMapping
    public String deleteFile(@ModelAttribute File file, RedirectAttributes redirectAttributes,
                             Authentication authentication){
        this.fileError = null;
        this.fileErrorMessage = null;
        this.fileSuccess = null;
        this.fileSuccessMessage = null;

        file.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = fileService.deleteFile(file.getFileId());
        if( rowsAdded < 0 ){
            fileErrorMessage = "There was an error deleting the file, please try again.";
        }
        if(fileError == null){
            redirectAttributes.addFlashAttribute("fileSuccess",true);
            redirectAttributes.addFlashAttribute("fileSuccessMessage","File deleted successfully!");
        }else{
            redirectAttributes.addFlashAttribute("fileError",true);
            redirectAttributes.addFlashAttribute("fileErrorMessage",this.fileErrorMessage);
        }

        return "redirect:/home";
    }
}
