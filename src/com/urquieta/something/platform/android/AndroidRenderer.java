package com.urquieta.something.platform.android;

import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Canvas;

import com.urquieta.something.platform.Screen;

public class AndroidRenderer {
    public Screen screen;

    public AndroidRenderer(Screen screen) {
        this.screen = screen;
    }

    public void DrawText(String str, int x, int y, int color) {
        if (this.screen.getCanvas() != null) {
            Paint paint = new Paint();
            paint.setColor(color);
            this.screen.getCanvas().drawText(str, x, y, paint);
        }
    }

    public void DrawRect(int x, int y, int width, int height, int color) {
        if (this.screen.getCanvas() != null)
        {
            Paint paint = new Paint();
            paint.setColor(color);
            this.screen.getCanvas().drawRect(x, y, width, height, paint);
        }
    }

    public void DrawCircle(int x, int y, int radius, int color) {
        if (this.screen.getCanvas() != null) {
            Paint paint = new Paint();
            paint.setColor(color);
            float pos_x = x-radius;
            float pos_y = y-radius;
            radius = 2 * radius;
            RectF rect = new RectF(pos_x, pos_y,
                                   radius+pos_x,
                                   radius+pos_y);
            this.screen.getCanvas().drawOval(rect, paint);
        }
    }

    public void DrawImage(AndroidImage image, int x, int y) {
        if (this.screen.getCanvas() != null && image.GetBuffer() != null) {
            this.screen.getCanvas().drawBitmap(image.GetBuffer(), x, y, null);
        }
    }

    public void DrawLine(int x1, int y1, int x2, int y2,
                         int thickness, int color) {
        if (this.screen.getCanvas() != null) {
            Paint paint = new Paint();
            float width = paint.getStrokeWidth();
            paint.setColor(color);
            paint.setStrokeWidth(thickness);
            this.screen.getCanvas().drawLine(x1, y1, x2, y2, paint);
            paint.setStrokeWidth(width);

        }
    }

    public void DrawLine(int x1, int y1, int x2, int y2, int color) {
        if (this.screen.getCanvas() != null) {
            Paint paint = new Paint();
            paint.setColor(color);
            this.screen.getCanvas().drawLine(x1, y1, x2, y2, paint);
        }
    }
}
