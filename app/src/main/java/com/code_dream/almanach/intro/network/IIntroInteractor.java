package com.code_dream.almanach.intro.network;

/**
 * Created by Qwerasdzxc on 1/31/17.
 */

public interface IIntroInteractor {
    void sendRegisterRequest(OnRegisterFinishedListener listener, String fullName, String dob, String email, String password, String allSubjects);

    void updateFirebaseToken(OnRegisterFinishedListener listener, String firebaseToken);
}
