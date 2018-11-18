package com.urquieta.something.platform;

import com.urquieta.something.platform.android.AndroidRenderer;
import com.urquieta.something.platform.Screen;

public class Renderer extends AndroidRenderer {
    public Renderer(Screen screen) {
        super(screen);
    }

    public void DrawRect(int x, int y, int width, int height, int color) {
        super.DrawRect(x, y, width, height, color);
    }
}
