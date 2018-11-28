package com.urquieta.something.platform;

// @Android<>import com.urquieta.something.platform.android.AndroidRenderer;
import com.urquieta.something.platform.pc.PCRenderer;// @PC

import com.urquieta.something.platform.Screen;

public class Renderer extends PCRenderer { // @Class
    public Renderer(Screen screen) {
        super(screen);
    }

    public void DrawRect(float x, float y, float width, float height, int color) {
        int pixel_x = super.screen.widthInPixels(x);
        int pixel_y = super.screen.heightInPixels(-y);
        int pixel_width = super.screen.widthInPixels(width);
        int pixel_height = super.screen.heightInPixels(-height);
        super.DrawRect(pixel_x,
                       pixel_y,
                       pixel_width,
                       pixel_height, color);
    }

    public void BeginDraw() {
        super.screen.beginDraw();
        this.DrawRect(-1f, 1f, 1f, -1f, 0xFFFFFFFF);
    }

    public void EndDraw() {
        super.screen.endDraw();
    }
}
