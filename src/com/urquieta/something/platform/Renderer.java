package com.urquieta.something.platform;

// @Android<>import com.urquieta.something.platform.android.AndroidRenderer;
import com.urquieta.something.platform.pc.PCRenderer;// @PC

import com.urquieta.something.platform.Screen;

public class Renderer extends PCRenderer { // @Class
    private float point_size = 0.01f;

    public Renderer(Screen screen) {
        super(screen);
    }

    public void DrawRect(float x, float y, float width, float height, int color) {
        int pixel_x = super.screen.XPositionInPixels(x);
        int pixel_y = super.screen.YPositionInPixels(-y);
        int pixel_width = super.screen.XPositionInPixels(width);
        int pixel_height = super.screen.YPositionInPixels(-height);
        super.DrawRect(pixel_x,
                       pixel_y,
                       pixel_width,
                       pixel_height, color);
    }

    public void DrawCircle(float x, float y, float radius, int color) {
        int pixel_x = super.screen.XPositionInPixels(x);
        int pixel_y = super.screen.YPositionInPixels(-y);
        int pixel_redius  = super.screen.lengthInPixels(radius);
        super.DrawCircle(pixel_x, pixel_y, pixel_redius, color);
    }

    public void BeginDraw() {
        super.screen.beginDraw();
        this.DrawRect(-1f, 1f, 1f, -1f, 0xFFFFFFFF);
    }

    public void DrawPoint(float x, float y, int color) {
        this.DrawRect(x - point_size, y + point_size,
                      x + point_size, y - point_size,
                      color);
    }

    public void EndDraw() {
        super.screen.endDraw();
    }
}
