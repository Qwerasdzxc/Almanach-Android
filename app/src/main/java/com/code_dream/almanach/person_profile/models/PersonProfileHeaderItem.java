package com.code_dream.almanach.person_profile.models;

import com.code_dream.almanach.models.Person;

/**
 * Created by Qwerasdzxc on 11/9/17.
 */

public class PersonProfileHeaderItem {

    private String schoolName;

    private Person.FriendType friendType;

    public PersonProfileHeaderItem(String schoolName, Person.FriendType friendType) {

        this.schoolName = schoolName;
        this.friendType = friendType;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Person.FriendType getFriendType() {
        return friendType;
    }

    public void setFriendType(Person.FriendType friendType) {
        this.friendType = friendType;
    }
}
