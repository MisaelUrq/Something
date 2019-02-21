package com.urquieta.something.platform;

// @Android<>import com.urquieta.something.platform.android.AndroidRenderer;
import com.urquieta.something.platform.pc.PCRenderer;// @PC

import com.urquieta.something.platform.Screen;
import com.urquieta.something.game.util.Vec2;

// TODO(Misael): Move this to a renderer directory.
public class Renderer extends PCRenderer { // @Class
    private float point_size = 0.01f;

    // NOTE(Misael): Should this be here? or in the parent class?

    // NOTE(Misael): We should pass only the screen, not the renderer.
    private class PixelPoint {
        public int x, y;

        public PixelPoint(Renderer r, Vec2 position) {
            this(r, position.x, -position.y);
        }

        public PixelPoint(Renderer r, float x, float y) {
            this.x = r.screen.XPositionInPixels(x);
            this.y = r.screen.YPositionInPixels(y);
        }
    }

    public Renderer(Screen screen) {
        super(screen);
    }

    public void DrawText(String str, Vec2 position, int color) {
        PixelPoint pixel_point = new PixelPoint(this, position);
        super.DrawText(str, pixel_point.x, pixel_point.y, color);
    }

    public void DrawRect(float x, float y, float width, float height, int color) {
        super.DrawRect(x, y, width, height, 0.0f, color);
    }

    public void DrawRect(float x, float y, float width, float height, float z, int color) {
        super.DrawRect(x, y, width, height, z, color);
    }

    public void DrawImage(Image image, float x, float y) {
        PixelPoint pixel_point = new PixelPoint(this, x, -y);
        super.DrawImage(image, pixel_point.x, pixel_point.y);
    }

    public void DrawCircle(float x, float y, float radius, int color) {
        PixelPoint pixel_point = new PixelPoint(this, x, -y);
        int pixel_radius  = super.screen.lengthInPixels(radius);
        super.DrawCircle(pixel_point.x, pixel_point.y,
                         pixel_radius, color);
    }

    public void DrawLine(Vec2 p1, Vec2 p2, float thickness, int color) {
        PixelPoint point_1 = new PixelPoint(this, p1.x, -p1.y);
        PixelPoint point_2 = new PixelPoint(this, p2.x, -p2.y);
        int pixel_thickness = super.screen.lengthInPixels(thickness);
        super.DrawLine(point_1.x, point_1.y,
                       point_2.x, point_2.y,
                       pixel_thickness,
                       color);
    }

    public void DrawLine(float x1, float y1, float x2, float y2, float thickness, int color) {
        PixelPoint point_1 = new PixelPoint(this, x1, -y1);
        PixelPoint point_2 = new PixelPoint(this, x2, -y2);
        int pixel_thickness = super.screen.lengthInPixels(thickness);
        super.DrawLine(point_1.x, point_1.y,
                       point_2.x, point_2.y,
                       pixel_thickness,
                       color);
    }

    public void DrawLine(float x1, float y1, float x2, float y2, int color) {
        PixelPoint point_1 = new PixelPoint(this, x1, -y1);
        PixelPoint point_2 = new PixelPoint(this, x2, -y2);
        super.DrawLine(point_1.x, point_1.y,
                       point_2.x, point_2.y,
                       color);
    }

    public void BeginDraw() {
        // super.screen.beginDraw();
        // super.DrawRect(-1f, 1f, 1f, -1f, 0xffa2b5cd);
    }

    public void DrawPoint(float x, float y, int color) {
        super.DrawRect(x - point_size, y + point_size,
                       x + point_size, y - point_size,
                       0, color);
    }

    public void EndDraw() {
        super.screen.endDraw();
    }

    public final Screen GetScreen() {
        return this.screen;
    }
}
