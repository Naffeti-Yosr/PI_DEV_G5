package com.esprit.utils;

import java.util.prefs.Preferences;

public class RememberMeUtil {
    private static final String PREF_NODE = "com.esprit.rememberme";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static Preferences prefs = Preferences.userRoot().node(PREF_NODE);

    public static void saveEmail(String email) {
        prefs.put(KEY_EMAIL, email);
    }

    public static String getSavedEmail() {
        return prefs.get(KEY_EMAIL, null);
    }

    public static void clearSavedEmail() {
        prefs.remove(KEY_EMAIL);
    }

    public static void savePassword(String password) {
        prefs.put(KEY_PASSWORD, password);
    }

    public static String getSavedPassword() {
        return prefs.get(KEY_PASSWORD, null);
    }

    public static void clearSavedPassword() {
        prefs.remove(KEY_PASSWORD);
    }

    public static void clearAll() {
        clearSavedEmail();
        clearSavedPassword();
    }
}
