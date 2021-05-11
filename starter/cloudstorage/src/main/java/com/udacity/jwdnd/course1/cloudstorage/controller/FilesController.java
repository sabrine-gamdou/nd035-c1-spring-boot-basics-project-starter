package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.service.FileService;
import com.udacity.jwdnd.course1.cloudstorage.service.UserService;
import org.apache.coyote.Response;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FilesController {

    private final FileService fileService;
    private final UserService userService;

    public String fileError = null;
    public String fileSuccess = null;

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
    public String createFile(@RequestParam("fileUpload") MultipartFile multipartFile, Model model,
                             Authentication authentication){
        this.fileError = null;
        this.fileSuccess = null;

        int userId = userService.getUser(authentication.getName()).getUserId();
        /*if (this.fileService.isFileNameAvailable(multipartFile, userId)) {*/
            try {
                fileService.createFile(new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                                        multipartFile.getSize(), userId, multipartFile.getBytes()));
                model.addAttribute("fileSuccess", "File created successfully!");

                return "redirect:/home";

            } catch (Exception e) {
                System.out.println("This is the file data: " + multipartFile.getOriginalFilename() + " " + multipartFile.getSize());
                System.out.println(e.toString());
                model.addAttribute("fileError", e.toString());
                return "redirect:/home";
            }
        /*} else {
            model.addAttribute("fileError", "File name already used");
            model.addAttribute("files", fileService.getFiles(userService.getUser(authentication.getName())));
            return "redirect:/home";*/
       // }

    }

    @DeleteMapping
    public String deleteFile(@ModelAttribute File file, Model model,
                             Authentication authentication){
        this.fileError = null;
        this.fileSuccess = null;

        file.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = fileService.deleteFile(file.getFileId());
        if( rowsAdded < 0 ){
            fileError = "There was an error deleting the file, please try again.";
        }
        if(fileError == null){
            model.addAttribute("fileSuccess", "File deleted successfully!");
        }else{
            model.addAttribute("fileError", fileError);
        }

        return "redirect:/home";
    }
}
