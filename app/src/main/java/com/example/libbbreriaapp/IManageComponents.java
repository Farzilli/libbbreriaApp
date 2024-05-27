package com.example.libbbreriaapp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

public interface IManageComponents {
    void setComponents();
    void setListeners();
    default void setData() {}
}