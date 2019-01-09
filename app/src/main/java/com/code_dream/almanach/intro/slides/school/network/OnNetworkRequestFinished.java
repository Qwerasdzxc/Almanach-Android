package com.code_dream.almanach.intro.slides.school.network;

import com.code_dream.almanach.models.School;

import java.util.List;

/**
 * Created by Qwerasdzxc on 7/5/17.
 */

public interface OnNetworkRequestFinished extends com.code_dream.almanach.network.OnNetworkRequestFinished {

    void onSchoolsSuccessfullyReceived(List<School> receivedSchools);
}
