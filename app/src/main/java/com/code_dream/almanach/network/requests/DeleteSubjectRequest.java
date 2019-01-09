package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 6/26/17.
 */

public class DeleteSubjectRequest {

    private String subjectName;

    public DeleteSubjectRequest(String subjectName){
        this.subjectName = subjectName;
    }

    public String getSubjectName(){
        return subjectName;
    }
}
