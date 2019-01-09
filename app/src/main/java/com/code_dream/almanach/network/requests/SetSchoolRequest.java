package com.code_dream.almanach.network.requests;

/**
 * Created by Qwerasdzxc on 2/15/17.
 */

public class SetSchoolRequest {

    private String schoolName;

    public SetSchoolRequest(String schoolName){
        this.schoolName = schoolName;
    }

    public String getSchoolName(){
        return schoolName;
    }
}
