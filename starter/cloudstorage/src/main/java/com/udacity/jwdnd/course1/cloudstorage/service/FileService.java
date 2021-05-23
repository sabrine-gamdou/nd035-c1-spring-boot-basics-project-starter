package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService {

    public final FileMapper fileMapper;

    public FileService(FileMapper fileMapper){
        this.fileMapper = fileMapper;
    }

    public int createFile(File file){
        return fileMapper.insert(new File(null, file.getFilename(), file.getContentType(),
                file.getFilesize(), file.getUserId(), file.getFileData()));
    }

    public File findFileById(int fileId){
        return fileMapper.findFileById(fileId);
    }

    public List<File> getFiles(User user){
        return fileMapper.getFiles(user.getUserId());
    }

    public int deleteFile(int fileId){
        return fileMapper.delete(fileId);
    }

    public boolean isFileNameAvailable(MultipartFile multipartFile, Integer userId) {
        boolean isFileNameAvailable;
        List <File> files = fileMapper.getFiles(userId);
        isFileNameAvailable = files.stream().noneMatch(currFile -> currFile.getFilename().equals(multipartFile.getOriginalFilename()));
        return isFileNameAvailable;
    }
}
