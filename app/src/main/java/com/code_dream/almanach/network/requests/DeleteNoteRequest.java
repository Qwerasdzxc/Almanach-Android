package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 10/23/17.
 */

public class DeleteNoteRequest {

    private int id;

    public DeleteNoteRequest(int noteId){
        this.id = noteId;
    }

    public int getId(){
        return id;
    }
}
