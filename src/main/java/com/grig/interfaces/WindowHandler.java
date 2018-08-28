package com.grig.interfaces;

import com.grig.services.Window;

public interface WindowHandler {

    void addAllWindows(Window... windows);

    void changeWindow(String windowName);

    void show();

    void close();
}
