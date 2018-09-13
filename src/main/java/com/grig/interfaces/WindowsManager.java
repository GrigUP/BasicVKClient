package com.grig.interfaces;

import com.grig.services.Window;

public interface WindowsManager {

    void addAllWindows(Window... windows);

    void changeWindow(String windowName);

    void show();

    void close();
}
