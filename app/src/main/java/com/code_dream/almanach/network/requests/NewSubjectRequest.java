package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 6/26/17.
 */

public class NewSubjectRequest {

    private String subjectName;

    public NewSubjectRequest(String subjectName){
        this.subjectName = subjectName;
    }

    public String getSubjectName(){
        return subjectName;
    }
}
