package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 10/23/17.
 */

public class EditNoteRequest {

    private int id;

    private String title;

    private String content;

    public EditNoteRequest(int noteId, String title, String content){
        this.id = noteId;

        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
