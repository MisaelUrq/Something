package com.urquieta.something.platform;

// @Android<>import com.urquieta.something.platform.android.AndroidRenderer;
import com.urquieta.something.platform.pc.PCRenderer;// @PC

import com.urquieta.something.platform.Screen;

public class Renderer extends PCRenderer { // @Class
    private float point_size = 0.01f;

    // NOTE(Misael): Should this be here? or in the parent class?
    // NOTE(Misael): We should pass only the screen, not the renderer.
    private class PixelPoint {
        public int x, y;
        public PixelPoint(Renderer r, float x, float y) {
            this.x = r.screen.XPositionInPixels(x);
            this.y = r.screen.YPositionInPixels(y);
        }
    }

    public Renderer(Screen screen) {
        super(screen);
    }

    public void DrawRect(float x, float y, float width, float height, int color) {
        PixelPoint pixel_point = new PixelPoint(this, x, -y);
        PixelPoint distance_point = new PixelPoint(this, width, -height);
        super.DrawRect(pixel_point.x,
                       pixel_point.y,
                       distance_point.x,
                       distance_point.y, color);
    }

    public void DrawCircle(float x, float y, float radius, int color) {
        PixelPoint pixel_point = new PixelPoint(this, x, -y);
        int pixel_radius  = super.screen.lengthInPixels(radius);
        super.DrawCircle(pixel_point.x, pixel_point.y,
                         pixel_radius, color);
    }

    public void DrawLine(float x1, float y1, float x2, float y2, int color) {
        PixelPoint point_1 = new PixelPoint(this, x1, -y1);
        PixelPoint point_2 = new PixelPoint(this, x2, -y2);
        super.DrawLine(point_1.x, point_1.y,
                       point_2.x, point_2.y,
                       color);
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
