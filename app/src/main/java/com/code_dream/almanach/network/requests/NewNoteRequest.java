package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 10/22/17.
 */

public class NewNoteRequest {

    private String title;

    private String content;

    public NewNoteRequest(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String getTitle(){
        return title;
    }

    public String getContent() {
        return content;
    }
}
