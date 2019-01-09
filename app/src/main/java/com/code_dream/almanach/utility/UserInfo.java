package com.code_dream.almanach.utility;

import com.code_dream.almanach.models.Person;
import com.code_dream.almanach.models.TimetableItem;

import java.util.ArrayList;

/**
 * Created by Qwerasdzxc on 5/25/17.
 */

public final class UserInfo {

    public static Person CURRENT_USER;

    public static String TOKEN = "";
    public static String FIREBASE_TOKEN = "";

    public static ArrayList<TimetableItem> SUBJECTS = new ArrayList<>();
}
