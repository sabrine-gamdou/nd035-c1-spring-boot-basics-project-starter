package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper){
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note){
        return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }

    public List<Note> getNotes(User user){
        return noteMapper.getNotes(user.getUserId());
    }

    public int updateNote(Note note) {
        return noteMapper.update(new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }

    public int deleteNote(int noteId){
        return noteMapper.delete(noteId);
    }

    public boolean isNoteAvailable(String noteTitle){
        return noteMapper.getNote(noteTitle) == null;
    }
}
