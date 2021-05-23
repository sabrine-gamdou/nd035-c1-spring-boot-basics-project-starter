package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper){
        this.credentialMapper = credentialMapper;
    }

    public int createCredential(Credential credential){
        return credentialMapper.insert(new Credential(null, credential.getUrl(), credential.getUsername(), credential.getKey(), credential.getPassword(), credential.getUserId()));
    }

    public List<Credential> getCredentials(User user){
        return credentialMapper.getCredentials(user.getUserId());
    }

    public Credential getCredentialById(int credentialId){
        return credentialMapper.getCredentialById(credentialId);
    }

    public int updateCredential(Credential credential){
        return credentialMapper.update(new Credential(credential.getCredentialId(), credential.getUrl(), credential.getUsername(), credential.getKey(), credential.getPassword(), credential.getUserId()));
    }

    public int deleteCredential(int credentialId){
        return credentialMapper.delete(credentialId);
    }

    public String generateKey(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
