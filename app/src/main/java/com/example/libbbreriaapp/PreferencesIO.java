package com.example.libbbreriaapp;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesIO {
    private static final String PREFS_NAME = "MyAppPrefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesIO(Context context, String prefs_name) {
        this.sharedPreferences = context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public PreferencesIO(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public void set(String key, String value) {
        this.editor.putString(key, value);
        this.editor.apply();
    }

    public String get(String key, String defaultValue) {
        return this.sharedPreferences.getString(key, defaultValue);
    }

    public void remove(String key) {
        this.editor.remove(key);
        this.editor.apply();
    }

    public void clear() {
        this.editor.clear();
        this.editor.apply();
    }
}
